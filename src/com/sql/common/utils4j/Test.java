package com.sql.common.utils4j;

import java.io.Serializable;

/**
 * @Description: Test daomain
 * @author wutp 2016年10月1日
 * @version 1.0
 */
public class Test implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String password;
	public Test() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Test(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
