package com.sql.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sql.common.utils4j.StringTools;
import com.sql.environment.PropertiesReader;

/**
 * @Description: 获得Connection
 * @author wutp 2016年9月22日
 * @version 1.0
 */
public class AbstractConnectionUtil implements IConnectionBase{

	private Connection con=null;	
	
	private String driver;
	private String url;
	private String user;
	private String pwd;	

	public AbstractConnectionUtil()throws Exception{
		
	}
	
	private void getParameter(int id)throws Exception{
		try {
			PropertiesReader.getDbMessage(id);
			driver = PropertiesReader.getDriver();
			url = PropertiesReader.getUrl();
			user = PropertiesReader.getName();
			pwd = PropertiesReader.getPwd();
			if(!StringTools.checkNull(driver,url,user,pwd)){
				throw new Exception("获取数据库配置文件信息失败！");
			}					
		} catch (Exception e) {			
			e.printStackTrace();
			throw e;
		}
	}
	
	private Connection initConnection()throws Exception{
		try {
			Class.forName(driver);			
			con=DriverManager.getConnection(url,user,pwd);
		} catch (Exception e) {			
			e.printStackTrace();
			throw e;
		}
		return con;
	}
	
	@Override
	public Connection getConnection(int databaseId) throws Exception {
		try {
			getParameter(databaseId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return initConnection();
	}
		
	
	public static void BackPreparedStatement(Connection conn) throws SQLException{
		if(conn ==null)
			return;
		try {			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void BackPreparedStatement(Connection conn,PreparedStatement stmt)
			throws SQLException {
		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		if (conn == null)
			return;
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void BackPreparedStatement(Connection conn,PreparedStatement stmt,
			ResultSet rs)throws SQLException {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		if (conn == null)
			return;
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	
}


