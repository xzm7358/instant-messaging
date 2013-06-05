package com.sky.qq.biz;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.google.gson.JsonObject;
import com.sky.qq.annotation.Service;
import com.sky.qq.entity.EOwnState;
import com.sky.qq.socket.PriorityWriter;

@Service
public class OwnStateBiz {
	
	/**
	 * 发送状态
	 * @param key
	 * @param state
	 */
	public void sendState(SelectionKey key,EOwnState state){
		JsonObject root=new JsonObject();
		root.addProperty("type", "ownState");
		JsonObject playload=new JsonObject();
		root.add("playload", playload);
		playload.addProperty("state", EOwnState.getIndex(state));
		try {
			PriorityWriter.write(key, root.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SocketChannel socketChannel=(SocketChannel) key.channel();
		try {
			socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
