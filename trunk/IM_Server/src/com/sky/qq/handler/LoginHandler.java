package com.sky.qq.handler;

import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.FriendGroupBiz;
import com.sky.qq.biz.MessageBiz;
import com.sky.qq.biz.OwnStateBiz;
import com.sky.qq.biz.QQInforBiz;
import com.sky.qq.biz.StateBiz;
import com.sky.qq.entity.DataBox;
import com.sky.qq.entity.EOwnState;
import com.sky.qq.entity.EState;
import com.sky.qq.entity.QQInfor;

/**
 * 用户登录处理器
 * @author Administrator
 *
 */
@Service
public class LoginHandler extends BaseHandler implements IHandler{

	public LoginHandler() {
		super("login");
	}

	@Autowired
	private QQInforBiz qqInforBiz;
	
	@Autowired
	private StateBiz stateBiz;
	
	@Autowired
	private FriendGroupBiz friendGroupBiz;
	
	@Autowired
	private OwnStateBiz ownStateBiz;
	
	@Autowired
	private MessageBiz messageBiz;
	
	@Override
	public void run(JsonObject source,SelectionKey key) {
		//{"type":"login","playload":{"username":"XXX","password":"XXX"}}
		//{"type":"login","playload":{"login":XXX}}
		JsonObject obj=source.getAsJsonObject("playload");
		QQInfor qqInfor=new QQInfor();
		int userName=obj.get("userName").getAsInt();
		qqInfor.setUserName(userName);
		qqInfor.setPassword(obj.get("password").getAsString());
		boolean result=qqInforBiz.login(qqInfor);
		source.remove("playload");
		JsonObject qqInfo=null;
		JsonObject playload=new JsonObject();
		playload.addProperty("login", result);
		if(result){
			String qqNum=String.valueOf(qqInfor.getUserName());
			if(DataBox.existQQ(qqNum)){
				SelectionKey oldKey=DataBox.getKey(qqNum);
				oldKey.cancel();
				ConcurrentLinkedQueue<JsonElement> jsonQueue=DataBox.getQueue(oldKey);
				//清除前一个QQ信息
				DataBox.removeQQ(qqNum);
				DataBox.setQQ(key, qqNum);
				//发送下线请求
				ownStateBiz.sendState(oldKey, EOwnState.OFFSITE);
				JsonElement element=null;
				while((element=jsonQueue.poll())!=null){
					DataBox.addMes(key, element);
				}
			}else{
				DataBox.setQQ(key, qqNum);
			}
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
		
		if(result){
			sendState(userName,key);
		}
	}
	
	/**
	 * 发送好友状态
	 * @param isSuccess
	 */
	private void sendState(int userName,SelectionKey key){
		Map<String,JsonObject> states=stateBiz.notifyQQState(userName,EState.ONLINE);
		for(String keyS : states.keySet()){
			SelectionKey notifyKey=DataBox.getKey(keyS);
			DataBox.addMes(notifyKey, states.get(keyS));
			notifyKey.interestOps(SelectionKey.OP_WRITE);
		}
		key.selector().wakeup();
	}
	
	
}
