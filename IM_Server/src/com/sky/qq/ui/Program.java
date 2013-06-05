package com.sky.qq.ui;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.sky.qq.biz.ProgramVariable;
import com.sky.qq.entity.Config;
import com.sky.qq.util.ClasspathLoader;
import com.sky.qq.util.ConfigUtil;
import com.sky.qq.util.InfuseUtil;
import com.sky.qq.util.NException;

public class Program {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initGobalFont(new Font("宋体", Font.PLAIN, 12));   
		try {
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 程序开始
	 * @throws Exception 
	 */
	private static void start() throws Exception{
		NException.setLogPath(ClasspathLoader.getProgramDirectory());
		NException.consoleFording("qqLog");
		ClasspathLoader.loadJars();
		ProgramVariable.setConfig(new ConfigUtil().readConfigReturnT(Config.class));
		InfuseUtil.setBaseClassPath(ProgramVariable.getConfig().getClassPath());
		InfuseUtil.scanBeans(ProgramVariable.getConfig().getClassPath());
		Taskbar.show();
	}
	
	/**
	 * 程度结束
	 */
	@SuppressWarnings("unused")
	private static void end(){
		
	}

	/**
	 * 初始化字体
	 * @param font
	 */
	public static void initGobalFont(Font font) {
		FontUIResource fontResource = new FontUIResource(font);
		for(Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(value instanceof FontUIResource) {
				UIManager.put(key, fontResource);
			}
		}
	}
}
