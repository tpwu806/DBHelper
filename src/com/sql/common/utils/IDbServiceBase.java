package com.sql.common.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Description: 数据库操作基本接口
 * @author wutp 2016年9月25日
 * @version 1.0
 */
public interface IDbServiceBase {
	
	/**
	 * @Description:返回表
	 * @auther: wutongpeng 2016年9月25日 
	 * @param sql
	 * @param params
	 * @return: ResultSet
	 */
	ResultSet getResultSet(Connection conn,String sql,String []params)
			throws SQLException;
	
	/**
	 * @Description:查找是否存指定信息
	 * @auther: wutongpeng 2016年9月25日 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException: boolean
	 */ 
	boolean executeQuery(Connection con,String sql,String []params) 
			throws SQLException;
	/**
	 * @Description:增删改
	 * @auther: wutongpeng 2016年9月25日 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException: boolean
	 */
	boolean execute(String sql,String []params) 
			throws SQLException;
	
	/**
	 * @Description:回滚数据，关闭连接
	 * @auther: wutongpeng 2016年9月25日 
	 * @param stmt
	 * @param rs
	 * @throws SQLException: void
	 */
	void BackPreparedStatement(Connection conn,PreparedStatement stmt,
			ResultSet rs)throws SQLException;
}
