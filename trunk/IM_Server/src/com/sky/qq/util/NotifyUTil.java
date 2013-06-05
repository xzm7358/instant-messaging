package com.sky.qq.util;

import javax.swing.JLabel;

import com.sky.qq.ui.Main;

public class NotifyUTil {
	private static final String LOCK_UI="lockUI";
	private static final String LOCK_QQ_NUM="lockQQNum";
	
	/**
	 * Main UI显示
	 * @param mes
	 */
	public static void showMes(String mes){
		synchronized (LOCK_UI) {
			Main.getTxtShowInfor().append(mes+"\r\n");
		}
	}
	
	/**
	 * 在线人数显示
	 * @param labOnline
	 * @param num
	 */
	public static void showQQOnline(JLabel labOnline,int num){
		synchronized (LOCK_QQ_NUM) {
			labOnline.setText(num+"");
		}
	}
}
