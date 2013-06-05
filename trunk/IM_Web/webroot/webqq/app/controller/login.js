steal('jquery/controller', 'jquery/view/ejs').then('/IM_Web/webqq/app/view/login.ejs', function($) {

	$.Controller('qq.controll.login',
	{},
	{
	   init:function(){
	   		this.element.append("/IM_Web/webqq/app/view/login.ejs", {});
	   		this.registerHandler();
	   		qq.socket.connect();
	   		$("body").css("width",com.js.util.screen.getWidth()+"px");
	   		$("body").css("height",com.js.util.screen.getHeight()+"px");
	   		com.js.util.screen.setLocation("#login",380,247,"Center");
	   },
	   //注册处理器
	   registerHandler:function(){
	   		var scope=this;
	   		com.sky.qq.handler.CommandHandler.registerHandler({
	   			type:"login",
	   			run:function(data){
	   				if(data.playload.login){
	   					net.sky.qq.entity.Owner.qq=$("#txt_userName").val();
	   					new com.sky.qq.ui.Main($("#container"),data.playload.qqInfo);
	   					scope.destroy();
	   					$("#login").remove();
	   				}else{
	   					alert("登陆失败");
	   				}
	   			}
	   		});
	   },
	   //事件注册
	   "#btn_login click":function(){
	   		qq.socket.write({
	   			type:"login",
	   			playload:{
	   				userName:$("#txt_userName").val(),
	   				password:$("#txt_password").val()
	   			}
	   		});
	   } 
	});
});