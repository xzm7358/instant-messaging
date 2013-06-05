package com.sky.qq.handler;

import java.nio.channels.SelectionKey;
import com.google.gson.JsonObject;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.FriendGroupBiz;
import com.sky.qq.entity.DataBox;

/**
 * 好友分组处理器
 * @author Administrator
 *
 */
@Service
public class FriendGroupHandler extends BaseHandler implements IHandler {

	@Autowired
	private FriendGroupBiz friendGroupBiz;
	
	public FriendGroupHandler() {
		super("friendGroup");
	}

	@Override
	public void run(JsonObject obj,SelectionKey key) {
//	  request:{"type":"friendGroup","playload":{"type":"group | friend","groupId":XXX}}
//	  response:{"type":"friendGroup","playload":{"type":"group",groups:[{xxx},{xxxx]}}
//	  response:{"type":"friendGroup","playload":{"type":"friend",friends:[{xxx,state:"xxx "}]}}
		JsonObject playload=obj.get("playload").getAsJsonObject();
		int qq=Integer.parseInt(DataBox.getQQ(key));
		if(playload.get("type").getAsString().equals("group")){
			playload.add("groups", friendGroupBiz.getFriendGroups(qq));
		}else{
			playload.add("friends", friendGroupBiz.getQQFriendsByQQ(qq, playload.get("groupId").getAsInt()));
		}
		DataBox.addMes(key, obj);
		key.interestOps(SelectionKey.OP_WRITE);
		key.selector().wakeup();
	}

}
