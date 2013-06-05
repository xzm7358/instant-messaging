package com.sky.qq.biz;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.google.gson.JsonObject;
import com.sky.qq.handler.CommandHandler;

/**
 * 数据池
 * @author Administrator
 *
 */
public class DataPool {
	private static ConcurrentLinkedQueue<JsonObject> dataList=new ConcurrentLinkedQueue<JsonObject>();
	
	/**
	 * 添加数据
	 * @param jsonObject
	 */
	public static void addData(JsonObject jsonObject){
		dataList.add(jsonObject);
	}
	
	/**
	 * 推送数据
	 */
	public static void pushData(){
		if(!dataList.isEmpty()){
			Iterator<JsonObject> datas=dataList.iterator();
			while(datas.hasNext()){
				CommandHandler.execute(datas.next());
				datas.remove();
			}
		}
	}
	
}
