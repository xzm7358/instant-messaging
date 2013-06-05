package com.sky.qq.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
/**
 * 自定义类加载器
 * @author sky
 *
 */
public final class ClasspathLoader {
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
		URL url = ClasspathLoader.class.getProtectionDomain().getCodeSource().getLocation();
		String pathUrl=url.getFile();
		String path=System.getProperty("user.dir");
		if(pathUrl.contains(".jar")){
			programPath=pathUrl;
			path=pathUrl.substring(0,pathUrl.lastIndexOf("/"));
			if(path.indexOf("/")==0){
				path=path.substring(1);
			}
		}else{
			InfuseUtil.setIsJar(false);
		}
		NException.setLogPath(path);
		return path;
	}
	
	public static void loadJars(){
		loadDefaultPathJars();
		loadExpandJars();
	}
	
	/**
	 * 加载默认路径下的jar
	 * @throws NoSuchMethodException 
	 * @throws Exception 
	 */
	private static void loadDefaultPathJars(){
		System.out.println(programDirectory);
		URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		try {
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			loadJars(new File(programDirectory+"/lib"),method,classloader);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 加载外部jar
	 */
	private static void loadExpandJars(){
		
	}
	
	/**
	 * 加载路径上的jar
	 * @param file
	 * @param method
	 * @param classLoader
	 * @throws Exception
	 */
	private static void loadJars(File file,Method method,URLClassLoader classLoader) throws Exception{
		if(file.isDirectory()){
			for(File everyFile : file.listFiles()){
				loadJars(everyFile,method,classLoader);
			}
		}else{
			if(file.getAbsolutePath().endsWith(".jar")){
				method.invoke(classLoader, file.toURI().toURL());
			}
		}
	}
	
}
