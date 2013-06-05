package com.sky.qq.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonElement;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;

@Service
public class MessageDao {
	
	@Autowired
	private BaseDao baseDao;
	/**
	 * 保存消息
	 */
	public boolean saveMessage(int target,JsonElement obj){
		return baseDao.exeProc("UP_Message_Insert", target,obj.toString());
	}
	
	/**
	 * 查询消息
	 * @param uId
	 * @return
	 */
	public List<String> query(int uId){
		List<String> result=new ArrayList<String>();
	    ResultSet rs=baseDao.execProcReutnSet("UP_Message_Get", uId);
	    try {
			while(rs.next()){
				result.add(rs.getString("mes"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return result;
	}
	
	/**
	 * 删除离线消息
	 * @param uId
	 * @return
	 */
	public boolean delete(int uId){
		return baseDao.exeProc("UP_Message_Delete", uId);
	}
}
