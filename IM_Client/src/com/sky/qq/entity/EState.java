package com.sky.qq.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端状态枚举
 * @author Administrator
 *
 */
public enum EState {
	ONLINE,			//在线
	OFFLINE,	    //离线
	LEAVE,			//离开
	BUSY,			//忙碌
	STEALTH;		//隐身
	
	private static Map<EState,Integer> indexs=new HashMap<EState, Integer>();
	private static Map<Integer,EState> enums=new HashMap<Integer, EState>();
	
	static{
		Field[] fields=EState.class.getFields();
		for(int i=0,j=fields.length;i<j;i++){
			try {
				EState es=(EState)fields[i].get(null);
				indexs.put(es, i);
				enums.put(i, es);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	/**
	 * 根据枚举获取索引值
	 * @param eState
	 * @return
	 */
	public static int getIndex(EState eState){
		return indexs.get(eState);
	}
	
	/**
	 * 根据索引获取枚举
	 * @param i
	 * @return
	 */
	public static EState getState(int i){
		return enums.get(i);
	}
	
}
