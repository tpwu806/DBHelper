package com.sql.common.utils4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.sql.connect.AbstractConnectionUtil;
import com.sql.connect.IConnectionPool;

/**
 * @Title: DbHelper.java
 * @Description: 统一查询类 。预编译，通过?赋值方式可以防止漏洞注入方式，保证安全性。
 * @author wutp
 * @version 1.0
 * @time 2016-8-17下午4:37:39
 */
public abstract class ADbServiceBase implements IDbServiceBase{
	private Connection conn=null;
	private PreparedStatement ps=null;
	private ResultSet rs=null;   
	private Statement stmt = null;
	
	public ADbServiceBase()throws Exception{
	
	}	
	/**
	 * @Description: 
	 * @param sql
	 * @return PreparedStatement
	 */
	public PreparedStatement getPreparedStatement(Connection conn,String sql) 
			throws SQLException{
		try {
			ps=conn.prepareStatement(sql);
			System.out.println("执行sql: " + sql + "************************");
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;			
		}finally{
			BackPreparedStatement(conn,ps, null);			
		}
		return ps;
	}

	/**
	 * @Description:
	 * @auther: wutongpeng 2016年9月25日 
	 * @return
	 * @throws SQLException: Statement
	 */
	public Statement getStatement(Connection conn) throws SQLException{
		try {
			stmt=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;			
		}finally{
			BackPreparedStatement(conn,ps, null);			
		}
		return stmt;
	}
	
	/**
	 * @Description:返回表
	 * @param sql
	 * @param params	
	 * @return ResultSet
	 * @throws SQLException 
	 */
	public ResultSet getResultSet(Connection conn,String sql,String []params) 
			throws SQLException{
		try {
			ps=conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)			
				ps.setString(i+1, params[i]);			
			rs=ps.executeQuery();
			System.out.println("执行sql: " + sql + "************************");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			AbstractConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return rs;
	}
	
	// 显示表
	public Vector[] query(Connection conn,String sql, String[] params) 
			throws SQLException{
		// 初始化
		Vector[] data = new Vector[2];
		Vector<String> colums = new Vector<String>();
		Vector<Vector> rows = new Vector<Vector>();
		// Vector[2] = new Vector[2];
		// this.colums.add("员工号");
		// this.colums.add("姓名");
		// this.colums.add("性别");
		// this.colums.add("职位");			
		try {
			ResultSet rs = getResultSet(conn,sql, params);
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				colums.add(rsmd.getColumnName(i + 1));
			}
			while (rs.next()) {
				Vector<String> temp = new Vector<String>();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					temp.add(rs.getString(i + 1));
				}
				rows.add(temp);
				// temp.add(rs.getString(1));
				// temp.add(rs.getString(2));
				// temp.add(rs.getString(3));
				// temp.add(rs.getString(4));
			}
			data[0] = colums;
			data[1] = rows;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			AbstractConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return data;
	}

	/**
	 * @Description:查看有多少记录
	 * @param sql
	 * @return
	 * @return int
	 * @throws SQLException 
	 */
	public int getExecuteCount(String sql,String []params) throws SQLException{
		int sum=0;
		try {
			ps=conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)
				ps.setString(i+1, params[i]);
			rs=ps.executeQuery();
			System.out.println("执行sql: " + sql + "************************");
			if(rs.next())
				sum=rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			AbstractConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return sum;
	}
	
	/**
	 * @Description:获得最大编号
	 * @param sql
	 * @return
	 * @return String
	 * @throws SQLException 
	 */
	public String getMaxCount(String sql,String value) throws SQLException
	{
		String max="0";
		try {
			ps=conn.prepareStatement(sql);			
			rs=ps.executeQuery();
			System.out.println("执行sql: " + sql + "************************");
			if(rs.next())
				max=rs.getString(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			AbstractConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return max;
	}


	/**
	 * @Description:查找是否存指定信息
	 * @param sql
	 * @param params
	 * @return String
	 * @throws SQLException 
	 */
	public boolean executeQuery(Connection conn,String sql,String []params) throws SQLException
	{		
		boolean confInfo = false;
		try {
			ps=conn.prepareStatement(sql);			
			for(int i=0;i<params.length;i++){
				System.out.println("参数为：" + params[i]);
 				ps.setString(i+1, params[i]);
 			}
			rs=ps.executeQuery();
			System.out.println("执行sql: " + sql + "************************");
			if(rs.next() && rs.getInt(1) >= 1)
				confInfo = true;			 					
		} catch (Exception e) {
			e.printStackTrace();
			throw e;		
		} finally {
			AbstractConnectionUtil.BackPreparedStatement(conn,ps, rs);		
		}
		return confInfo;		
	}


	/**
	 * @Description:增删改
	 * @param sql
	 * @param params
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean execute(String sql,String []params) throws SQLException
	{
		boolean confInfo = false;
		try {
			ps=conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)
				ps.setString(i+1, params[i]);
			if(ps.executeUpdate()==1)
				confInfo = true;
			System.out.println("执行sql: " + sql + "************************");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			AbstractConnectionUtil.BackPreparedStatement(conn,ps, null);
		}
		return confInfo;		
	}
	
	public void BackPreparedStatement(Connection conn,PreparedStatement stmt,
			ResultSet rs)throws SQLException {	
		try {
			if(stmt == null && rs == null)
				AbstractConnectionUtil.BackPreparedStatement(conn);
			else if(rs == null)
				AbstractConnectionUtil.BackPreparedStatement(conn,stmt);
			else
				AbstractConnectionUtil.BackPreparedStatement(conn,stmt,rs);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
