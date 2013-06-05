package com.sky.qq.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 异常处理工具类
 * @author Administrator
 *
 */
public class NException {
	private static final String lock="lockException";
	private static String proPath;
	
	public static void setLogPath(String path){
		NException.proPath=path;
	}
	/**
	 * 异常回写进磁盘
	 * @param e
	 */
	public static void writeToDriver(Throwable e){
		synchronized (NException.lock) {
			e.printStackTrace();
			StringWriter stringWriter = new StringWriter();
	        PrintWriter writer = new PrintWriter(stringWriter);
	        e.printStackTrace(writer);
	        BufferedWriter bWriter=null;
			try {
				bWriter = new BufferedWriter(new FileWriter(new File(proPath+"/logs/exception.log"),true));
				bWriter.write("错误时间点:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\r\n");
		        bWriter.write(stringWriter.toString()+"\r\n");
		        bWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 若异常文件存则删除
	 *
	 */
	public static void remove(){
		String filePath=proPath+"/logs/discover";
		File pathFile=new File(filePath);
		if(!pathFile.exists()){
			pathFile.mkdirs();
		}
		File exceptionFile=new File(filePath+"/exception.log");
		if(exceptionFile.exists()){
			exceptionFile.delete();
		}
	}
	
	/**
	 * 控制台重定向
	 */
	public static void consoleFording(String path){
		if(InfuseUtil.getIsJar()){
			PrintStream out;
			try {
				File file=new File(proPath+"/"+path+"/output.txt");
				if(file.exists()){
					file.delete();
				}else{
					file.getParentFile().mkdirs();
				}
				out = new PrintStream(new BufferedOutputStream(new FileOutputStream(file.getPath(),true)),true);
				//System.setOut(out); 
				System.setErr(out); 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
}
