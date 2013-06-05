steal('jquery/controller', 'jquery/view/ejs').then('/IM_Web/webqq/app/view/chat.ejs', function($) {

	$.Controller('com.sky.qq.ui.Chat',
	{
		obj:null
	},
	{
	   init:function(element,qqInfor){
	   		this.id=qqInfor.userName;
	   		var html=$.View("/IM_Web/webqq/app/view/chat.ejs", {qq:qqInfor});
	   		element.append(html);
	   		
	   		this.showToTop(this.id);
	   		com.js.util.Event.darg("chat_top_"+this.id,"chat_"+this.id);
	   		//com.js.util.Event.darg("chat_"+this.id);
	   		com.js.util.screen.setLocation("#chat_"+this.id,520,470,"Center");
	   		this.registerEvent();
	   		this.chatBiz=new com.sky.qq.biz.Chat(qqInfor);
	   		this.chatBiz.readMesByBox();
	   		com.sky.qq.ui.Chat.obj=this;
	   },
	   //注册friendGroup处理器
	   registerHandler:function(){
	   		var scope=this;
	   		com.sky.qq.handler.CommandHandler.registerHandler({
	   			type:"friendGroup",
	   			run:function(data){
	   				scope.mainBiz.execFriendGroupRsponse(data);
	   			}
	   		});
	   },
	   //初始化窗体置顶
	   showToTop:function(id){
	   		var scope=this;
	   		$("#chat_"+id).css("z-index",++window.z);
	   		$("#chat_top_"+id).click(function(){
	   			scope.closeBrow();
	   			var obj=$("#chat_"+id);
	   			if(obj.css("z-index") < window.z){
	   				obj.css("z-index",++window.z);
	   			}
	   		});
	   },
	   registerEvent:function(){
	   		var scope=this;
	   		$("#chat_"+this.id).click(function(){
	   			scope.closeBrow();
	   		});
	   		$("#chat_qq_close_"+this.id+","+"#chat_buttom_close_"+this.id).click(function(){
	   			scope.stopPropagation();
	   			scope.closeBrow();
	   			scope.dispose();
	   		});
	   		$("#chat_operate_clear_"+this.id).click(function(){
	   			scope.stopPropagation();
	   			$("#chat_show_mes_"+scope.id).empty();  			
	   		});
	   		$("#chat_buttom_send_"+this.id).click(function(){
	   			scope.chatBiz.sendMes();
	   		});
	   		$("#chat_brow_"+this.id).click(function(){
	   			scope.stopPropagation();
	   			if(scope.brow==null){
	   				console.log("position:"+$(this).position());
	   				var $chat=$("#chat_"+scope.id);
	   				var y=$chat.offset().top+$("#chat_top_"+scope.id).height()+$("#chat_show_mes_"+scope.id).height()-214-2;
	   				var x=$chat.offset().left+$(this).width() / 2 - 223;
	   				console.log("x:"+x+"---y:"+y);
	   				scope.brow=new com.sky.qq.ui.Brow($("#container"),{
	   					x:x,
	   					y:y,
	   					id:scope.id
	   				},scope.insertBrow);
	   			}
	   		});
	   },
	   update:function(message){
	   		var type=message.type;
			var playload=message.playload;
			if(type=="qqMes"){
				this.chatBiz.appendMes(playload);
			}
	   },
	   dispose:function(){
	   		net.sky.qq.biz.MessageBox.MeBox.dettach(this.id,this);
	   		this.destroy();
	   		$("#chat_"+this.id).remove();
	   },
	   getFrmType:function(){
	   		return "friendMes";
	   },
	   //关闭表情窗口
	   closeBrow:function(){
	   		if(this.brow != undefined && this.brow!=null){
	   			this.brow.dispose();
	   			this.brow=null;
	   		}
	   },
	   //阻止事件冒泡
	   stopPropagation:function(){
	 	  	var event=arguments.callee.caller.arguments[0];
   			//阻止冒泡事件
   			event.stopPropagation();
	   },
	   //插入表情
	   insertBrow:function(i){
	   		com.sky.qq.ui.Chat.obj.closeBrow();
	   		var img=$("<img src='/IM_Web/webqq/app/images/brow/"+i+".gif' />");
	   		var editor=$("#rich_editor_text_"+this.id);
	   		editor.append(img);
	   		editor.focus();
	   }
	});
});