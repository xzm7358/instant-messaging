package com.sky.qq.ui;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import com.sky.qq.util.ClasspathLoader;
import com.sky.qq.util.InfuseUtil;

/**
 * swing程序最小化至系统托盘
 * @author Administrator
 *
 */
public class Taskbar {
	
	public static void show(){
		final Main main=(Main)InfuseUtil.getBeans(Main.class.getName());
		TrayIcon trayIcon = null;
		if (SystemTray.isSupported()) // 判断系统是否支持系统托盘
		{
			SystemTray tray = SystemTray.getSystemTray(); // 创建系统托盘
			Image image = Toolkit.getDefaultToolkit().getImage(ClasspathLoader.getProgramDirectory()+"/res/images/logo.gif");// 载入图片
			ActionListener listener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					main.showWindow();
				}
			};
			// 创建弹出菜单
			PopupMenu popup = new PopupMenu();
			MenuItem defaultItem = new MenuItem("Show MainWndow");
			defaultItem.addActionListener(listener);
			MenuItem exitItem = new MenuItem("Exit");
			exitItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog(null, "确定退出系统?") == 0) {
						System.exit(0);
					}
				}
			});
			popup.add(defaultItem);
			popup.add(exitItem);
			trayIcon = new TrayIcon(image, "QQServer", popup);// 创建trayIcon
			trayIcon.addActionListener(listener);
			try {
				tray.add(trayIcon);
			} catch (Exception e) {
				e.printStackTrace();
			}
			main.showWindow();
		}
	}
	
}