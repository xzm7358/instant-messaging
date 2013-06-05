package com.sky.qq.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendGroup {
	@Expose
	@SerializedName("id") 
	private int frId;
	
	private int uId;
	@Expose
	
	private String name;
	public int getFrId() {
		return frId;
	}
	public void setFrId(int frId) {
		this.frId = frId;
	}
	public int getuId() {
		return uId;
	}
	public void setuId(int uId) {
		this.uId = uId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FriendGroup(int frId, String name) {
		super();
		this.frId = frId;
		this.name = name;
	}
	
}
