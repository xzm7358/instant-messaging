package com.sky.qq.biz;
import com.google.gson.JsonObject;

public interface Observer {
	public void update(JsonObject message);
	public String getFrmType();
}
