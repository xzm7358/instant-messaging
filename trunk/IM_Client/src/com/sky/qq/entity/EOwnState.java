package com.sky.qq.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public enum EOwnState {
	LOCK,		//锁定
	KICKOFF,	//强制下线
	OFFSITE;	//异地登陆
	
	private static Map<EOwnState,Integer> indexs=new HashMap<EOwnState, Integer>();
	private static Map<Integer,EOwnState> enums=new HashMap<Integer, EOwnState>();
	
	static{
		Field[] fields=EOwnState.class.getFields();
		for(int i=0,j=fields.length;i<j;i++){
			try {
				EOwnState es=(EOwnState)fields[i].get(null);
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
	public static int getIndex(EOwnState eState){
		return indexs.get(eState);
	}
	
	/**
	 * 根据索引获取枚举
	 * @param i
	 * @return
	 */
	public static EOwnState getState(int i){
		return enums.get(i);
	}
}
