package com.main.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

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
		manager = ConnectionPoolManagerSingleton.getInstance();
		pcPool = manager.getPool("pcPool");
		padPool = manager.getPool("padPool");
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
		select();
	}
	
	private void select() throws SQLException{
		Connection conn = null;
		try{
			conn = pcPool.getConnection();
			testQuery4(conn);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			AbstractConnectionUtil.BackPreparedStatement(conn,null,null);
		}
		pcPool.getConnection();
	}
	private void checkequral(){
		
	}
	
	public void testQuery4(Connection conn) throws SQLException {  
        final String SQL = "select * from test where 1 = ?";  
        try {   
            @SuppressWarnings("deprecation")
			List<Map<String, Object>> values = new QueryRunner().query(conn,  
                    SQL, new Object[] { "%1%" }, new MapListHandler());  
            if (null != values) {  
                for (int i = 0; i < values.size(); i++) {  
                    Map<String, Object> map = values.get(i);  
                    System.out.println(map.get("name"));  
                    System.out.println(map.get("password"));  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
        	AbstractConnectionUtil.BackPreparedStatement(conn,null,null); 
        }  
    }  

}
