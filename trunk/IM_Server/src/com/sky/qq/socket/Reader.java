package com.sky.qq.socket;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.google.gson.JsonObject;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.StateBiz;
import com.sky.qq.entity.DataBox;
import com.sky.qq.entity.EState;
import com.sky.qq.handler.CommandHandler;
import com.sky.qq.util.ByteUtil;
import com.sky.qq.util.JsonUtil;
import com.sky.qq.util.ZLibUtil;

/**
 * 读取器
 * @author Administrator
 *
 */
@Service
public class Reader {
	//读线程池
	private ThreadPoolExecutor threadPool=new ThreadPoolExecutor(5, 10, 6000, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));
	
	@Autowired
	private StateBiz stateBiz;
	
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
					//远程断开连接
					if(total==-1){
						remoteDisconnect(key);
						return;
					}
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
							JsonObject json = (JsonObject)JsonUtil.convertToJson(new String(oneData,"UTF-8"));
							key.interestOps(SelectionKey.OP_READ);
							CommandHandler.execute(json,key);
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
					remoteDisconnect(key);
					return;
				}
			}
		});
	}
	
	/**
	 * 远程断开连接
	 */
	private void remoteDisconnect(SelectionKey key){
		String qq=DataBox.getQQ(key);
		System.out.println("用户:【"+qq+"】下线了");
		DataBox.removeQQ(key);
		try {
			key.channel().close();
		} catch (Exception e1) { 
			e1.printStackTrace();
		}
		Map<String,JsonObject> states=stateBiz.notifyQQState(Integer.parseInt(qq),EState.OFFLINE);
		for(String keyS : states.keySet()){
			SelectionKey notifyKey=DataBox.getKey(keyS);
			DataBox.addMes(notifyKey, states.get(keyS));
			notifyKey.interestOps(SelectionKey.OP_WRITE);
		}
		key.selector().wakeup();
	}
}
