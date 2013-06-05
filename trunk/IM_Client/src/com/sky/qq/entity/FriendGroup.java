package com.sky.qq.entity;

/**
 * 好友分组
 * @author Administrator
 *
 */
public class FriendGroup {
	private int id;
	private String name;
	//好友数量
	private int friendSize;
	//在线好友数量
	private int onlineNum;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFriendSize() {
		return friendSize;
	}
	public void setFriendSize(int friendSize) {
		this.friendSize = friendSize;
	}
	public int getOnlineNum() {
		return onlineNum;
	}
	public void setOnlineNum(int onlineNum) {
		this.onlineNum = onlineNum;
	}
	@Override
	public String toString() {
		return name+" ["+onlineNum+"/"+friendSize+"]";
	}
	
	
}
