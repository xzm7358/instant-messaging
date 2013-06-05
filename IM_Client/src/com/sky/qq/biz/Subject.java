package com.sky.qq.biz;

import java.util.*;
import com.google.gson.JsonObject;
import com.sky.qq.entity.FormType;

public class Subject {
	//存储打开的好友聊天窗口
	private Map<String,Observer> observers=new HashMap<String,Observer>();
	//存储打开的群聊天窗口
	private Map<String,Observer> groupObserver=new HashMap<String,Observer>();
	
	/**
	 * 添加观察者
	 * @param qqNum
	 * @param ob
	 */
	public void attach(String qqNum,Observer ob){
		if(ob.getFrmType().equals("FRIENDMES")){ 
			observers.put(qqNum, ob);
		}else{
			groupObserver.put(qqNum, ob);
		}
	}
	
	/**
	 * 删除观察者
	 * @param qqNum
	 */
	public void dettach(String qqNum,Observer ob){
		if(ob.getFrmType().equals("FRIENDMES")){
			observers.remove(qqNum);
		}else{
			groupObserver.remove(qqNum);
		}
	} 
	
	/**
	 * 通知观察者
	 * @param message
	 */
	public void notifyObserver(JsonObject message){
		String mesType=message.get("type").getAsString();
		JsonObject playload=message.getAsJsonObject("playload");
		String sender=playload.get("sender").getAsString();
		if(mesType.equals("groupMes")){
			Observer observer=groupObserver.get(sender);
			if(observer!=null){
				observer.update(playload); 
			}else{ 
				//将消息加入到群消息盒子
				MessageBox.getGroupMessages().get(sender).add(playload);
			}
		}else{
			Observer observer=observers.get(sender);
			if(observer!=null){
				observer.update(message);
			}else{
				//将消息加入到好友消息盒子
				MessageBox.getQqMessages().get(sender).add(message);
			}
		}
	}
	
	/**
	 * 判断观察者是否存在
	 * @param qqNum
	 * @return
	 */
	public boolean observerExist(String qqNum,FormType fType){
		boolean result=false;
		if(fType==FormType.friendMes){
			if(observers.containsKey(qqNum)){
				result=true;
			}
		}else{
			if(groupObserver.containsKey(qqNum)){
				result=true;
			}
		}
		return result;
	}
	
}
