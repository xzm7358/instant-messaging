package com.sky.qq.handler;

import java.nio.channels.SelectionKey;
import java.util.Date;
import com.google.gson.JsonObject;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.MessageBiz;
import com.sky.qq.entity.DataBox;
import com.sky.qq.util.DateUTil;

/**
 * QQ好友消息处理器
 * @author Administrator
 *
 */
@Service
public class QQMessageHandler extends BaseHandler implements IHandler {

	@Autowired
	private MessageBiz biz;
	
	public QQMessageHandler() {
		super("qqMes");
	}

	@Override
	public void run(JsonObject obj,SelectionKey key) {
		JsonObject playload=obj.getAsJsonObject("playload");
		//{"type":"qqMes","playload":{"sender":"XXX","target":"XXX","time":"XXX","mes":"XX"}}
		System.out.println(obj.toString());
		String sender=DataBox.getQQ(key);
		Integer targetQQ=Integer.parseInt(playload.get("target").getAsString());
		SelectionKey target=DataBox.getKey(targetQQ+"");
		playload.remove("target");
		playload.addProperty("sender", sender);
		playload.addProperty("time", DateUTil.format(new Date()));
		if(target!=null){
			DataBox.addMes(target, obj);
			target.interestOps(SelectionKey.OP_WRITE);
		}else{
			biz.saveMessage(targetQQ,obj);
			key.interestOps(SelectionKey.OP_READ); 
		}
		key.selector().wakeup();
		
	}

}
