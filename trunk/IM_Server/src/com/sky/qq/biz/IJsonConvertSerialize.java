package com.sky.qq.biz;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

/**
 * 枚举序列化接口
 * @author Administrator
 *
 * @param <T>
 */
public interface IJsonConvertSerialize<T> {
	
	T deserialize(JsonElement json, Type type,JsonDeserializationContext context) throws JsonParseException ;
		
	JsonElement serialize(T state, Type arg1,JsonSerializationContext arg2);
	
	Class<T> getType();
		
}
