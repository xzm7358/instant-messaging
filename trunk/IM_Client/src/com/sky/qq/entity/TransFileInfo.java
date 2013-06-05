package com.sky.qq.entity;

public class TransFileInfo {
	private EFileTransType fileTransType;
	private int target;
	private String fileName;
	private long fileSize;
	private long saveSize=0;
	private String md5;
	private String savePath;
	private String filePath;
	private long position;
	
	public EFileTransType getFileTransType() {
		return fileTransType;
	}
	public void setFileTransType(EFileTransType fileTransType) {
		this.fileTransType = fileTransType;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public long getSaveSize() {
		return saveSize;
	}
	public void setSaveSize(long saveSize) {
		this.saveSize = saveSize;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getPosition() {
		return position;
	}
	public void setPosition(long position) {
		this.position = position;
	}
	
}
