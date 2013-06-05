package com.sky.qq.entity;

import com.google.gson.annotations.Expose;

public class QQInfor extends QQ {
	@Expose
	private String nickName;
	@Expose
	private String faceImg; 
	@Expose
	private EState state;
	
	public QQInfor() {
	}

	public QQInfor(int userName, String password) {
		super(userName, password);
	}

	public QQInfor(int userName, String nickName,String faceImg) {
		super(userName);
		this.setNickName(nickName);
		this.faceImg=faceImg;
	}
	
	public String getFaceImg() {
		return faceImg;
	}

	public void setFaceImg(String faceImg) {
		this.faceImg = faceImg;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public EState getState() {
		return state;
	}

	public void setState(EState state) {
		this.state = state;
	}

}
