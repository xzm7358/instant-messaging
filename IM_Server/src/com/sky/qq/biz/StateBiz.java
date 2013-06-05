package com.sky.qq.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.JsonObject;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.dao.FriendGroupDao;
import com.sky.qq.entity.DataBox;
import com.sky.qq.entity.EState;
import com.sky.qq.entity.QQInfor;

@Service
public class StateBiz {
	
	@Autowired
	private FriendGroupDao friendGroupDao;
	
	public Map<String,JsonObject> notifyQQState(int qqNum,EState state){
		Map<String,JsonObject> result=new HashMap<String, JsonObject>();
		List<QQInfor> qqInfors=friendGroupDao.getQQFriends(qqNum);
		for(QQInfor qq : qqInfors){
			if(DataBox.selectionKeyExist(qq.getUserName()+"")){
				JsonObject root=new JsonObject();
				root.addProperty("type", "state");
				JsonObject playload=new JsonObject();
				playload.addProperty("userName", qqNum);
				playload.addProperty("state", EState.getIndex(state));
				root.add("playload", playload);
				result.put(String.valueOf(qq.getUserName()), root);
			}
		}
		return result;
	}
}
