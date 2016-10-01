package com.sql.connect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

import com.sql.common.utils.DBbean;
import com.sql.environment.PoolitInfos;

/**
 * 连接管理类
 * 
 * @author Ran
 * 
 */
public class ConnectionPoolManagerSingleton {

	private static ConnectionPoolManagerSingleton instance;
	// 连接池存放
	public Hashtable<String, IConnectionPool> pools = new Hashtable<String, IConnectionPool>();

	private ConnectionPoolManagerSingleton() {
		 try {
				init();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	}

	// 单例实现
	public static synchronized ConnectionPoolManagerSingleton getInstance() {
		if (instance == null)
			instance = new ConnectionPoolManagerSingleton();
		return instance;
	}

	// 初始化所有的连接池
	public void init() throws Exception {

		PoolitInfos.addBean("pcPool");// 默认创建一份连接池信息
		PoolitInfos.addBean("padPool");// 默认创建一份连接池信息

		List<DBbean> beans = PoolitInfos.getBeans();
		System.out.println("beans.size() :" + beans.size());
		for (int i = 0; i < beans.size(); i++) {
			DBbean bean = beans.get(i);

			ConnectionPool pool;
			try {
				pool = new ConnectionPool(bean);
				if (pool != null) {
					pools.put(bean.getPoolName(), pool);
					System.out.println("Info:Init connection successed ->PoolName:" + bean.getPoolName());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// 获得连接,根据连接池名字 获得连接
	public Connection getConnection(String poolName) {
		Connection conn = null;
		if (pools.size() > 0 && pools.containsKey(poolName)) {
			conn = getPool(poolName).getConnection();
		} else {
			System.out.println("Error:Can't find this connecion pool ->" + poolName);
		}
		return conn;
	}

	// 关闭，回收连接
	public void close(String poolName, Connection conn) {
		IConnectionPool pool = getPool(poolName);
		try {
			if (pool != null) {
				pool.releaseConn(conn);
			}
		} catch (SQLException e) {
			System.out.println("连接池已经销毁");
			e.printStackTrace();
		}
	}

	// 清空连接池
	public void destroy(String poolName) {
		IConnectionPool pool = getPool(poolName);
		if (pool != null) {
			pool.destroy();
		}
	}

	/**
	 * @Description:以连接池名字获得连接池
	 * @auther: wutp 2016年10月1日
	 * @param poolName
	 * @return
	 * @return IConnectionPool
	 */
	public IConnectionPool getPool(String poolName) {

		IConnectionPool pool = null;
		if (pools.size() > 0) {
			pool = pools.get(poolName);
			if (pool != null)
				System.out.println("ConnectionPoolManager getPool :" + poolName);
		}
		return pool;
	}

}
