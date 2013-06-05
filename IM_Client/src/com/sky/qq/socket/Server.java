package com.sky.qq.socket;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import com.sky.qq.handler.CommandHandler;
import com.sky.qq.util.ByteUtil;
import com.sky.qq.util.JsonUtil;
import com.sky.qq.util.ZLibUtil;

public class Server {
	private static Socket socket;
	private static DataInputStream dis;
	private static DataOutputStream dos;
	private static byte[] buffer = new byte[1024];
	private static ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream(5120);

	/**
	 * 连接服务器
	 * 
	 * @return
	 */
	public static boolean connect() {
		boolean result = true;
		try {
			socket = new Socket("127.0.0.1", 8023);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			read();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 断开服务器
	 * 
	 * @return
	 */
	public static boolean disconnect() {
		boolean result = true;
		try {
			socket.close();
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 读取数据
	 */
	public static void read() {
		new Thread() {
			public void run() {
				try {
					int lengthByteIndex=0;
					byte[] lengthByte = new byte[4];
					int bodyLength = 0;  //包体长度
					int readLength = 0;  //数据读取长度
					while (true) {
						int n = dis.read(buffer,0,buffer.length);
						if(n==-1){
							break;
						}
						readLength += n;
						if (bodyLength == 0) {
							System.arraycopy(buffer, 0, lengthByte, 0, 4-lengthByteIndex);
							bodyLength = ByteUtil.bytesToInt(lengthByte);
							baOutputStream.write(buffer, 4-lengthByteIndex, n - (4-lengthByteIndex));
						} else {
							baOutputStream.write(buffer, 0, n);
						}
						while(readLength >=(bodyLength + 4)){	//循环处理粘包
							byte[] data=baOutputStream.toByteArray();
							System.out.println("数据长度:"+data.length);
							byte[] oneData=new byte[bodyLength];
							System.arraycopy(data, 0, oneData, 0, bodyLength);
							//数据解压缩
							byte[] decompressData=ZLibUtil.decompress(oneData);
							String response = new String(decompressData, 0, decompressData.length, "UTF-8");
							System.out.println("接收服务端数据#长度:"+bodyLength+"内容:"+response+"\r\n");
							CommandHandler.execute(JsonUtil.convertToJson(response));
							baOutputStream.reset();
							readLength=data.length-bodyLength;
							if(readLength < 4){		//包体长度长度不足
								bodyLength=0;
								System.arraycopy(buffer, bodyLength, lengthByte, 0, readLength);   //保存残缺的数据长度字节
								lengthByteIndex=readLength;
							}else{ 
								System.arraycopy(data, bodyLength, lengthByte, 0, 4);
								baOutputStream.write(data,bodyLength+4,readLength-4);  //将多余的数据写入
								bodyLength = ByteUtil.bytesToInt(lengthByte);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 写入数据
	 */
	public static void write(byte[] data) {
		byte[] compressData=ZLibUtil.compress(data);
		byte[] content=new byte[compressData.length+4];
		byte[] sendLength=ByteUtil.intToBytes(compressData.length);
		System.arraycopy(sendLength,0,content,0,sendLength.length);
		System.arraycopy(compressData,0,content,4,compressData.length);
		
		try {
			dos.write(content);
			dos.flush();
			System.out.println("发送数据长度:【"+data.length+","+compressData.length+"】\t"+new String(data,"UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得本地Ip地址
	 * @return
	 */
	public static String getIpAddress(){
		return socket.getLocalAddress().getHostAddress();
	}

}
