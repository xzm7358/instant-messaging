package com.sky.qq.util;

import java.net.URL;

public final class Path {
	//项目目录
	private static String programPath;
	//项目路径
	private static String programDirectory=programDirectory();
	
	public static String getProgramDirectory() {
		return programDirectory;
	}

	public static String getProgramPath(){
		return programPath;
	}
	/**
	 * 获取项目加载目录
	 * @return
	 */
	public static String programDirectory(){
		URL url = Path.class.getProtectionDomain().getCodeSource().getLocation();
		String pathUrl=url.getFile();
		String path=System.getProperty("user.dir");
		if(pathUrl.contains(".jar")){
			programPath=pathUrl;
			path=pathUrl.substring(0,pathUrl.lastIndexOf("/"));
			if(path.indexOf("/")==0){
				path=path.substring(1);
			}
		}
		return path;
	}
	
	public static void main(String[] args) {
		System.out.println(programDirectory);
	}
}
