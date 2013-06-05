package com.sky.qq.handler;

import java.nio.channels.SelectionKey;
import com.google.gson.JsonObject;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.FriendGroupBiz;
import com.sky.qq.biz.QQInforBiz;
import com.sky.qq.dao.FriendGroupDao;
import com.sky.qq.entity.DataBox;
import com.sky.qq.entity.QQInfor;

/**
 * 用户注册处理器
 * @author Administrator
 *
 */
@Service
public class RegisterHandler extends BaseHandler implements IHandler {
	
	public RegisterHandler() {
		super("register");
	}

	@Autowired
	private QQInforBiz qqInforBiz;
	
	@Autowired
	private FriendGroupBiz groupBiz;
	
	@Override
	public void run(JsonObject obj,SelectionKey key) {
		//{type:”register”,playload:{username:”xxxx”,password:”xxxxx”}}
		//JsonElement jsObject=obj.get(getType());
		System.out.println("json:"+obj.toString());
		JsonObject playload=obj.getAsJsonObject("playload");
		QQInfor qqInfor=new QQInfor();
		qqInfor.setNickName(playload.get("nickName").getAsString());
		qqInfor.setPassword(playload.get("password").getAsString());
		int qq=qqInforBiz.registerQQ(qqInfor);
		//qq=(Integer.parseInt(qq)-1)+"";
		obj.remove("playload");
		JsonObject send=new JsonObject();
		send.addProperty("success", false);
		if(qq!=0){
			qqInforBiz.initFaceImg(qq);
			groupBiz.addDefaultGroup(qq);
			//request:{"type":"register","playload":{"nickName":"xxx","password":"xxx","gender":"xxx","question":"xxx","answer":"xxx"}}
			//response:{"type":"register","playload":{"success":true,"qqNum":"xxx"}}
			send.addProperty("success", true);
			send.addProperty("qqNum", qq);
		}
		obj.add("playload", send);
		DataBox.addMes(key, obj);
		key.interestOps(SelectionKey.OP_WRITE);
		key.selector().wakeup();
	}

}
