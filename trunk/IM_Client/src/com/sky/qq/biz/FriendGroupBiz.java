package com.sky.qq.biz;

import com.google.gson.JsonObject;
import com.sky.qq.socket.Server;

/**
 * 好友分组业务处理
 * @author Administrator
 *
 */
public class FriendGroupBiz {

	/**
	 * 获取好友分组
	 */
	public void getGroup(){
		JsonObject root=createRoot();
		JsonObject playload=root.get("playload").getAsJsonObject();
		playload.addProperty("type", "group");
		try {
			Server.write(root.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过组ID获得好友
	 */
	public void getFriendByGroupId(int groupId){
		JsonObject root=createRoot();
		JsonObject playload=root.get("playload").getAsJsonObject();
		playload.addProperty("type", "friend");
		playload.addProperty("groupId", groupId);
		try {
			Server.write(root.toString().getBytes("UTF-8"));
 		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JsonObject createRoot(){
		JsonObject root=new JsonObject();
		root.addProperty("type", "friendGroup");
		JsonObject playload=new JsonObject();
		root.add("playload", playload);
		return root;
	}
}
