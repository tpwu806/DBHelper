package com.sql.common.utils.service.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sql.connect.base.AbstractConnectionManager;
import com.sql.connect.pool.IConnectionPool;


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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			AbstractConnectionManager.BackPreparedStatement(conn,ps, rs);
		}
		return rs;
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
			if(rs.next())
				sum=rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			AbstractConnectionManager.BackPreparedStatement(conn,ps, rs);
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
			if(rs.next())
				max=rs.getString(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			AbstractConnectionManager.BackPreparedStatement(conn,ps, rs);
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
	public boolean executeQuery(String sql,String []params) throws SQLException
	{		
		boolean confInfo = false;
		try {
			ps=conn.prepareStatement(sql);			
			for(int i=0;i<params.length;i++){
				System.out.println("参数为：" + params[i]);
 				ps.setString(i+1, params[i]);
 			}
			rs=ps.executeQuery();
			if(rs.next() && rs.getInt(1) >= 1)
				confInfo = true;			 					
		} catch (Exception e) {
			e.printStackTrace();
			throw e;		
		} finally {
			AbstractConnectionManager.BackPreparedStatement(conn,ps, rs);		
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
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			AbstractConnectionManager.BackPreparedStatement(conn,ps, null);
		}
		return confInfo;		
	}
	
	public void BackPreparedStatement(Connection conn,PreparedStatement stmt,
			ResultSet rs)throws SQLException {	
		try {
			if(stmt == null && rs == null)
				AbstractConnectionManager.BackPreparedStatement(conn);
			else if(rs == null)
				AbstractConnectionManager.BackPreparedStatement(conn,stmt);
			else
				AbstractConnectionManager.BackPreparedStatement(conn,stmt,rs);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
