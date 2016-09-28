package com.sql.pool.manager;

import java.sql.Connection;

import com.sql.data.connect.pool.IConnectionPool;
/**
 * 模拟线程启动，去获得连接
 * @author Ran
 *
 */
public class ThreadConnection implements Runnable{
	private IConnectionPool pool;
	@Override
	public void run() {
		System.out.println("ThreadConnection run");
		pool = ConnectionPoolManager.getInstance().getPool("testPool");
	}
	
	public Connection getConnection(){
		System.out.println("ThreadConnection getConnection");
		Connection conn = null;
		if(pool != null && pool.isActive()){
			conn = pool.getConnection();
		}
		return conn;
	}
	
	public Connection getCurrentConnection(){
		System.out.println("ThreadConnection getCurrentConnection");
		Connection conn = null;
		if(pool != null && pool.isActive()){
			conn = pool.getCurrentConnecton();
		}
		return conn;
	}
}

