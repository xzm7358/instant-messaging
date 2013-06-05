steal("jquery/model", function($) {
	
	var handlers=new com.js.util.HashMap();
	
	//命令处理器
	$.Model("com.sky.qq.handler.CommandHandler",{
			//注册处理器
			registerHandler:function(handler){
				handlers.put(handler.type,handler);
			},
			//执行处理器
			execute:function(data){
				handlers.get(data.type).run(data);
			}			
		},{
			
		}
	);
});
