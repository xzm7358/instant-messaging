package com.sky.qq.handler;

import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonObject;

/**
 * 命令处理器
 * @author Administrator
 *
 */
public class CommandHandler {
	//所有的handler
	public static Map<String,IHandler> handlers=new HashMap<String, IHandler>();
	
	/**
	 * 注册Handler
	 * @param handler
	 */
	public static void regsiterHandler(IHandler handler){
		handlers.put(handler.getType(), handler);
	}
	
	/**
	 * 执行Handler
	 * @param jsonObject
	 */
	public static void execute(JsonObject jsonObject,SelectionKey key){
		System.out.println("accept:"+jsonObject.toString());
		String type=jsonObject.get("type").getAsString();
		if(type.equals("login") || type.equals("register")){
			handlers.get(type).run(jsonObject,key);
		}else if(InterceptorHandler.interceptor(key)){
			handlers.get(type).run(jsonObject,key);
		}
	}

}