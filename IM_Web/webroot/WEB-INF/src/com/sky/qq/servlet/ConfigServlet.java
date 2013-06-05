package com.sky.qq.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.sky.qq.socket.ProxyServer;

public class ConfigServlet extends HttpServlet{
	
	@Override 
	public void init() throws ServletException {
		super.init();
		try {
			ProxyServer.start();
			System.out.println("代理服务器已启动");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
