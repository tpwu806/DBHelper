package com.sql.connect;

import java.sql.Connection;  
import java.sql.SQLException;

import com.sql.connect.base.IConnectionBase;  
  
public interface IConnectionPool extends IConnectionBase{
	
	Connection  getConnection();
    // 获得当前连接  
    Connection getCurrentConnecton();  
    // 回收连接  
    void releaseConn(Connection conn) throws SQLException;  
    // 销毁清空  
    void destroy();  
    // 连接池是活动状态  
    boolean isActive();  
    // 定时器，检查连接池  
    void cheackPool();  
}  
