package com.sky.qq.entity;

import com.sky.qq.annotation.Annotate;
import com.sky.qq.annotation.Attribute;
import com.sky.qq.annotation.XmlName;

@XmlName("Config.xml")
public class Config {
	
	@Attribute("qq_port")
	@Annotate("服务器端口号")
	private Integer port=8023;
	
	@Attribute("qq_onlineNum")
	@Annotate("服务器负载量")
	private Integer onlineNum=10;
	
	@Attribute("qq_dbDriverName")
	@Annotate("数据库驱动名称")
	private String driverName="com.mysql.jdbc.Driver";
	
	@Attribute("qq_dbUrl")
	@Annotate("数据库名称")
	private String databaseUrl="jdbc:mysql://127.0.0.1:3306/skyqq";
	
	@Attribute("qq_userName")
	@Annotate("数据库用户名")
	private String userName="root";
	
	@Attribute("qq_password")
	@Annotate("数据库密码")
	private String password="10086";
	
	@Attribute("qq_dbConnectionInitCountt")
	@Annotate("数据连接池初始连接数")
	private Integer initCount=10;
	
	@Attribute("qq_dbConnectionMaxCount")
	@Annotate("数据连接池最大连接数")
	private Integer maxCount=50;
	
	@Annotate("自动扫描类Path")
	private String classPath="com.sky.qq";
	
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getOnlineNum() {
		return onlineNum;
	}
	public void setOnlineNum(Integer onlineNum) {
		this.onlineNum = onlineNum;
	}
	
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDatabaseUrl() {
		return databaseUrl;
	}
	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}
	public Integer getIntCount() {
		return initCount;
	}
	public void setIntCount(Integer initCount) {
		this.initCount = initCount;
	}
	public Integer getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
