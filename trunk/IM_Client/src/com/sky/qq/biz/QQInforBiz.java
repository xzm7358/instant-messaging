package com.sky.qq.biz;

import com.google.gson.JsonObject;
import com.sky.qq.socket.Server;

public class QQInforBiz {
	
	/**
	 * QQ登陆
	 */
	public void login(int userName,String password){
		JsonObject root=new JsonObject();
		root.addProperty("type", "login");
		JsonObject playload=new JsonObject();
		playload.addProperty("userName",userName);
		playload.addProperty("password", password);
		root.add("playload", playload);
		try {
			Server.write(root.toString().getBytes("UTF-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
