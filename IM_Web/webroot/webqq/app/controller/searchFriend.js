steal('jquery/controller', 'jquery/view/ejs').then('/IM_Web/webqq/app/view/searchFriend.ejs', function($) {

	$.Controller('com.sky.qq.ui.SearchFriend',
	{
		
	},
	{
	   init:function(element){
	   		if($("#search_friend")==null){
	   			this.pageIndex=1;
	   			this.callBack=callBack;
	   			element.append("/IM_Web/webqq/app/view/searchFriend.ejs",{});
	   			com.js.util.Event.darg("search_friend_title_bar","search_friend");
	   			this.showToTop();
				var searchFriendBiz=new com.sky.qq.biz.SearchFriendBiz();
				searchFriendBiz.searchFriend(this.pageIndex);
	   		}else{
	   			this.destroy();
	   		}
	   		
	   },
	   //初始化窗体置顶
	   showToTop:function(id){
	   		$("#search_friend").css("z-index",++window.z);
	   },
	   "#search_friend_title_bar click":function(){
	   		var obj=$("#search_friend");
	   		if(obj.css("z-index") < window.z){
	   			obj.css("z-index",++window.z);
	   		}
	   },
	   "#search_friend_close click":function(){
	   		this.dispose();
	   },
	   dispose:function(){
	   		this.destroy();
	   		$("#chat_"+this.id).remove();
	   }
	});
});