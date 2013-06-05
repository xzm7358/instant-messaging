package com.sky.qq.entity;

/**
 * QQ信息
 * @author Administrator
 *
 */
public class QQInfor extends QQ{
	private String nickName;
	private String faceImg;
	private EState state;
	private byte[] faceImgData;
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getFaceImg() {
		return faceImg;
	}
	public void setFaceImg(String faceImg) {
		this.faceImg = faceImg;
	}
	public EState getState() {
		return state;
	}
	public void setState(EState state) {
		this.state = state;
	}
	public byte[] getFaceImgData() {
		return faceImgData;
	}
	public void setFaceImgData(byte[] faceImgData) {
		this.faceImgData = faceImgData;
	}
	@Override
	public String toString() {
		return nickName;
	}
	
	
}
