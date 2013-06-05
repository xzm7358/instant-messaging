package com.sky.qq.biz;

import com.google.gson.JsonObject;
import com.sky.qq.socket.Server;

/**
 * 离线消息业务逻辑类
 * @author Administrator
 *
 */
public class LeaveMessageBiz {
	
	/**
	 * 获取离线消息
	 */
	public void getLeaveMessage(){
		JsonObject root=new JsonObject();
		root.addProperty("type", "leaveMes");
		try {
			Server.write(root.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
