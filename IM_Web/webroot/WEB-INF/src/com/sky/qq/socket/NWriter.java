package com.sky.qq.socket;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.sky.qq.biz.DataBox;
import com.sky.qq.util.ByteUtil;
import com.sky.qq.util.ZLibUtil;

/**
 * 写入器
 * @author Administrator
 *
 */
public class NWriter {
	//写线程池
	private ThreadPoolExecutor threadPool=new ThreadPoolExecutor(5, 10, 6000, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));
	
	public void write(final SelectionKey key){
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				SocketChannel socketChannel=(SocketChannel) key.channel();
				String data=null;
				ConcurrentLinkedQueue<String> datas=DataBox.getInputDataValue(key);
				while((data=datas.poll())!=null){
					try {
						System.out.println("队列数据:"+data.toString());
						socketChannel.write(ByteBuffer.wrap(packageDataToByte(data.getBytes("UTF-8"))));
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				//切换为读事件
				key.interestOps(SelectionKey.OP_READ);
				key.selector().wakeup();
			}
		});
	}
	
	private byte[] packageDataToByte(byte[] data){
		byte[] compressData=ZLibUtil.compress(data);
		byte[] content=new byte[compressData.length+4];
		byte[] sendLength=ByteUtil.intToBytes(compressData.length);
		System.arraycopy(sendLength,0,content,0,4);
		System.arraycopy(compressData,0,content,4,compressData.length);
		
		
		
//		byte[] compressData=ZLibUtil.compress(data);
//		byte[] content=new byte[compressData.length+4];
//		byte[] sendLength=ByteUtil.intToBytes(compressData.length);
//		
//		System.arraycopy(sendLength, 0, content, 0, 4);
//		System.arraycopy(compressData, 0, content, 4, compressData.length);
		
//		byte[] oneData=ZLibUtil.decompress(decompressData);
//		String command=new String(oneData,"UTF-8");
//		
//		
//    	byte[] content=new byte[data.length+4];
//		byte[] sendLength=ByteUtil.intToBytes(data.length);
//		System.arraycopy(sendLength,0,content,0,sendLength.length);
//		System.arraycopy(data,0,content,4,data.length);
		return content;
    }
	
}
