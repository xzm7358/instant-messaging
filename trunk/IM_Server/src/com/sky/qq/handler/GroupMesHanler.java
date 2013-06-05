
package com.sky.qq.handler;
import java.nio.channels.SelectionKey;
import com.google.gson.JsonObject;
import com.sky.qq.annotation.Autowired;
import com.sky.qq.annotation.Service;
import com.sky.qq.biz.QQInforBiz;

@Service
public class GroupMesHanler extends BaseHandler implements IHandler {

	@Autowired
	private QQInforBiz qqInforBiz;
	
	public GroupMesHanler() {
		super("-------");
	}

	@Override
	public void run(JsonObject obj,SelectionKey key) {
		//System.out.println(obj.toString());
		
	}

}
