package com.sky.qq.handler;


public abstract class BaseHandler implements IHandler{

	private String type;
	
	public BaseHandler(String type){
		this.type=type;
		CommandHandler.regsiterHandler(this);
	}

	@Override
	public String getType() {
		return type;
	}

}
