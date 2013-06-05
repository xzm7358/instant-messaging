package com.sky.qq.handler;

import javax.swing.JOptionPane;
import com.google.gson.JsonObject;
import com.sky.qq.biz.DestoryCenter;
import com.sky.qq.entity.EOwnState;
import com.sky.qq.ui.Login;

public class OwnStateHanler extends BaseHandler implements IHandler {

	public OwnStateHanler() {
		super("ownState");	
	}

	@Override
	public void run(JsonObject obj) {
		JsonObject playload=obj.get("playload").getAsJsonObject();
		if(playload.get("state").getAsInt()==EOwnState.getIndex(EOwnState.OFFSITE)){
			DestoryCenter.clearAndClose();
			new Login();
			JOptionPane.showMessageDialog(null, "您的QQ在异地登陆,您被迫下线","提示",0);
		}
		
	}

}
