package com.sky.qq.socket;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.google.gson.JsonElement;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.MessageBiz;
import com.sky.qq.entity.DataBox;
import com.sky.qq.util.ByteUtil;
import com.sky.qq.util.ZLibUtil;

/**
 * 写入器
 * @author Administrator
 *
 */
@Service
public class Writer {
	//写线程池
	private ThreadPoolExecutor threadPool=new ThreadPoolExecutor(5, 10, 6000, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));
	
	@Autowired
	private MessageBiz messageBiz;
	
	public void write(final SelectionKey key){
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				writeData(key);
			}
		});
	}
	
	private void writeData(SelectionKey key){
		SocketChannel socketChannel=(SocketChannel) key.channel();
		//发送数据盒子中的消息
		ConcurrentLinkedQueue<JsonElement> jsonQueue=DataBox.getQueue(key);
		JsonElement element=null;
		while((element=jsonQueue.poll())!=null){
			try{
				String data=element.toString();
				byte[] dataByte=data.getBytes("UTF-8");
				byte[] compressData= ZLibUtil.compress(dataByte);
				
				byte[] content=new byte[compressData.length+4];
				byte[] sendLength=ByteUtil.intToBytes(compressData.length);
				System.arraycopy(sendLength,0,content,0,sendLength.length);
				System.arraycopy(compressData,0,content,4,compressData.length);
				synchronized (key) {
					//当QQ socket关闭时 此处会有异常
					try{
						socketChannel.write(ByteBuffer.wrap(content));
						System.out.println("ServerSend:数据长度【"+dataByte.length+ ","+compressData.length+"】\t"+data);
					}catch(Exception e){
						e.printStackTrace();
						//判断是否异地登陆 如果是转移前一个QQ登陆点所关联的消息数据 否则消息入库
						//@D 这部有问题   某些环境下 DataBox.getQQ(key)取得的为空 
						String qq=DataBox.getQQ(key);
						if(DataBox.existQQ(qq)){
							DataBox.getQueue(key).add(element);
						}else{
							messageBiz.saveMessage(Integer.parseInt(qq), element);
						}
						break;
					}
//					try{
//						socketChannel.write(ByteBuffer.wrap(content));
//					}catch(Exception e){
//						e.printStackTrace();
//						//Socket异常则消息入库
//						int target=Integer.parseInt(DataBox.getQQ(key));
//						messageBiz.saveMessage(target, element);
//						while((element=jsonQueue.poll())!=null){
//							messageBiz.saveMessage(target, element);
//						}
//						break;
//					}
					
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//切换为读事件
		key.interestOps(SelectionKey.OP_READ);
		key.selector().wakeup();
	}
}
