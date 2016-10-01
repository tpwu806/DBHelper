package com.sql.common.utils;

/**
 * @Description: 工具类
 * @author wutp 2016年9月25日
 * @version 1.0
 */
public class StringTools {
	
	private StringTools(){	
	}
	
	public static boolean checkNull(String arg1){
		boolean result = false;
		if(arg1!=null && !arg1.isEmpty())
			result = true;
		return result;
	}
	
	public static boolean checkNull(String arg1,String arg2){
		boolean result = false;
		if(arg1!=null && !arg1.isEmpty() && arg2!=null && !arg2.isEmpty())
			result = true;
		return result;
	}
	
	public static boolean checkNull(String arg1,String arg2,
			String arg3){
		boolean result = false;
		if(arg1!=null && !arg1.isEmpty() && arg2!=null && !arg2.isEmpty() &&
				arg3!=null && !arg3.isEmpty())
			result = true;
		return result;
	}
	
	public static boolean checkNull(String arg1,String arg2,
			String arg3,String arg4){
		boolean result = false;
		if(arg1!=null && !arg1.isEmpty() && arg2!=null && !arg2.isEmpty() &&
				arg3!=null && !arg3.isEmpty() && arg4!= null && !arg4.isEmpty())
			result = true;
		return result;
	}
}
