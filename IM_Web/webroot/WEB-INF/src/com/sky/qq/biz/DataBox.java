package com.sky.qq.biz;

import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 消息中心 
 * @author Administrator
 *
 */
public class DataBox {
	//input data
	private static Map<SelectionKey, ConcurrentLinkedQueue<String>> inputData=new HashMap<SelectionKey, ConcurrentLinkedQueue<String>>();
	private static Map<SelectionKey, ConcurrentLinkedQueue<String>> outputData=new HashMap<SelectionKey, ConcurrentLinkedQueue<String>>();
	
	public static void initData(SelectionKey selectionKey){
		inputData.put(selectionKey, new ConcurrentLinkedQueue<String>());
		outputData.put(selectionKey, new ConcurrentLinkedQueue<String>());
	}
	
	public static void setOutputDataValue(SelectionKey key,String value){
		outputData.get(key).add(value);
	}
	
	public static void setInputDataValue(SelectionKey key,String value){
		inputData.get(key).add(value);
	}
	
	public static ConcurrentLinkedQueue<String> getInputDataValue(SelectionKey key){
		return inputData.get(key);
	}
}
