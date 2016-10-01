package com.sql.environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sql.common.utils.DBbean;

/**
 * @Description: 获取数据库配置信息
 * @author wutp 2016年9月24日
 * @version 1.0
 */
public final class PropertiesReader {
	private static final String driver_mysql = "com.mysql.jdbc.Driver";
	private static final String driver_oracle = "oracle.jdbc.driver.OracleDriver";
	private static final String driver_sybase = "com.sybase.jdbc2.jdbc.SybDriver";
	private static final String driver_sqlserver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	private static String driver;
	private static String url;
	private static String name;
	private static String pwd;		
	
	private static String db_type;
	private static String url_before;
	private static String ip;
	private static String port;
	private static String DataBaseName;

	/*static {
		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/com/sql/db.properties");
		try {
			if(in != null)
				prop.load(in);
			driver = prop.getProperty("mysql_driver").trim();
			url = prop.getProperty("mysql_url").trim();
			name = prop.getProperty("mysql_user").trim();
			pwd = prop.getProperty("mysql_pwd").trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
    /**
     * @Description: 获取数据库配置信息
     * @param id
     * @throws Exception 
     */
    public static void getDbMessage(int id) throws Exception{
    	Properties prop = new Properties();
    	InputStream in = Object.class.getResourceAsStream("/resources/db.properties");   	
    	try {    		
    		if(in == null)
    			throw new Exception("InputStream 读取失败！");
    		
    		prop.load(in);
    		db_type = prop.getProperty("db_type_" + String.valueOf(id)).trim();
    		url_before = prop.getProperty(db_type + "_url_before").trim();	
    		ip = prop.getProperty("ip_" + String.valueOf(id)).trim();
    		port = prop.getProperty("port_" + String.valueOf(id)).trim();
    		DataBaseName = prop.getProperty("dbname_" + String.valueOf(id)).trim();
    		
    		driver = prop.getProperty(db_type + "_driver").trim();
    		if("sqlserver".equals(db_type))
    			url = url_before + ip + ":" + port +";DatabaseName="+ DataBaseName;
    		else
    			url = url_before + ip + ":" + port +"/"+ DataBaseName;
    		name = prop.getProperty("user_" + String.valueOf(id)).trim();
    		pwd = prop.getProperty("pwd_" + String.valueOf(id)).trim();
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * @Description: 获取数据库配置信息
     * @param id
     * @throws Exception 
     */
    public static DBbean getDBbean(int id) throws Exception{
    	DBbean bean =new DBbean();
    	Properties prop = new Properties();
    	InputStream in = Object.class.getResourceAsStream("/resources/db.properties");   	
    	try {    		
    		if(in == null)
    			throw new Exception("InputStream 读取失败！");
    		
    		prop.load(in);
    		db_type = prop.getProperty("db_type_" + String.valueOf(id)).trim();
    		url_before = prop.getProperty(db_type + "_url_before").trim();	
    		ip = prop.getProperty("ip_" + String.valueOf(id)).trim();
    		port = prop.getProperty("port_" + String.valueOf(id)).trim();
    		DataBaseName = prop.getProperty("dbname_" + String.valueOf(id)).trim();
    		
    		driver = prop.getProperty(db_type + "_driver").trim();
    		if("sqlserver".equals(db_type))
    			url = url_before + ip + ":" + port +";DatabaseName="+ DataBaseName;
    		else
    			url = url_before + ip + ":" + port +"/"+ DataBaseName;
    		name = prop.getProperty("user_" + String.valueOf(id)).trim();
    		pwd = prop.getProperty("pwd_" + String.valueOf(id)).trim();
    		
    		bean.setDriverName(driver);
    		bean.setUrl(url);
    		bean.setUserName(name);
    		bean.setPassword(pwd);
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return bean;
    }
	
	/**  
     * 私有构造方法，不需要创建对象  
     */   
    private PropertiesReader() {   
    }	

	public static String getDriver() {
		return driver;
	}

	public static String getUrl() {
		return url;
	}

	public static String getName() {
		return name;
	}

	public static String getPwd() {
		return pwd;
	}

	/*public static void main(String args[]) {
		System.out.println(getDriver());
		System.out.println(getUrl());
		System.out.println(getName());
		System.out.println(getPwd());
				
	    //File file = new File( MyProperties.class.getResource("db.properties").getPath());  	      
	    //System.out.println( "file path: " + file.getPath()); 
	    //System.out.println(System.getProperty("user.dir")); 
		//System.out.println("the path is : "+MyFilePath.getPath("sql", "db.properties"));
	}
*/
}
