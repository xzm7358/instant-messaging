package com.sky.qq.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import com.sky.qq.util.ByteUtil;
import com.sky.qq.util.ZLibUtil;

/**
 * 优先写入
 * @author Administrator
 *
 */
public class PriorityWriter {
	
	/**
	 * 数据写入
	 * @param socket
	 * @param obj
	 */
	public static void write(SelectionKey key,byte[] data){
		SocketChannel socketChannel=(SocketChannel) key.channel();
		
		byte[] compressData= ZLibUtil.compress(data);
		
		byte[] content=new byte[compressData.length+4];
		byte[] sendLength=ByteUtil.intToBytes(compressData.length);
		System.arraycopy(sendLength,0,content,0,sendLength.length);
		System.arraycopy(compressData,0,content,4,compressData.length);
		
		try {
			synchronized (key) {
				socketChannel.write(ByteBuffer.wrap(content));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		DataBox.removeQQ(key);
//		try {
//			socketChannel.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		key.cancel();
	}
	
}
