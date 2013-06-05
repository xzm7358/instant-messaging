package com.sky.qq.socket;

import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SelectionKeyFactory {

	private static Map<String, SelectionKey> selectionKeys=new ConcurrentHashMap<String, SelectionKey>();
	
	
	public static Map<String, SelectionKey> getSelectionKeys() {
		return selectionKeys;
	}

	/**
	 * 通过key获取SelectionKey
	 * @param key
	 * @return
	 */
	public static SelectionKey getSelectionKey(String key){
		SelectionKey selectionKey=null;
		while((selectionKey=selectionKeys.get(key))==null){
			synchronized (key) {
				try {
					key.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		selectionKeys.remove(key);
		return selectionKey;
	}
	
	public static void setSelectionKey(String key,SelectionKey value){
		selectionKeys.put(key, value);
	}
}
