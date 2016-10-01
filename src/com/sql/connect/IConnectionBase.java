package com.sql.connect;

import java.sql.Connection;  
import java.sql.SQLException;  
  
public interface IConnectionBase {  

    /**
     * @Description:获得连接  
     * @auther: wutongpeng 2016年9月27日 
     * @return: Connection
     */
    Connection  getConnection(int databaseId)throws Exception;  
     
}  
