package com.sky.qq.entity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.sky.qq.util.Path;

/**
 * 加载资源文件  
 * @author Administrator
 *
 */
public class Resource {

	//表情数据
	private static byte[][] data=new byte[105][];
	//表情按钮图片
	private static Icon brow=new ImageIcon(Path.getProgramDirectory()+"/resource/system/brow.png");
	//截图按钮图片
	private static Icon screenshot=new ImageIcon(Path.getProgramDirectory()+"/resource/system/cut.png");
	//发送文件按钮图片
	private static Icon sendFile=new ImageIcon(Path.getProgramDirectory()+"/resource/system/sendfile.png");
	//好友分组按钮收缩图片
	private static Icon shrinkage=new ImageIcon(Path.getProgramDirectory()+"/resource/system/shousuo.jpg");
	//好友分组按钮展开图片
	private static Icon spread=new ImageIcon(Path.getProgramDirectory()+"/resource/system/zhankai.jpg");
	/**
	 * 获取表情数据长度
	 * @return
	 */
	public static int getBrowLength(){
		return data.length;
	}
	/**
	 * 获取表情索引对应的字节数据
	 * @param i
	 * @return
	 */
	public static byte[] getBrowData(int i){
		return data[i];
	}
	
	/**
	 * 获取资源文件
	 * @throws Exception 
	 */
	public static void loadResource(String path) throws Exception{
		byte[] buffer=new byte[4096];
		for(int i=0;i<105;i++){
			File file=new File(path+i+".gif");
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			int t=-1;
			FileInputStream fStream=new FileInputStream(file);
			while((t=fStream.read(buffer))!=-1){
				baos.write(buffer, 0, t);
			}
			fStream.close();
			baos.flush();
			baos.close();
			data[i]=baos.toByteArray();
		}
		
	}
	public static byte[][] getData() {
		return data;
	}
	public static Icon getBrow() {
		return brow;
	}
	public static Icon getScreenshot() {
		return screenshot;
	}
	public static Icon getSendFile() {
		return sendFile;
	}
	public static Icon getShrinkage() {
		return shrinkage;
	}
	public static Icon getSpread() {
		return spread;
	}
	
	
}
