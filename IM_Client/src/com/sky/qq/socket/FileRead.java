package com.sky.qq.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sky.qq.biz.DestoryCenter;
import com.sky.qq.entity.EFileTransType;
import com.sky.qq.handler.ICallBack;

/**
 * 文件读取服务
 * @author Administrator
 *
 */
public class FileRead {
	private static int port=0;
	
	private static ServerSocket server;
	//存储所有文件的回调
	private static Map<String, ICallBack> allTransferFile=new ConcurrentHashMap<String, ICallBack>();
	
	/**
	 * 添加文件发送回调
	 */
	public static void addCallBack(String key,ICallBack value){
		allTransferFile.put(key, value);
	}
	
	public static int getPort() {
		return port;
	}

	/**
	 * 启动文件传输服务
	 * @return
	 */
	public static boolean start(){
		boolean result=true;
		try {
			server=new ServerSocket(0);
			port=server.getLocalPort();
		} catch (IOException e) {
			e.printStackTrace();
			result=false;
		}
		return result;
	}
	
	/**
	 * 监听  一对一服务
	 */
	public static void listener(){
		new Thread(){
			public void run() {
				while(true){
					try {
						readData(server.accept());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
	
	/**
	 * 停止服务
	 * @return
	 */
	public static boolean stop(){
		boolean result=true;
		try {
			server.close();
		} catch (IOException e) {
			result=false;
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 数据读取
	 * @param socket
	 */
	private static void readData(final Socket socket){
		new Thread(){
			byte[] buffer=new byte[44];   //缓冲占32字节的md5值
			public void run() {
				try {
					int total=0;     
					DataInputStream dis=new DataInputStream(socket.getInputStream());
					while(total!=44){    //若未读满M5D 继续读取
						total+=dis.read(buffer,total,44-total);
					}
					String key=new String(buffer,0,total,"UTF-8");
					DestoryCenter.addSocket(EFileTransType.ACCEPT, String.valueOf(Integer.parseInt(key.substring(0, 12))), key.substring(12), socket);
					allTransferFile.get(key).exece(socket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

}
