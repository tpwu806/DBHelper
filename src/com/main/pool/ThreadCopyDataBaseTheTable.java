package com.main.pool;

import java.util.Timer;
import java.util.TimerTask;

import com.sql.connect.IConnectionPool;
import com.sql.connect.manager.ConnectionPoolManagerSingleton;

public class ThreadCopyDataBaseTheTable implements Runnable {
	private ConnectionPoolManagerSingleton manager ;	
	private IConnectionPool pcPool;
	private IConnectionPool padPool;
	private long lazyCheck = 1000*60;// 延迟多少时间后开始 检查
	private long periodCheck = 1000*60;// 检查频率
	public  ThreadCopyDataBaseTheTable(){
		manager = ConnectionPoolManagerSingleton.getInstance();
		pcPool = manager.getPool("pcPool");
		padPool = manager.getPool("padPool");
	}
	@Override
	public void run() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				// 3.其他状态进行检查，因为这里还需要写几个线程管理的类，暂时就不添加了
				System.out.println("************** copy start ***********");
				//System.out.println(DBbean.getPoolName() + "的活动连接数：" + activeConnection.size());
				//System.out.println(DBbean.getPoolName() + "的总的连接数：" + contActive);
				System.out.println("************** copy stop ***********");
			}
		}, lazyCheck, periodCheck);
	}

}
