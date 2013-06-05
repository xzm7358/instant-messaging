package com.sky.qq.handler;

import java.nio.channels.SelectionKey;

import com.google.gson.JsonObject;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.QQInforBiz;
import com.sky.qq.entity.DataBox;
import com.sky.qq.entity.QQInfor;

@Service
public class SearchQQHandler extends BaseHandler implements IHandler{

	public SearchQQHandler() {
		super("searchQQ");
	}

	@Autowired
	private QQInforBiz qqInforBiz;
	
	@Override
	public void run(JsonObject source,SelectionKey key) {
		//{"type":"login","playload":{"username":"XXX","password":"XXX"}}
		//{"type":"login","playload":{"login":XXX}}
		
		/**
		 *  request:{"type":"searchQQ","playload":{"type":"all | one",isOnline:xxx,"qq":"xxx"}}
  response:{"type":"searchQQ","playload":[{"qq":"xxx","nickName":"xxx","faceImg":"xxxx"}]}
		 */
		JsonObject obj=source.getAsJsonObject("playload");
		String type=obj.get("type").getAsString();
		if(type.equals("all")){
			if(obj.get("isOnline").getAsBoolean()){
				
			}else{
				
			}
		}else{
			
		}
		
		
		
		
		
		QQInfor qqInfor=new QQInfor();
		qqInfor.setUserName(obj.get("userName").getAsInt());
		qqInfor.setPassword(obj.get("password").getAsString());
		boolean result=qqInforBiz.login(qqInfor);
		source.remove("playload");
		JsonObject qqInfo=null;
		JsonObject playload=new JsonObject();
		playload.addProperty("login", result);
		if(result){
			
			DataBox.setQQ(key, String.valueOf(qqInfor.getUserName()));
			QQInfor qq=qqInforBiz.getQQInforByQQNum(qqInfor);
			qqInfo=new JsonObject();
			qqInfo.addProperty("nickName", qq.getNickName());
			qqInfo.addProperty("faceImg", qq.getFaceImg());
			playload.add("qqInfo", qqInfo);
		}
		source.add("playload", playload);
		DataBox.addMes(key, source);
		key.interestOps(SelectionKey.OP_WRITE);
		System.out.println("key hashCode:"+key.hashCode());
		System.out.println("--------------");
		key.selector().wakeup();
	}

}
