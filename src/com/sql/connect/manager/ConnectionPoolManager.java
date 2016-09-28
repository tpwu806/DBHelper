package com.sql.connect.manager;

import java.sql.Connection;  
import java.sql.SQLException;  
import java.util.Hashtable;

import com.sql.common.bean.DBbean;
import com.sql.connect.ConnectionPool;
import com.sql.connect.IConnectionPool;
import com.sql.environment.PoolitInfos;  
/** 
 * 连接管理类 
 * @author Ran 
 * 
 */  
public class ConnectionPoolManager {  
      
      
    // 连接池存放  
    public Hashtable<String,IConnectionPool> pools = new Hashtable<String, IConnectionPool>();  
      
    // 初始化  
    private ConnectionPoolManager(){  
    	//System.out.println("ConnectionPoolManager ConnectionPoolManager");
        try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }  
    // 单例实现  
    public static ConnectionPoolManager getInstance(){  
    	//System.out.println("ConnectionPoolManager getInstance");
        return Singtonle.instance;  
    }  
    private static class Singtonle {    	
        private static ConnectionPoolManager instance =  new ConnectionPoolManager();  
    }  
      
      
    // 初始化所有的连接池  
    public void init() throws Exception{
    	//System.out.println("ConnectionPoolManager init");
    	PoolitInfos.addBean(1);
    	
        for(int i =0;i<PoolitInfos.getBeans().size();i++){  
            DBbean bean = PoolitInfos.getBeans().get(i);
    	  
            ConnectionPool pool;
			try {
				pool = new ConnectionPool(bean);
				if(pool != null){  
	                pools.put(bean.getPoolName(), pool);  
	                System.out.println("Info:Init connection successed ->" +bean.getPoolName());  
	            } 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
             
        }  
    }  
      
    // 获得连接,根据连接池名字 获得连接  
    public Connection  getConnection(String poolName){  
        Connection conn = null;  
        if(pools.size()>0 && pools.containsKey(poolName)){  
            conn = getPool(poolName).getConnection();  
        }else{  
            System.out.println("Error:Can't find this connecion pool ->"+poolName);  
        }  
        return conn;  
    }  
      
    // 关闭，回收连接  
    public void close(String poolName,Connection conn){  
            IConnectionPool pool = getPool(poolName);  
            try {  
                if(pool != null){  
                    pool.releaseConn(conn);  
                }  
            } catch (SQLException e) {  
                System.out.println("连接池已经销毁");  
                e.printStackTrace();  
            }  
    }  
      
    // 清空连接池  
    public void destroy(String poolName){  
        IConnectionPool pool = getPool(poolName);  
        if(pool != null){  
            pool.destroy();  
        }  
    }  
      
    // 获得连接池  
    public IConnectionPool getPool(String poolName){ 
    	//System.out.println("ConnectionPoolManager getPool");
        IConnectionPool pool = null;  
        if(pools.size() > 0){  
             pool = pools.get(poolName);  
        }  
        return pool;  
    }  
}  
