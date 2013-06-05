package com.sky.qq.handler;

import com.google.gson.JsonObject;

/**
 * 根据报文类型进行处理
 * @author Administrator
 *
 */
public interface IHandler {
	void run(JsonObject obj);
	
	String getType();
}
