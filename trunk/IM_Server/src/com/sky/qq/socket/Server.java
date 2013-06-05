package com.sky.qq.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.ProgramVariable;

@Service
public class Server {
	
	private Selector selector;
	private ServerSocketChannel socketChannel;
	
	@Autowired
	private Reader reader;
	
	@Autowired
	private Writer writer;
	
	/**
	 * 启动服务
	 * @return
	 */
	public boolean start(){
		boolean result=true;
		try {
			this.selector = Selector.open();
			socketChannel = ServerSocketChannel.open();
			ServerSocket socket = socketChannel.socket();
			socket.bind(new InetSocketAddress(ProgramVariable.getConfig().getPort()));
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			result=false;
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 停止服务
	 * @return
	 */
	public boolean stop(){
		boolean result=true;
		try {
			socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
			result=false;
		}
		return result;
	}
	
	/**
	 *事件轮询
	 */
	public void pull(){
		new Thread(){
			public void run() {
				pullKey();
			};
		}.start();
	}
	
	
	/**
	 * 处理Key
	 * 
	 * @param key
	 * @throws Exception
	 */
	public void handlerKey(SelectionKey key) throws Exception {
		if (key.isAcceptable()) {
			SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();// 对应socketchannel
			System.out.println("新用户上线##端口号为:"+channel.socket().getPort());
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ,ByteBuffer.allocate(8092));// 可读
		} else if (key.isReadable()) {
			key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
			reader.read(key);
		} else if (key.isWritable()) {
			key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
			writer.write(key);
		}
	}
	
	/**
	 * 循环拉取SelectionKey
	 */
	private void pullKey(){
		while(true){
			try {
				selector.select();
				System.out.println("轮训中.....");
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
}
