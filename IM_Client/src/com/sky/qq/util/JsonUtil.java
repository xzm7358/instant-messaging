package com.sky.qq.util;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sky.qq.biz.IJsonConvertSerialize;

public class JsonUtil {
	private static JsonParser parser=new JsonParser();
	private static Gson json=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	
	/**
	 * 讲对象转成Json字符串
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj){
		return json.toJson(obj);
	}
	
	/**
	 * 将包含枚举的对象转换为Json
	 * @param clz
	 * @param serializer
	 * @param data
	 * @return
	 */
	public static String toJson(IJsonConvertSerialize<?> serializer,Object data){
	     return getGsonByConvert(serializer).toJson(data);  
	}
	
	/**
	 * 通过转换器获得Gson
	 * @return
	 */
	private static Gson getGsonByConvert(IJsonConvertSerialize<?> serializer){
		GsonBuilder gsonBuilder = new GsonBuilder();  
	    gsonBuilder.registerTypeAdapter(serializer.getType(),serializer);  
	    return gsonBuilder.create(); 
	}
	/**
	 * 将字符串转换为JsonObject
	 * @param data
	 * @return
	 */
	public static JsonObject convertToJson(String data){
		return (JsonObject) parser.parse(data);
	}
	
	/**
	 * 将Json字符串转成对象
	 * @param data
	 * @param clz
	 * @return
	 */
	public static <T> T convertToObject(Class<T> clz,String data){
		return new Gson().fromJson(data, clz);
	}
	
	/**
	 * 将Json字符串转成ArrayList
	 * @param type
	 * @param data
	 * @return
	 */
	public static <T> ArrayList<T> convertToObject(Type type,String data){
		return new Gson().fromJson(data, type);
	}
	
	/**
	 * 将包含枚举的字符串转成对象
	 * @param clz
	 * @param serializer
	 * @param data
	 * @return
	 */
	public static <T> T convertToObject(IJsonConvertSerialize<?> serializer,Class<T> clz,String data){
	     return getGsonByConvert(serializer).fromJson(data, clz);
	}
	
	/**
	 * 将包含枚举的字符串转成ArrayList
	 * @param serializer
	 * @param type
	 * @param data
	 * @return
	 */
	public static <T> ArrayList<T> convertToObject(IJsonConvertSerialize<?> serializer,Type type,String data){
	     return getGsonByConvert(serializer).fromJson(data, type);
	} 
	
	public static void main(String[] args) {
		//System.out.println(new TypeToken<List<Integer>>().getType().getClass());
		System.out.println(new TypeToken<ArrayList<Integer>>(){}.getType().getClass());
		//new TypeToken().getType();
	}
	
}
