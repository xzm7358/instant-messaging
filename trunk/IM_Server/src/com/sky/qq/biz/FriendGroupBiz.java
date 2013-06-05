package com.sky.qq.biz;

import java.util.List;
import com.google.gson.JsonElement;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.dao.FriendGroupDao;
import com.sky.qq.entity.DataBox;
import com.sky.qq.entity.EState;
import com.sky.qq.entity.QQInfor;
import com.sky.qq.util.JsonUtil;

@Service
public class FriendGroupBiz{
	
	@Autowired
	private FriendGroupDao groupDao;
	
	public boolean addGroup(int qq,String groupName){
		return groupDao.addGroup(qq, groupName);
	}
	
	/**
	 * 获取好友分组
	 * @param qq
	 * @return
	 */
	public JsonElement getFriendGroups(int qq){
		return JsonUtil.convertToJson(JsonUtil.toJson(groupDao.getFriendGroups(qq)));
	}
	
	/**
	 * 获取分组好友
	 * @param qq
	 * @return
	 */
	public JsonElement getQQFriendsByQQ(int qq,int friendGroupId){
		List<QQInfor> qqInfors=groupDao.getQQFriendsByGroupId(qq,friendGroupId);
		for(QQInfor qqInfo : qqInfors){
			qqInfo.setState((DataBox.selectionKeyExist(qqInfo.getUserName()+""))?(EState.ONLINE):(EState.OFFLINE));
		}
		return JsonUtil.convertToJson(JsonUtil.toJson(new EStateSerializer(),qqInfors));
	}
	
	/**
	 * 添加默认分组
	 */
	public void addDefaultGroup(int qqNum){
		groupDao.addDefaultGroups(qqNum);
	}
	
}
