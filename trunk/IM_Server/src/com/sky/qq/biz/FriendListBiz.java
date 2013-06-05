package com.sky.qq.biz;

import java.util.List;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.dao.FriendListDao;
import com.sky.qq.entity.DataBox;
import com.sky.qq.entity.EState;
import com.sky.qq.entity.QQInfor;
import com.sky.qq.util.JsonUtil;

@Service
public class FriendListBiz {
	
	@Autowired
	private FriendListDao friendListDao;
	private int pageSize=20;
	
	public JsonElement searchQQ(int qq,int page){
		JsonObject playload=new JsonObject();
		List<QQInfor> qqInfors=friendListDao.searchQQ(qq, page);
		for(QQInfor qqInfo : qqInfors){
			qqInfo.setState((DataBox.selectionKeyExist(qqInfo.getUserName()+""))?(EState.ONLINE):(EState.OFFLINE));
		}
		playload.add("friends", JsonUtil.convertToJson(JsonUtil.toJson(new EStateSerializer(),qqInfors)));
		if(page==1){
			playload.addProperty("totalPage", getTotalPage(qq));
		}
		return playload;
	}
	
	/**
	 * 获取所有页数
	 * @param qq
	 * @return
	 */
	public int getTotalPage(int qq){
		int totalRow=friendListDao.getTotalQQ(qq);
		return (totalRow % pageSize == 0)?(totalRow / pageSize):(totalRow % pageSize + 1);
	}
}
