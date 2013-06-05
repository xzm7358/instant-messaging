package com.sky.qq.handler;

import java.nio.channels.SelectionKey;
import com.google.gson.JsonObject;
import com.sky.qq.annotation.Service;
import com.sky.qq.entity.DataBox;

/**
 * 文件传输处理器
 * @author Administrator
 *
 */
@Service
public class FileTransfersHandler extends BaseHandler implements IHandler {

	public FileTransfersHandler() {
		super("fileTrans");
	}

	@Override
	public void run(JsonObject obj,SelectionKey key) {
		/**
		 * 
		 *   {"type:":"fileTrans","playload":{"type":"1","target":"目标","fileName":"文件名","size":"文件长度","md5":"M5D码"}}
  {"type:":"fileTrans","playload":{"type":"2","target":"目标","isAgree":"yes | no 是否同意接受","position":"文件发送偏移量","md5":"M5D码"}}
  {"type:":"fileTrans","playload":{"type":"3","target":"目标","port":"端口号","md5":"MD5码"}}
		 
		JsonObject playload=obj.getAsJsonObject("playload");
		//{"type":"qqMes","playload":{"sender":"XXX","target":"XXX","time":"XXX","mes":"XX"}}
		System.out.println(obj.toString());
		String sender=DataBox.getQQ(key);
		SelectionKey target=DataBox.getKey(playload.get("target").getAsString());
		playload.remove("target");
		playload.addProperty("sender", sender);
		playload.addProperty("time", DateUTil.format(new Date()));
		if(target!=null){
			DataBox.addMes(target, obj);
			target.interestOps(SelectionKey.OP_WRITE);
			key.selector().wakeup();
		}else{
			biz.saveMessage(obj);
		}
		*/
		//{"type:":"fileTrans","playload":{"type":"3","target":"目标","port":"端口号","md5":"MD5码","host":"IP地址"}}
		JsonObject playload=obj.getAsJsonObject("playload");
		String sender=DataBox.getQQ(key);
		SelectionKey target=DataBox.getKey(playload.get("target").getAsString());
		playload.remove("target");
		playload.addProperty("sender", sender);
		if(target!=null){
			DataBox.addMes(target, obj);
			target.interestOps(SelectionKey.OP_WRITE);
			key.selector().wakeup();
		}
	}


}
