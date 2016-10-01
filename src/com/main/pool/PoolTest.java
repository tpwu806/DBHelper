package com.main.pool;

import com.sql.connect.IConnectionPool;
import com.sql.connect.manager.ConnectionPoolManager;
import com.sql.connect.manager.ConnectionPoolManagerSingleton;

/**
 * @Description:
 * @author wutp 2016年10月1日
 * @version 1.0
 */
public class PoolTest {
	private static ConnectionPoolManagerSingleton poolManager;	

	public static void main(String[] args) throws InterruptedException {
		Test1();
	}

	/**
	 * @Description:
	 * @auther: wutp 2016年10月1日
	 * @throws InterruptedException
	 * @return void
	 */
	private static void Test1() throws InterruptedException {
		// 初始化连接池
		Thread t = init();
		t.start();
		t.join();

		ThreadConnection a = new ThreadConnection("pcPool");
		ThreadConnection b = new ThreadConnection("pcPool");
		ThreadConnection c = new ThreadConnection("pcPool");
		ThreadConnection d = new ThreadConnection("padPool");
		Thread t1 = new Thread(a);
		Thread t2 = new Thread(b);
		Thread t3 = new Thread(c);
		Thread t4 = new Thread(d);

		// 设置优先级，先让初始化执行，模拟 线程池 先启动
		// 这里仅仅表面控制了，因为即使t 线程先启动，也不能保证pool 初始化完成，为了简单模拟，这里先这样写了
		t1.setPriority(10);
		t2.setPriority(10);
		t3.setPriority(10);
		t4.setPriority(10);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		

		System.out.println("线程A-> " + a.getConnection());
		System.out.println("线程B-> " + b.getConnection());
		System.out.println("线程C-> " + c.getConnection());
		System.out.println("线程D-> " + d.getConnection());
		
		/*ThreadCopyDataBaseTheTable copy = new ThreadCopyDataBaseTheTable();
		Thread tcopy = new Thread(copy);
		tcopy.setPriority(10);
		tcopy.start();*/
		
	}	

	// 初始化
	public static Thread init() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("init pool!");
				poolManager = initPool();
				
				while (poolManager == null || !poolManager.getPool("pcPool").isActive()) {
					poolManager = initPool();
				}
				
			}
		});
		return t;
	}

	public static ConnectionPoolManagerSingleton initPool() {
		// System.out.println("PoolTest initPool");
		return ConnectionPoolManagerSingleton.getInstance();
	}

}
