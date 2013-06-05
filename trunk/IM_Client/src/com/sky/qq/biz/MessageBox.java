package com.sky.qq.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.JsonObject;

/**
 * 消息盒子 管理所有的消息
 * 
 * @author Administrator
 * 
 */
public class MessageBox extends Subject {

	// 好友消息盒子
	private static Map<String, List<JsonObject>> qqMessages = new HashMap<String, List<JsonObject>>();
	// 群消息盒子
	private static Map<String, List<JsonObject>> groupMessages = new HashMap<String, List<JsonObject>>();
	
	private static MessageBox messageBox=new MessageBox();
	
	
	/**
	 * 处理消息
	 * @param message
	 */
	public static void execMes(JsonObject message) {
		messageBox.notifyObserver(message);
	}
	
	public static Map<String, List<JsonObject>> getQqMessages() {
		return qqMessages;
	}

	/**
	 * 添加qq键
	 * @param qq
	 */
	public static void addQQKey(String qq){
		qqMessages.put(qq, new ArrayList<JsonObject>());
	}
	
	/**
	 * 添加Group键
	 * @param group
	 */
	public static void addGroupKey(String group){
		groupMessages.put(group, new ArrayList<JsonObject>());
	}
	
	public static Map<String, List<JsonObject>> getGroupMessages() {
		return groupMessages;
	}

	public static MessageBox getInstance(){
		return messageBox;
	}
	
	private MessageBox(){
		
	}
}
