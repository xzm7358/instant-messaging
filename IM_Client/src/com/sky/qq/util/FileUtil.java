package com.sky.qq.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 文件工具类
 * @author Administrator
 *
 */
public class FileUtil {
	private static ThreadPoolExecutor threadPool=new ThreadPoolExecutor(5, 10, 6000, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));
	private static Map<String,String> fileMapping=readObject();
	/**
	 * 文件剪切
	 * @param source
	 * @param target
	 */
	public static void cutFile(final String source,final String target){
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				byte[] buffer=new byte[4096];
				try {
					File soureFile=new File(source);
					FileInputStream fr=new FileInputStream(soureFile);
					FileOutputStream fw=new FileOutputStream(target);
					int i=0;
					while((i=fr.read(buffer))!=-1){
						fw.write(buffer,0,i);
					}
					fr.close();
					fw.close();
					soureFile.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	/**
	 * 对象序列化到磁盘
	 */
	public static void saveDataToFile(String key,String value){
		fileMapping.put(key, value);
		saveData();
	}
	
	public static void saveData(){
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(new FileOutputStream(Path.getProgramDirectory()+ "/file/temp/file.db"));
			synchronized (FileUtil.class) {
				os.writeObject(fileMapping);
			}
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 文件反序列化
	 */
	@SuppressWarnings("unchecked")
	private static Map<String,String> readObject(){
		Map<String,String> data=null;
		try {
			File file=new File(Path.getProgramDirectory()+ "/file/temp/file.db");
			if (!file.exists()) {
				File parentFile = file.getParentFile();
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				data=new ConcurrentHashMap<String,String>();
			}else{
				ObjectInputStream os=new ObjectInputStream(new FileInputStream(file));
				data=(ConcurrentHashMap<String,String>)os.readObject();
				os.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return data;
	}
	
	/**
	 * 获取文件长度
	 * @param path
	 * @return
	 */
	public static long getFileLength(String path){
		File file=new File(path);
		return (file.exists())?(file.length()):(0);
	}
	
	public static String getFilePath(String key){
		return fileMapping.get(key);
	}
	
	public static void removeFilePath(String key){
		if(fileMapping.containsKey(key)){
			fileMapping.remove(key);
			saveData();
		}
	}
}
