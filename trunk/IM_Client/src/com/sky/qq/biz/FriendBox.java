package com.sky.qq.biz;

import java.util.HashMap;
import java.util.Map;

/**
 * 好友盒子
 * @author Administrator
 *
 */
public class FriendBox {

	private static Map<String,String> qqForGroupId=new HashMap<String, String>();
	
	public static void setGroupId(String qq,String groupId){
		qqForGroupId.put(qq, groupId);
	}
	
	public static String getGroupId(String qq){
		return qqForGroupId.get(qq);
	}
}
