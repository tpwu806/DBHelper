package com.sql.data.connect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @Description: 数据源信息
 * @author wutp 2016年9月25日
 * @version 1.0
 */
public class C3p0ConnectionManager {
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.0.121:3306/test", "root", "123456");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static ComboPooledDataSource dataSource;  
	
	static {  
	    try {  
	        dataSource = new ComboPooledDataSource();  
	        dataSource.setUser("root");  
	        dataSource.setPassword("123456");  
	        dataSource.setJdbcUrl("jdbc:mysql://192.168.0.121:3306/test");  
	        dataSource.setDriverClass("com.mysql.jdbc.Driver");  
	        dataSource.setInitialPoolSize(10);  
	        dataSource.setMinPoolSize(5);  
	        dataSource.setMaxPoolSize(50);  
	        dataSource.setMaxStatements(100);  
	        dataSource.setMaxIdleTime(60);  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	} 
	
	/** 
	 * 从连接池中获取数据源链接 
	 *  
	 * @author gaoxianglong 
	 *  
	 * @return Connection 数据源链接 
	 */  
	public static Connection getConnection(int databaseId) {  
	    Connection conn = null;  
	    if (null != dataSource) {  
	        try {  
	            conn = dataSource.getConnection();  
	        } catch (SQLException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    return conn;  
	}  
}