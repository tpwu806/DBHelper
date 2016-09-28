package com.sql.common.utils.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sql.common.utils.service.base.ADbServiceBase;
import com.sql.common.utils.service.base.IDbServiceBase;
import com.sql.connect.base.AbstractConnectionManager;


/**
 * @Title: DbHelper.java
 * @Description: 统一查询类 。预编译，通过?赋值方式可以防止漏洞注入方式，保证安全性。
 * @author wutp
 * @version 1.0
 * @time 2016-8-17下午4:37:39
 */
public class DbService extends ADbServiceBase implements IDbService{    
	
	public DbService()throws Exception{

	}

}
