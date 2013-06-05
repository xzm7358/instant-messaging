
net.sky.qq.biz.Subject=function(){
	//存储所有打开的好友窗口
	var friendObserver=new com.js.util.HashMap();
	//存储所有打开的群聊天窗口
	var groupObserver=new com.js.util.HashMap();
	
	//添加观察者
	this.attach=function(qq,observer){
		if(observer.getFrmType()=="friendMes"){ 
			friendObserver.put(qq, observer);
		}else{
			groupObserver.put(qq, observer);
		}
	}
	
	//删除观察者
	this.dettach=function(qq,observer){
		if(observer.getFrmType()=="friendMes"){
			friendObserver.remove(qq);
		}else{
			groupObserver.remove(qq);
		}
	} 
	
	//通知观察者
	this.notifyObserver=function(message){
		var mesType=message.type;
		var playload=message.playload;
		var sender=playload.sender;
		if(mesType=="groupMes"){
			var observer=groupObserver.get(sender);
			if(observer!=null){
				observer.update(playload); 
			}else{ 
				//将消息加入到群消息盒子
				net.sky.qq.biz.MessageBox.getGroupMessages().get(sender).add(playload);
			}
		}else{
			var observer=friendObserver.get(sender);
			if(observer!=null){
				observer.update(message);
			}else{
				//将消息加入到好友消息盒子
				net.sky.qq.biz.MessageBox.getQqMessages().get(sender).add(message);
			}
		}
	}
	
	//判断观察者是否存在
	this.observerExist=function(qq,fType){
		var result=false;
		if(fType=="friendMes"){
			if(friendObserver.containsKey(qq)){
				result=true;
			}
		}else{
			if(groupObserver.containsKey(qq)){
				result=true;
			}
		}
		return result;
	}
}

//消息盒子 ---管理所有的好友消息、群消息
net.sky.qq.biz.MessageBox=function(){
	
};
net.sky.qq.biz.MessageBox.prototype=new net.sky.qq.biz.Subject();

//好友消息盒子
net.sky.qq.biz.MessageBox.qqMessages=new com.js.util.HashMap();
//群消息盒子
net.sky.qq.biz.MessageBox.groupMessages=new com.js.util.HashMap();
net.sky.qq.biz.MessageBox.MeBox=new net.sky.qq.biz.MessageBox();

//处理消息
net.sky.qq.biz.MessageBox.execMes=function(message){
	net.sky.qq.biz.MessageBox.MeBox.notifyObserver(message);
}

net.sky.qq.biz.MessageBox.getQqMessages=function(){
	return net.sky.qq.biz.MessageBox.qqMessages;
}

//添加qq键
net.sky.qq.biz.MessageBox.addQQKey=function(qq){
	net.sky.qq.biz.MessageBox.qqMessages.put(qq, new com.js.util.ArrayList());
}

//添加Group键
net.sky.qq.biz.MessageBox.addGroupKey=function(qq){
	net.sky.qq.biz.MessageBox.groupMessages.put(qq, new com.js.util.ArrayList());
}

net.sky.qq.biz.MessageBox.getGroupMessages=function() {
	return net.sky.qq.biz.MessageBox.groupMessages;
}
