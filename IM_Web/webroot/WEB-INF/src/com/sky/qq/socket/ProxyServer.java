package com.sky.qq.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.catalina.websocket.StreamInbound;

public class ProxyServer {
	
	private static ConcurrentLinkedQueue<ICallBack> taskList=new ConcurrentLinkedQueue<ICallBack>();
	private static NWriter writer=new NWriter();
	private static NReader reader=new NReader();
	
	private static Selector selector;
	/*服务器端地址*/  
	private final static InetSocketAddress SERVER_ADDRESS = new InetSocketAddress("127.0.0.1", 8023);
	//MessageInbound与SelectionKey映射
	private static Map<Object,SelectionKey> allSelectionKeys=new ConcurrentHashMap<Object,SelectionKey>();
	//SelectionKey与MessageInbound映射
	private static Map<SelectionKey,StreamInbound> allStreamInbound=new ConcurrentHashMap<SelectionKey,StreamInbound>();
	
	public static void addSelectionKey(Object key,SelectionKey value){
		allSelectionKeys.put(key, value);
	}

	public static SelectionKey getSelectionKey(Object key){
		return allSelectionKeys.get(key);
	}
	
	public static void addStreamInbound(SelectionKey key,StreamInbound value){
		allStreamInbound.put(key, value);
	}

	public static StreamInbound getStreamInbound(SelectionKey key){
		return allStreamInbound.get(key);
	}
	
	
	/**
	 * 开启服务
	 * 
	 * @throws Exception
	 */
	public static void start() throws Exception {
		selector = Selector.open();
		pull();
	}

	/**
	 *事件轮询
	 */
	private static void pull(){
		new Thread(){
			public void run() {
				pullKey();
			};
		}.start();
	}
	
	private static void pullKey(){
		while(true){
			try {
				//处理任务队列
				Iterator<ICallBack> itaskList=taskList.iterator();
				while(itaskList.hasNext()){
					 ICallBack callBack=itaskList.next();
					 callBack.doUpdate();
					 synchronized (callBack.getUUID()) {
						 callBack.getUUID().notify();
					 }
					itaskList.remove();
				}
				//轮询Key
				selector.select();
				Iterator<SelectionKey> keys=selector.selectedKeys().iterator();
				while(keys.hasNext()){
					try{
						handlerKey(keys.next());
  					}catch (Exception e) {
						e.printStackTrace();
					}
					keys.remove();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 处理Key
	 * 
	 * @param key
	 * @throws Exception
	 */
	public static void handlerKey(SelectionKey key) throws Exception {
		if (key.isConnectable()) {
			 System.out.println("client connect");  
			 SocketChannel client = (SocketChannel) key.channel();  
			// 判断此通道上是否正在进行连接操作。  
             // 完成套接字通道的连接过程。  
             if (client.isConnectionPending()) {  
            	 client.finishConnect();  
                 System.out.println("完成连接!");  
             }
             client.register(selector, SelectionKey.OP_READ,ByteBuffer.allocate(8192));
		} else if (key.isReadable()) {
			key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
			reader.read(key);
		} else if (key.isWritable()) {
			key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
			writer.write(key);
		}
	
 	}
	
	/**
	 * 关闭服务
	 */
	public static void stop() {

	}

	/**
	 * 创建到服务器的链接
	 * 
	 * @throws Exception
	 */
	public static SelectionKey createConnection() throws Exception {
		// 打开socket通道
		final SocketChannel socketChannel = SocketChannel.open();
		// 设置为非阻塞方式
		socketChannel.configureBlocking(false);
		// 连接   
		socketChannel.connect(SERVER_ADDRESS);
		final String uuid=UUID.randomUUID().toString().replace("-","");
		taskList.add(new ICallBack() {
			@Override
			public void doUpdate() {
				try {
					SelectionKeyFactory.setSelectionKey(getUUID(), socketChannel.register(selector, SelectionKey.OP_CONNECT));
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}

			@Override
			public String getUUID() {
				return uuid;
			}
		});
		selector.wakeup();
		return SelectionKeyFactory.getSelectionKey(uuid);
	}
}

interface ICallBack{
	void doUpdate();
	
	String getUUID();
}
