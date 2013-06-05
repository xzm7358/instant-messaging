package com.sky.qq.biz;

import java.nio.channels.SelectionKey;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.dao.MessageDao;
import com.sky.qq.entity.DataBox;
import com.sky.qq.util.JsonUtil;

@Service
public class MessageBiz {

	@Autowired
	private MessageDao messageDao;

	/**
	 * 保存消息
	 */
	public boolean saveMessage(int target,JsonElement obj) {
		return messageDao.saveMessage(target,obj);
	}

	/**
	 * 所有消息入库
	 */
	public void saveAllMessage(int target,SelectionKey key){
		ConcurrentLinkedQueue<JsonElement> jsonQueue=DataBox.getQueue(key);
		JsonElement element=null;
		while((element=jsonQueue.poll())!=null){
			messageDao.saveMessage(target, element);
		}
	}
	
	/**
	 * 查询消息
	 * 
	 * @param uId 
	 * @return
	 */
	public JsonArray query(int uId) {
		JsonArray result=new JsonArray();
		List<String> allMes=messageDao.query(uId);
		for(String mes : allMes){
			result.add(JsonUtil.convertToJson(mes));
		}
		return result;
	}
	
	/**
	 * 删除离线消息
	 * @param uId
	 * @return
	 */
	public boolean delete(int uId){
		return messageDao.delete(uId);
	}
}
