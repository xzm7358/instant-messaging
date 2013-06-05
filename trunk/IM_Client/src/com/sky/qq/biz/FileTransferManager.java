package com.sky.qq.biz;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.sky.qq.entity.EFileTransType;

/**
 * 文件传输管理器
 * @author Administrator
 *
 */
public class FileTransferManager {
	//发送类Socket
	private static Map<String, ConcurrentHashMap<String, Socket>> sendSocket=new ConcurrentHashMap<String, ConcurrentHashMap<String,Socket>>();
	
	//接受类Socket
	private static Map<String, ConcurrentHashMap<String, Socket>> acceptSocket=new ConcurrentHashMap<String, ConcurrentHashMap<String,Socket>>();
	
	/**
	 * 添加Socket
	 * @param fileTransType
	 * @param keyOne
	 * @param keyTwo
	 * @param socket
	 */
	public static void addSocket(EFileTransType fileTransType,String keyOne,String keyTwo,Socket socket){
		ConcurrentHashMap<String,Socket> sockets=null;
		if(fileTransType==EFileTransType.SEND){
			sockets=(sendSocket.containsKey(keyOne))?(sendSocket.get(keyOne)):(new ConcurrentHashMap<String, Socket>());
			sendSocket.put(keyOne, sockets);
		}else{
			sockets=(acceptSocket.containsKey(keyOne))?(acceptSocket.get(keyOne)):(new ConcurrentHashMap<String, Socket>());
			sendSocket.put(keyOne, sockets);
		}
		sockets.put(keyTwo, socket);
	}
	
	/**
	 * 删除Socket
	 * @param fileTransType
	 * @param keyOne
	 * @param keyTwo
	 */
	public static void removeSocket(EFileTransType fileTransType,String keyOne,String keyTwo){
		ConcurrentHashMap<String,Socket> sockets=(fileTransType==EFileTransType.SEND)?(sendSocket.get(keyOne)):(acceptSocket.get(keyOne));
		sockets.remove(keyTwo);
	}
	
	/**
	 * 判断Socket是否存在
	 * @param fileTransType
	 * @param keyOne
	 * @param keyTwo
	 * @return
	 */
	public static boolean existSocket(EFileTransType fileTransType,String keyOne,String keyTwo){
		boolean result=true;
		if(fileTransType==EFileTransType.SEND){
			if(!sendSocket.containsKey(keyOne)){
				result=false;
			}else{
				if(!sendSocket.get(keyOne).contains(keyTwo)){
					result=false;
				}
			}
		}else{
			if(!acceptSocket.containsKey(keyOne)){
				result=false;
			}else{
				if(!acceptSocket.get(keyOne).contains(keyTwo)){
					result=false;
				}
			}
		}
		return result;
	}
	
	/**
	 * 清空并关闭Socket
	 */
	public static void clearAndClose(){
		for(Map<String, Socket> allSocket : sendSocket.values()){
			for(Socket socket : allSocket.values()){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for(Map<String, Socket> allSocket : acceptSocket.values()){
			for(Socket socket : allSocket.values()){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
