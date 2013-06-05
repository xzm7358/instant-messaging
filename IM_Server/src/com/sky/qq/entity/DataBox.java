package com.sky.qq.entity;

import java.nio.channels.SelectionKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.google.gson.JsonElement;
import com.sky.qq.ui.Main;
import com.sky.qq.util.DateUTil;

/**
 * 数据盒
 * @author Administrator
 *
 */
public class DataBox {
	
	private static Map<SelectionKey, String> keyForQQ=new ConcurrentHashMap<SelectionKey, String>();
	
	private static Map<String, SelectionKey> qqForkey=new ConcurrentHashMap<String,SelectionKey>();
	
	//存储在线QQ
	private static Map<String, String> onlineQQ=new ConcurrentHashMap<String, String>();
	
	//存储每个QQ需要发送的消息队列
	private static Map<SelectionKey, ConcurrentLinkedQueue<JsonElement>> sendDataQueue=new ConcurrentHashMap<SelectionKey,ConcurrentLinkedQueue<JsonElement>>();
	
	public static void setQQ(SelectionKey key,String value){
		onlineQQ.put(value, DateUTil.format(new Date()));
		keyForQQ.put(key, value);
		qqForkey.put(value, key);
		Main.labTotal.setText("当前在线人数:"+onlineQQ.size());
	}
	
	/**
	 * 获得在线QQ
	 * @return
	 */
	public static Map<String, String> getOnlineQQ(){
		return onlineQQ;
	}
	/**
	 * QQ下线 删除存储的QQ
	 */
	public static void removeQQ(String qq){
		SelectionKey key=qqForkey.get(qq);
		qqForkey.remove(qq);
		qqForkey.remove(key);
		onlineQQ.remove(qq);
		Main.labTotal.setText("当前在线人数:"+onlineQQ.size());
//		synchronized (qq) {
//			qq.notify();
//		}
	}
	
	/**
	 * QQ下线 删除存储的QQ
	 */
	public static void removeQQ(SelectionKey key){
		String qq=keyForQQ.get(key);
		qqForkey.remove(qq);
		qqForkey.remove(key);
		onlineQQ.remove(qq);
		Main.labTotal.setText("当前在线人数:"+onlineQQ.size());
//		synchronized (key) {
//			key.notify();
//		}
	}
	
	public static SelectionKey getKey(String key){
		return qqForkey.get(key);
	}
	
	public static String getQQ(SelectionKey key){
		return keyForQQ.get(key);
	}
	
	public static ConcurrentLinkedQueue<JsonElement> getQueue(SelectionKey key){
		return sendDataQueue.get(key);
	}
	
	public static void setTempKey(){
		
	}
	
	public static boolean selectionKeyExist(String qq){
		return qqForkey.containsKey(qq);
	}
	/**
	 * 将消息存储到队列中
	 * @param key
	 * @param value
	 */
	public static void addMes(SelectionKey key,JsonElement value){
		ConcurrentLinkedQueue<JsonElement> data=sendDataQueue.get(key);
		if(data==null){
			data=new ConcurrentLinkedQueue<JsonElement>();
			sendDataQueue.put(key, data);
		}
		data.offer(value);
	}
	
	/**
	 * 判断SelectionKey是否存在
	 * @param key
	 * @return
	 */
	public static boolean existSelectionKey(SelectionKey key){
		return keyForQQ.containsKey(key);
	}
	
	/**
	 * 判断QQ是否存在
	 * @param qq
	 * @return
	 */
	public static boolean existQQ(String qq){
		return qqForkey.containsKey(qq);
	}
}
