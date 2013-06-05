steal("jquery/class", function($) {
	
	$.Class("com.sky.qq.biz.Main", {
		
	}, {
		init : function(data) {
			this.data=data;
			this.friends=new com.js.util.HashMap();
			//好友所关联的组ID
	    	this.friendsMapGroup=new com.js.util.HashMap(); 
		},
		getFriendGroup:function(){
	   		this.friendGroupBiz=new com.sky.qq.biz.FriendGroupBiz();
	   		this.friendGroupBiz.getGroup();
	   },
		fillData:function(){
			$("#qq_face img").attr("src","data:image/jpg;base64,"+this.data.faceImg);
			$("#qq_nickName").text(this.data.nickName);
		},
		//处理FriendGroup
		execFriendGroupRsponse:function(response){
			if(response.playload.type=="group"){
				this.groupSize=response.playload.groups.length;
				var groups=response.playload.groups;
				this.groupsMode=new com.js.util.HashMap();
				for(var p in groups){
					var groupNode=new net.sky.qq.entity.GroupNode(groups[p].name);
					this.groupsMode.put(groups[p].id,groupNode);
				}
				var html=$.View("/IM_Web/webqq/app/view/friendList.ejs",{"groups":groups});
				$("#friend_list").append(html);
				this.registerGroupEvent();
				this.getFriends(groups);
			}else{
				//alert(JSON.stringify(data));
				this.loadFriends(response.playload);
			}
		},
		//获取好友
	    getFriends:function(groups){
	   		for(var p in groups){
	   			this.friendGroupBiz.getFriend(groups[p].id);   			
	   		}
	    },
	    //加载好友
	    loadFriends:function(data){
	   		var groupContainer=$("#group_container_"+data.groupId);
	   		var friends=data.friends;
	   		var i=0;
	   		
	   		for(var p in friends){
	   			net.sky.qq.biz.MessageBox.addQQKey(friends[p].userName);
	   			this.friendsMapGroup.put(friends[p].userName,data.groupId);
	   			this.friends.put(friends[p].userName,friends[p]);
	   			var div="<div class='qq_infor' tag='"+friends[p].userName+"'><div class='qq_infor_faceimg'><img id=\""+friends[p].userName+"_faceImg\" src='data:image/jpg;base64,"+friends[p].faceImg+"' /></div><div>"+friends[p].nickName+"</div></div>";
	   			groupContainer.append($(div));
	   			if(friends[p].state==net.sky.qq.entity.EState.ONLINE){
	   				i++;
	   			}else{
	   				$("#"+friends[p].userName+"_faceImg").css("opacity","0.4");
	   			}
	   		}
	   		var groupNode=this.groupsMode.get(data.groupId);
	   		groupNode.setFriendSize(friends.length);
	   		groupNode.setOnlineNum(i);
	   		$("#group_name_"+data.groupId).text(groupNode.toString());
	   		if(--this.groupSize==0){
	   			this.registerOtherHandler();
	   			this.registerFriendEvent();
	   		}
	    },
	   //注册好友分组单击事件
	    registerGroupEvent:function(){
	   		var scope=this;
	   		$(".one_group").click(function(){
	   			var obj=$(this);
	   			var id=obj.attr("tag");
	   			var groupContainer=$("#group_container_"+id); 
	   			var groupIco=$("#group_ico_"+id);
	   			if(scope.groupsMode.get(id).getFriendSize() > 0){
	   				if(obj.attr("isExpand")=="false"){
		   				groupContainer.css("height","auto");
		   				groupIco.css("background","url('/IM_Web/webqq/app/images/eqq_sprite.gif') no-repeat scroll 5px -16px transparent");
		   				obj.attr("isExpand","true")
	   				}else{
		   				groupContainer.css("height","0px");
		   				groupIco.css("background","url('/IM_Web/webqq/app/images/eqq_sprite.gif') no-repeat scroll -12px -16px transparent");
		   				obj.attr("isExpand","false")
	   				}
	   			}
	   			
	   		});
	    },
	    //注册好友事件
	    registerFriendEvent:function(){
	    	var scope=this;
	    	$(".qq_infor").dblclick(function(){
	    		var qqInfor=scope.friends.get($(this).attr("tag"));
	    		var messageBox=net.sky.qq.biz.MessageBox.MeBox;
	    		if(!messageBox.observerExist(qqInfor.userName,"friendMes")){
	    			var chat=new com.sky.qq.ui.Chat($("#container"),qqInfor);
	    			messageBox.attach(qqInfor.userName,chat);
	    		}
	    	});
	    	$(".qq_infor").mouseover(function(){
	   			$(this).css("background-color","rgb(203,231,252)");
	   		});
	   		$(".qq_infor").mouseout(function(){
	   			$(this).css("background-color","transparent");
	   		});
	    },
	   //注册friendGroup处理器
	    registerFriendGroupHandler:function(){
	   		var scope=this;
	   		com.sky.qq.handler.CommandHandler.registerHandler({
	   			type:"friendGroup",
	   			run:function(data){
	   				scope.execFriendGroupRsponse(data);
	   			}
	   		});
	   },
	   //注册其它处理器
	   registerOtherHandler:function(){
	   		var scope=this;
	   		com.sky.qq.handler.CommandHandler.registerHandler({
	   			type:"ownState",
	   			run:function(data){
	   				if(data.playload.state==net.sky.qq.entity.EOwnState.OFFSITE){
	   					qq.socket.close();
	   					alert("您的QQ在异地登陆，您将被迫下线");
	   					location.href="index.jsp";
	   				}
	   			}
	   		});
	   		//注册好友消息处理器
	   		com.sky.qq.handler.CommandHandler.registerHandler({
	   			type:"qqMes",
	   			run:function(data){
	   				net.sky.qq.biz.MessageBox.execMes(data);
	   			}
	   		});
	   		//注册查询好友处理器
	   		com.sky.qq.handler.CommandHandler.registerHandler({
	   			type:"searchFriend",
	   			run:function(data){
	   				console.log(JSON.stringify(data));
	   			}
	   		});
	   		com.sky.qq.handler.CommandHandler.registerHandler({
	   			type:"state",
	   			run:function(data){
	   				console.log(JSON.stringify(data));
	   				var qq=data.playload;
	   				var groupId=scope.friendsMapGroup.get(qq.userName);
	   				var groupNode=scope.groupsMode.get(groupId);
	   				if(qq.state==net.sky.qq.entity.EState.ONLINE){
	   					groupNode.setOnlineNum(groupNode.getOnlineNum()+1);
	   					$("#"+qq.userName+"_faceImg").css("opacity","1");
	   				}else{
	   					groupNode.setOnlineNum(groupNode.getOnlineNum()-1);
	   					$("#"+qq.userName+"_faceImg").css("opacity","0.4");
	   				}
	   				$("#group_name_"+groupId).text(groupNode.toString());
	   			}
	   		});
	   }
	});
	
	
	$.Class("com.sky.qq.biz.Chat", {
		
	},{
		init:function(qqInfor){
			this.qqInfor=qqInfor;
		},
		//读取消息盒子中是否有消息
		readMesByBox:function(){
			var qqMessage=net.sky.qq.biz.MessageBox.getQqMessages().get(this.qqInfor.userName);
			if(qqMessage.getSize() > 0){
				var values=qqMessage.getList();
				for(var p in values){
					var message=values[p];
					var type=message.type;
					if(type=="qqMes"){
						message.playload.type="target";
						this.appendMes(message.playload);
					}
				}
				qqMessage.clear();
			}
		},
		//显示消息
		appendMes:function(playload){
			var $showMes=$("#chat_show_mes_"+this.qqInfor.userName);
			playload.mes=this.convertToShow(playload.mes);
			var html=$.View("/IM_Web/webqq/app/view/message.ejs",{mes:playload});
			$showMes.append(com.js.util.Encode.HtmlEncode(html));
			$showMes[0].scrollTop = $showMes[0].scrollHeight;
		},
		//发送消息
		sendMes:function(){
			var editor=$("#rich_editor_text_"+this.qqInfor.userName);
			var mes=editor.html();
			console.log(mes);
			mes=com.js.util.String.replace(mes,"<div>","</div>","\r\n???");
			mes=com.js.util.String.replace(mes,"<img src=\"/IM_Web/webqq/app/images/brow/",".gif\">","#@~???~@#");
			console.log(mes);
			var data={
				type:"qqMes",
				playload:{
					sender:net.sky.qq.entity.Owner.qq,
					target:this.qqInfor.userName,
					mes:mes,
					type:"my"
				}
			};
			qq.socket.write(data);
			data.playload.time=new Date().Format("yyyy-MM-dd hh:mm:ss");
			this.appendMes(data.playload);
			editor.empty();
		},
		//将接收字符串转换为可显示字符串
		convertToShow:function(source){
	   		var buffer =[]; 
	   		var showMes=$("#chat_show_mes_"+this.id);
	   		var i=0;
	   		var temp=source;
	   		while(i<source.length){
	   			var a=temp.indexOf("#@~");
	   			if(a!=-1){
	   				var z=temp.indexOf("~@#");
	   				if(a!=0){
	   					var insertStr=temp.substring(0,a);
	   					buffer.push(insertStr);
	   				}
	   				i+=z+3;
	   				var resource=temp.substring(a+3, z);
	   				if(resource.length > 3){
	   					buffer.push("<img src='data:image/jpg;base64,"+resource+"' />");
	   				}else{
	   					buffer.push("<img src='/IM_Web/webqq/app/images/brow/"+resource+".gif' />");
	   				}
	   				temp=temp.substring(z+3);
	   			}else{
	   				break;
	   			}
	   		}
	   		if(i < source.length){
	   			buffer.push(temp);
	   		}
	   		return buffer.join("");
	   }
	});
	
	//FriendGroup业务逻辑类
	$.Class("com.sky.qq.biz.FriendGroupBiz", {
	
	}, {
		init : function() {
			
		},
		//获取好友分组
		getGroup:function(){
			var data={
				type:"friendGroup",
				playload:{
					type:"group"
				}
			};
			qq.socket.write(data);
		},
		//获取好友列表
		getFriend:function(groupId){
			var data={
				type:"friendGroup",
				playload:{
					type:"friend",
					groupId:groupId
				}
			};
			qq.socket.write(data);
		}
	});
	
	$.Class("com.sky.qq.biz.SearchFriendBiz",{
		
	},{
		searchFriend:function(pageIndex){
			var data={
				type:"searchFriend",
				playload:{
					pageIndex:pageIndex
				}
			};
			qq.socket.write(data);
		}
	});
	
	//命令处理器
	$.Class("com.sky.qq.handler.CommandHandler",{
			handlers:new com.js.util.HashMap(),
			//注册处理器
			registerHandler:function(handler){
				com.sky.qq.handler.CommandHandler.handlers.put(handler.type,handler);
			},
			//执行处理器
			execute:function(data){
				com.sky.qq.handler.CommandHandler.handlers.get(data.type).run(data);
			}			
		},{
			
		}
	);
});
