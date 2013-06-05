package com.sky.qq.biz;

import java.io.File;
import com.google.gson.JsonObject;
import com.sky.qq.socket.FileRead;
import com.sky.qq.socket.Server;

/**
 * 文件传输业务逻辑
 * @author Administrator
 *
 */
public class FileTransfersBiz {

//	{type:"fileTrans",playload:{type:"request",action:"send" target:"目标",fileName:"文件名",size:"文件长度",md5:"M5D码"}}
//	{type:"fileTrans",playload:{type:"request",action:"cancel" target:"目标",md5:"M5D码"}}
//	//响应类型
//	{type:"fileTrans",playload:{type:"response",target:"目标",action:"agree",position:"文件发送偏移量",md5:"MD5码",host:"IP",port:端口}}
//	{type:"fileTrans",playload:{type:"response",target:"目标",action:"refuse",md5:"MD5码"}}
	
	
	/**
	 * 发送文件请求
	 */
	public static void sendFile(File file,int target,String md5){
		JsonObject root=createRoot("request");
		JsonObject playload=root.get("playload").getAsJsonObject();
		playload.addProperty("action", "send");
		playload.addProperty("target", target);
		playload.addProperty("fileName", file.getName());
		playload.addProperty("size", file.length());
		playload.addProperty("md5", md5);
		root.add("playload", playload);
		try {
			Server.write(root.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送取消请求
	 */
	public static void sendCancelRequest(int target,String md5){
		JsonObject root=createRoot("request");
		JsonObject playload=root.get("playload").getAsJsonObject();
		playload.addProperty("action", "cancel");
		playload.addProperty("target", target);
		playload.addProperty("md5", md5);
		try {
			Server.write(root.toString().getBytes("UTF-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 发送接收请求
	 */
	public static void sendAcceptRequest(int target,String md5,long position){
		JsonObject root=createRoot("response");
		JsonObject playload=root.get("playload").getAsJsonObject();
		playload.addProperty("target", target);
		playload.addProperty("action", "agree");
		playload.addProperty("position", position);
		playload.addProperty("md5", md5);
		playload.addProperty("host", Server.getIpAddress());
		playload.addProperty("port",FileRead.getPort());
		try {
			Server.write(root.toString().getBytes("UTF-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 发送拒绝请求
	 */
	public static void sendRefuseRequest(int target,String md5){
		JsonObject root=createRoot("response");
		JsonObject playload=root.get("playload").getAsJsonObject();
		playload.addProperty("target", target);
		playload.addProperty("action", "refuse");
		playload.addProperty("md5", md5);
		try {
			Server.write(root.toString().getBytes("UTF-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 创建根节点
	 * @param type
	 * @return
	 */
	public static JsonObject createRoot(String type){
		JsonObject root = new JsonObject();
		JsonObject playload = new JsonObject();
		root.addProperty("type", "fileTrans");
		root.add("playload", playload);
		playload.addProperty("type", type);
		return root;
	}
}
