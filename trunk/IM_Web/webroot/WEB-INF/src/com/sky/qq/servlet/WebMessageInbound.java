package com.sky.qq.servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import com.sky.qq.biz.DataBox;
import com.sky.qq.socket.ProxyServer;

public class WebMessageInbound extends MessageInbound {
	
    protected void onOpen(WsOutbound outbound) {
        super.onOpen(outbound);
        try {
        	SelectionKey selectionKey=ProxyServer.createConnection();
        	ProxyServer.addSelectionKey(this, selectionKey);
        	ProxyServer.addStreamInbound(selectionKey, this);
        	DataBox.initData(selectionKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    protected void onClose(int status) {
        super.onClose(status);
        SelectionKey selectionKey=ProxyServer.getSelectionKey(this);
        try {
        	SocketChannel scoket=(SocketChannel)selectionKey.channel();
        	scoket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * 处理二进制
     */
    protected void onBinaryMessage(ByteBuffer buffer) throws IOException {
       // logger.info("Binary Message Receive: " + buffer.remaining());
    }

    /**
     * 处理字符串
     */
    protected void onTextMessage(CharBuffer buffer) throws IOException {
    	SelectionKey selectionKey=ProxyServer.getSelectionKey(this);
    	System.out.println("accept web data:"+buffer.toString());
    	DataBox.setInputDataValue(selectionKey, buffer.toString());
    	selectionKey.interestOps(SelectionKey.OP_WRITE);
    	selectionKey.selector().wakeup();
    }
}