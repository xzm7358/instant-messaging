package com.sky.qq.ui;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class Program {
	public static void main(String[] args) {
		initGobalFont(new Font("宋体", Font.PLAIN, 12));   
		new Login();
		
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
