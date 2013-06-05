package com.sky.qq.handler;

import java.nio.channels.SelectionKey;

import com.sky.qq.entity.DataBox;

/**
 * Handler拦截器
 * @author Administrator
 *
 */
public class InterceptorHandler {
	
	/**
	 * 判断请求是否合法
	 * @param key
	 * @return
	 */
	public static boolean interceptor(SelectionKey key){
		return DataBox.existSelectionKey(key);
	}
}
