package com.sky.qq.handler;

import java.nio.channels.SelectionKey;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.MessageBiz;
import com.sky.qq.entity.DataBox;

/**
 * 离线消息处理器
 * @author Administrator
 *
 */
@Service
public class MessageHandler extends BaseHandler implements IHandler{

	public MessageHandler() {
		super("leaveMes");
	}

	@Autowired
	private MessageBiz messageBiz;
	
	@Override
	public void run(JsonObject source,SelectionKey key) {
		int qq=Integer.parseInt(DataBox.getQQ(key));
		Iterator<JsonElement> allMes=messageBiz.query(qq).iterator();
		while(allMes.hasNext()){
			DataBox.addMes(key, allMes.next());
		}
		key.interestOps(SelectionKey.OP_WRITE);
		key.selector().wakeup();
		messageBiz.delete(qq);
		//{"type":"login","playload":{"username":"XXX","password":"XXX"}}
		//{"type":"login","playload":{"login":XXX}}
		/*JsonObject obj=source.getAsJsonObject("playload");
		QQInfor qqInfor=new QQInfor();
		qqInfor.setUserName(obj.get("userName").getAsString());
		qqInfor.setPassword(obj.get("password").getAsString());
		boolean result=qqInforBiz.login(qqInfor);
		source.remove("playload");
		JsonObject qqInfo=null;
		JsonObject playload=new JsonObject();
		playload.addProperty("login", result);
		if(result){
			DataBox.setQQ(key, qqInfor.getUserName());
			QQInfor qq=qqInforBiz.getQQInforByQQNum(qqInfor);
			qqInfo=new JsonObject();
			qqInfo.addProperty("nickName", qq.getNickName());
			qqInfo.addProperty("faceImg", qq.getFacePic());
			playload.add("qqInfo", qqInfo);
		}
		source.add("playload", playload);
		DataBox.addMes(key, source);
		key.interestOps(SelectionKey.OP_WRITE);
		System.out.println("key hashCode:"+key.hashCode());
		System.out.println("--------------");
		key.selector().wakeup();*/
	}

}
