package com.sky.qq.handler;

import java.nio.channels.SelectionKey;
import com.google.gson.JsonObject;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.FriendListBiz;
import com.sky.qq.entity.DataBox;

/**
 * 好友分组处理器
 * @author Administrator
 *
 */
@Service
public class SearchFriendHandler extends BaseHandler implements IHandler {

	@Autowired
	private FriendListBiz friendListBiz;
	
	public SearchFriendHandler() {
		super("searchFriend");
	}

	@Override
	public void run(JsonObject obj,SelectionKey key) {
		JsonObject playload=obj.get("playload").getAsJsonObject();
		int qq=Integer.parseInt(DataBox.getQQ(key));
		int pageIndex=playload.get("pageIndex").getAsInt();
		obj.add("playload", friendListBiz.searchQQ(qq, pageIndex));
		DataBox.addMes(key, obj);
		key.interestOps(SelectionKey.OP_WRITE);
		key.selector().wakeup();
	}

}
