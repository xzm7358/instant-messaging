steal('jquery/controller', 'jquery/view/ejs').then('/IM_Web/webqq/app/view/main.ejs', function($) {

	$.Controller('com.sky.qq.ui.Main',
	{},
	{
	   init:function(element,data){
	   		element.append("/IM_Web/webqq/app/view/main.ejs", {});
	   		com.js.util.screen.setLocation("#main",287,585,"Right");
	   		com.js.util.Event.darg("main_top","main");
	   		this.mainBiz=new com.sky.qq.biz.Main(data);
	   		this.mainBiz.fillData();
	   		this.mainBiz.registerFriendGroupHandler();
	   		this.mainBiz.getFriendGroup();
	   },
	   ".main_top,.qq_info_right click":function(){
	   		var obj=$("#main");
	   		if(obj.css("z-index") < window.z){
	   			obj.css("z-index",++window.z);
	   		}	   },
	   "#main_search_friend click":function(){
	   		new com.sky.qq.ui.SearchFriend($("#container"));
	   },
	   "#main_close click":function(){
		   qq.socket.close();
		   location.href="iheartradio.html";
	   }
	});
});