package com.sky.qq.socket;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.catalina.websocket.WsOutbound;
import com.sky.qq.util.ByteUtil;
import com.sky.qq.util.ZLibUtil;

/**
 * 读取器
 * @author Administrator
 *
 */
public class NReader {
	//读线程池
	private ThreadPoolExecutor threadPool=new ThreadPoolExecutor(5, 10, 6000, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));
	
	/**
	 * 读取SocketChannel中的数据
	 * @param channel
	 */
	public void read(final SelectionKey key){
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					int bodyLength = 0;  //包体长度
					int dataLength=0;
					SocketChannel channel=(SocketChannel)key.channel();
					ByteBuffer buffer=(ByteBuffer)key.attachment();
					int total = channel.read(buffer);
					int readLength = total;
					byte[] data=buffer.array();
					while(readLength >= 4){
						bodyLength = ByteUtil.bytesToInt(data,total-readLength);
						dataLength=readLength-4;
						if(dataLength >= bodyLength){
							readLength=readLength-bodyLength-4;
							
							byte[] decompressData=new byte[bodyLength];
							System.arraycopy(data, total-dataLength, decompressData, 0, decompressData.length);
							byte[] oneData=ZLibUtil.decompress(decompressData);
							String command=new String(oneData,"UTF-8");
							
							
							//String command=new String(data,total-dataLength,bodyLength,"UTF-8");
							System.out.println("接受数据:"+command);
							WsOutbound outbound = ProxyServer.getStreamInbound(key).getWsOutbound();
							
							outbound.writeTextMessage(CharBuffer.wrap(command));
							outbound.flush();
						}else{
							readLength=-1;
							buffer.clear();
							for(int i=total-dataLength;i<total;i++){
								buffer.put(data[i]);
							}
						}
					}
					if(readLength != -1){
						buffer.clear();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					key.selector().wakeup();
					return;
				}
				key.interestOps(SelectionKey.OP_READ);
				key.selector().wakeup();
			} 
		});
	}
	
	
	
}
