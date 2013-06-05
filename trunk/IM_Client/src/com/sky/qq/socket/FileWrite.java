package com.sky.qq.socket;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import com.sky.qq.handler.ICallBack;

/**
 * 文件写入服务
 * @author Administrator
 *
 */
public class FileWrite {
	
	private Socket socket;
	private ICallBack callBack;
	
	public FileWrite(ICallBack callBack){
		this.callBack=callBack;
	}
	/**
	 * 连接服务器
	 * @return
	 */
	public boolean connect(String host,int port){
		boolean result=true;
		try {
			socket=new Socket(host, port);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	/**
	 * 传输文件
	 * @param file
	 * @param md5
	 */
	public void write(File file,String md5,long position) throws Exception{
			DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
			dos.write(md5.getBytes("UTF-8"));
			dos.flush();
			int i=-1;
			FileInputStream fis=new FileInputStream(file);
			fis.skip(position);
			byte[] buffer=new byte[4096];
			while((i=fis.read(buffer))!=-1){
				dos.write(buffer,0,i);
				try {
					//更新进度条
					callBack.exece(i);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
			dos.flush();
			dos.close();
			fis.close();
	}

}
