package com.main.pool;

import java.sql.Connection;

import com.sql.connect.ConnectionPoolManagerSingleton;
import com.sql.connect.IConnectionPool;
/**
 * 模拟线程启动，去获得连接
 * @author Ran
 *
 */
public class ThreadConnection implements Runnable{
	private String poolName ;
	private IConnectionPool pool;
	public  ThreadConnection(String poolName){
		this.poolName = poolName;
	}
	@Override
	public void run() {
		//System.out.println("ThreadConnection run");
		pool = ConnectionPoolManagerSingleton.getInstance().getPool(poolName);
	}
	
	public Connection getConnection(){
		//System.out.println("ThreadConnection getConnection");
		Connection conn = null;
		if(pool != null && pool.isActive()){
			conn = pool.getConnection();
		}
		return conn;
	}
	
	public Connection getCurrentConnection(){
		//System.out.println("ThreadConnection getCurrentConnection");
		Connection conn = null;
		if(pool != null && pool.isActive()){
			conn = pool.getCurrentConnecton();
		}
		return conn;
	}
}

