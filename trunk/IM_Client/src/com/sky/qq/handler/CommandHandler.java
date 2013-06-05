package com.sky.qq.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.google.gson.JsonObject;
import com.sky.qq.biz.DataPool;

/**
 * 命令处理器
 * @author Administrator
 *
 */
public class CommandHandler {
	private static ThreadPoolExecutor threadPool=new ThreadPoolExecutor(5, 10, 6000, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));
	
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
	public static void execute(final JsonObject source){
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				String type=source.get("type").getAsString();
				System.out.println("handlerType:"+type);
				IHandler hanlder=handlers.get(type);
				if(hanlder!=null){
					hanlder.run(source);
				}else{
					System.out.println("一条消息进入任务池");
					DataPool.addData(source);
				}
			}
		});
	}
}
