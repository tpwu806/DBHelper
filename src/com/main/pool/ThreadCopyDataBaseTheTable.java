package com.main.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.sql.common.utils4j.ADbServiceBase;
import com.sql.common.utils4j.DbService;
import com.sql.common.utils4j.IDbServiceBase;
import com.sql.common.utils4j.Test;
import com.sql.connect.AbstractConnectionUtil;
import com.sql.connect.ConnectionPoolManagerSingleton;
import com.sql.connect.IConnectionPool;

public class ThreadCopyDataBaseTheTable implements Runnable {
	private ConnectionPoolManagerSingleton manager ;	
	private IConnectionPool pcPool;
	private IConnectionPool padPool;
	private long lazyCheck = 1000*60;// 延迟多少时间后开始 检查
	private long periodCheck = 1000*60;// 检查频率
	public  ThreadCopyDataBaseTheTable(){
		
	}
	@Override
	public void run() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				// 3.其他状态进行检查，因为这里还需要写几个线程管理的类，暂时就不添加了
				System.out.println("************** copy start ***********");
				try {
					copyCore();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("************** copy stop ***********");
			}
		}, lazyCheck, periodCheck);
	}
	
	private void copyCore() throws SQLException{
		manager = ConnectionPoolManagerSingleton.getInstance();
		pcPool = manager.getPool("pcPool");
		
		Connection pcconn = pcPool.getConnection();
		List<Test> ts = select(pcconn);
		if(ts != null && ts.size() > 0){
			padPool = manager.getPool("padPool");
			Connection padconn = padPool.getConnection();
			boolean b= update(padconn, ts);
			if(b){
				System.out.println("copy ok!");
			}else{
				System.out.println("copy false!");
			}
		}
		
	}
	
	
	private boolean update(Connection conn, List<Test> ts) throws SQLException{
		Statement st;
		int number = 0;
		boolean b = false;
        try {  
        	//ADbServiceBase service = new ADbServiceBase();
        	st = conn.createStatement();
        	for(int i=0;i<ts.size();i++){
        		Test t = ts.get(i);
        		String name = t.getName().trim();
        		String password = t.getPassword().trim();
        		String sql = "insert into Test values("+"'"+ name +"','"+ password +"')";
        		System.out.println("执行sql： " + sql + "**********************");
        		 
        		if(st.execute(sql)){
        			number += 1;
        		}
        	}
        	if(number == ts.size())
        		b = true;
        	else
        		b = false;
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
        	AbstractConnectionUtil.BackPreparedStatement(conn,null,null); 
        }
		return b;
	}
	
	
	
	public List<Test> select(Connection conn) throws SQLException {
		List<Test> ts = null;
        final String sql = "select * from test ";  
        try {  
        	//ADbServiceBase service = new ADbServiceBase();
        	Statement st = conn.createStatement();
        	ResultSet rs = 	st.executeQuery(sql);
        	ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
    		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
    		ts = new ArrayList();
    		Test t = null;
    		while (rs.next()) {
    			
    			for (int i = 1; i <= columnCount; i++) {
    				t = new Test();
    				t.setName(rs.getString("name"));
    				t.setPassword(rs.getString("password"));
    				
    			}
    			ts.add(t);
    			//System.out.println("list:" + list.toString());
    		}
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
        	AbstractConnectionUtil.BackPreparedStatement(conn,null,null); 
        }
		return ts;  
    }  
	
	public List resultSetToList(ResultSet rs) throws java.sql.SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List list = new ArrayList();
		Map<String, Object> rowData = null;
		while (rs.next()) {
			rowData = new HashMap<String, Object>(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
			System.out.println("list:" + list.toString());
		}
		return list;
	}
	/*public void testQuery4(Connection conn) throws SQLException {  
        final String SQL = "select * from test where 1 = ?";  
        try {   
        	List<Test> test1Beans = (List<Test>) new QueryRunner().query(conn, SQL,  
                    new Object[] { "%1%" }, new BeanListHandler<Test>(  
                            Test.class));  
            if (null != test1Beans) {  
                for (Test test1Bean : test1Beans) {  
                    System.out.println(test1Bean.getName());  
                    System.out.println(test1Bean.getPassword());  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
        	AbstractConnectionUtil.BackPreparedStatement(conn,null,null); 
        }  
    }  */

}
