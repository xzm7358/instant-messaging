package com.sky.qq.servlet;

import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;


public class WebServlet extends WebSocketServlet{

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,HttpServletRequest request) {
		return new WebMessageInbound();
	}


}
