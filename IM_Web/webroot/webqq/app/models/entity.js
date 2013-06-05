
net.sky.qq.entity.GroupNode=function(name,friendSize){
	var $name=name;
	var $friendSize;
	var $onlineNum;
	
	this.setName=function(name){
		$name=name;
	};
	
	this.getName=function(){
		return $name;
	};
	
	this.setFriendSize=function(friendSize){
		$friendSize=friendSize;
	};
	
	this.getFriendSize=function(){
		return $friendSize
	};
	
	this.setOnlineNum=function(onlineNum){
		$onlineNum=onlineNum;
	};
	
	this.getOnlineNum=function(){
		return $onlineNum;
	};
	
	this.toString=function(){
		return $name+" ["+$onlineNum+"/"+$friendSize+"]";
	}
	
}

net.sky.qq.entity.Owner=function(){
	
}

net.sky.qq.entity.Owner.qq;

net.sky.qq.entity.EState={
	ONLINE:0,		//在线
	OFFLINE:1,	    //离线
	LEAVE:2,		//离开
	BUSY:3,			//忙碌
	STEALTH:4		//隐身
};

net.sky.qq.entity.EOwnState={
	LOCK:0,			//锁定
	KICKOFF:1,		//强制下线
	OFFSITE:2		//异地登陆
}
