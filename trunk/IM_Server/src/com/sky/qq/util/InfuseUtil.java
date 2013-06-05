package com.sky.qq.util;

import java.io.File;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;

/**
 * 依赖注入工具类
 * @author sky
 *
 */
public class InfuseUtil {
	private static String baseClassPath;

	//需要扫描的类是否被打包成Jar
	private static boolean isJar=true;
	
	private static Map<String, Object> beans=new HashMap<String, Object>();
	//存放所有扫描的Class类路径
	private static List<String> allClass=new ArrayList<String>();
	
	public static boolean getIsJar(){
		return isJar;
	}
	
	public static Object getBeans(String beanName){
		return beans.get(beanName);
	}
	
	public static void setBaseClassPath(String baseClassPath){
		InfuseUtil.baseClassPath=baseClassPath;
	}
	
	public static void setIsJar(boolean isJar){
		InfuseUtil.isJar=isJar;
	}
	
	protected InfuseUtil(){
		for(Field field :this.getClass().getDeclaredFields()){
			field.setAccessible(true);
			Autowired autowired=field.getAnnotation(Autowired.class);
			if(autowired!=null){
				try {
					field.set(this, beans.get(field.getType().getName()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 扫描bean
	 * @throws Exception 
	 */
	public static void scanBeans(String baseClass) throws Exception{
		if(isJar){ //扫描jar文件中的Class
			scanClassByJar(baseClass);
		}else{  //扫描目录中的Class
			scanClassByDirectory(new File(ClasspathLoader.getProgramDirectory()+"/bin"));
		}
		createObjectByClass();
		infuseBeans();
	}
	
	/**
	 * jar文件中扫描Class
	 * @param baseClassPath
	 * @throws Exception 
	 */
	private static void scanClassByJar(String baseClass) throws Exception{
		String baseClassPath=baseClass.replace(".", "/");
		URL url = InfuseUtil.class.getClassLoader().getResource(baseClassPath);
		JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
		JarFile jarFile = jarURLConnection.getJarFile();
		Enumeration<JarEntry> entrys = jarFile.entries();			
	    while(entrys.hasMoreElements()){
	       JarEntry imagEntry = entrys.nextElement();
	       if(!imagEntry.isDirectory()){
	    	   String fileName=imagEntry.getName();
	    	   int index=fileName.lastIndexOf(".class");
	    	   if(index!=-1 && fileName.contains(baseClassPath)){
	    		   String shortClassName=fileName.substring(fileName.indexOf(baseClassPath)+baseClassPath.length(), index);
	    		   allClass.add(baseClass+shortClassName.replace("/", "."));
	    	   }
	    	   
	       }
	   }
	}
	/**
	 * 目录中扫描Class
	 * @param file
	 * @throws Exception 
	 */
	private static void scanClassByDirectory(File file) throws Exception{
		if(file.isDirectory()){
			for(File everyFile : file.listFiles()){
				scanClassByDirectory(everyFile);
			}
		}else{
			String filePath=file.getPath();
			int i=filePath.lastIndexOf(".class");
			if(i!=-1){
				filePath=filePath.replaceAll("\\\\", "/");
				filePath=filePath.replace("/", ".");
				if(filePath.contains(baseClassPath)){
					String temp=filePath.substring(filePath.indexOf(baseClassPath)+baseClassPath.length(), i);
					allClass.add(baseClassPath+temp);
				}
			}
		}
	}
	
	/**
	 * 跟据ClassName生成对象
	 * @throws Exception 
	 */
	private static void createObjectByClass() throws Exception{
		for(String longClassName : allClass){
			Class<?> clz=Class.forName(longClassName);
		    Service service=clz.getAnnotation(Service.class);
		    if(service!=null){
		    	//String className=(service.value().length()==0)?(clz.getSimpleName().substring(0, 1).toLowerCase()+clz.getSimpleName().substring(1)):(service.value());
		    	beans.put(clz.getName(), clz.newInstance());	
		    }
		}
	}
	
	public static void main(String[] args) {
		Class<?> clz=InfuseUtil.class;
		System.out.println(clz.getName());
	}
	/**
	 * 注入bean
	 * @throws IllegalAccessException 
	 * @throws Exception 
	 */
	public static void infuseBeans() throws Exception{
		for(String key : beans.keySet()){
			Object obj=beans.get(key);
			for(Field field :obj.getClass().getDeclaredFields()){
				Autowired autowired=field.getAnnotation(Autowired.class);
				if(autowired!=null){
					field.setAccessible(true);
					field.set(obj,beans.get(field.getType().getName()));
				}
			}
		}
	}
	
}
