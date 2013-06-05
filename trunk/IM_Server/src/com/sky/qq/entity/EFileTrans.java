package com.sky.qq.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件传输状态枚举
 * @author Administrator
 *
 */
public enum EFileTrans {
	REQUEST,			//请求
	RESPONSE,			//响应
	TRANSMISSION;		//传输
	
	private static Map<EFileTrans,Integer> indexs=new HashMap<EFileTrans, Integer>();
	private static Map<Integer,EFileTrans> enums=new HashMap<Integer, EFileTrans>();
	
	static{
		Field[] fields=EFileTrans.class.getFields();
		for(int i=0,j=fields.length;i<j;i++){
			try {
				EFileTrans es=(EFileTrans)fields[i].get(null);
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
	public static EFileTrans getState(int i){
		return enums.get(i);
	}
}
