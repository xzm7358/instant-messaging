package com.sky.qq.entity;

import com.google.gson.annotations.Expose;

public class QQ {
	@Expose
	private int userName;
	@Expose
	private String password;
	public int getUserName() {
		return userName;
	}
	public void setUserName(int userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public QQ(int userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	public QQ(int userName) {
		this.userName = userName;
	}
	public QQ() {
	}
}
