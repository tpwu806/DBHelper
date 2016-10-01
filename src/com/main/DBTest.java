package com.main;

import java.sql.Connection;

import com.sql.common.utils.DbService;
import com.sql.common.utils.IDbServiceBase;
import com.sql.connect.AbstractConnectionUtil;

public class DBTest {
	
	public static void main(String[] args) {
		String sql = "";
		String[] params = null;
		IDbServiceBase dbService = null;
		AbstractConnectionUtil coutil = null;
		Connection con = null;
		try {
			coutil = new AbstractConnectionUtil();
			con = coutil.getConnection(1);
			dbService = new DbService();			
			sql="SELECT COUNT(*) FROM test WHERE NAME = ? "
					+" AND PASSWORD = ?";
			params = new String[2];
			params[0] = "admin";
			params[1] = "123456";
			
			boolean b = dbService.executeQuery(con,sql,params);
			System.out.println("登录：" + b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
