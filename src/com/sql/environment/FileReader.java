package com.sql.environment;

import java.io.File;

public class FileReader {
	
	private FileReader(){
		
	}
	/**
	 * 获取项目的相对路径下文件的绝对路径
	 * 
	 * @param parentDir
	 *            目标文件的父目录,例如说,工程的目录下,有lib与bin和conf目录,那么程序运行于lib or
	 *            bin,那么需要的配置文件却是conf里面,则需要找到该配置文件的绝对路径
	 * @param fileName
	 *            文件名
	 * @return一个绝对路径
	 */
	public static String getPath(String parentDir, String fileName) {
		String path = null;
		String userdir = System.getProperty("user.dir");
		String userdirName = new File(userdir).getName();
		
		if (userdirName.equalsIgnoreCase("lib") || userdirName.equalsIgnoreCase("bin")) {
			File newf = new File(userdir);
			File newp = new File(newf.getParent());
			if (fileName.trim().equals("")) {
				path = newp.getPath() + File.separator + parentDir;
			} else {
				path = newp.getPath() + File.separator + parentDir + File.separator + fileName;
			}
		} else {
			if (fileName.trim().equals("")) {
				path = userdir + File.separator + parentDir;
			} else {
				path = userdir + File.separator + parentDir + File.separator + fileName;
			}
		}
		return path;
	}
}
