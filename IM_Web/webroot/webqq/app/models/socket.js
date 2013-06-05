steal("jquery/model", function($) {
	var ws=null;
	
	$.Model("qq.socket", {
		//连接服务器
		connect : function() {
			var url = "ws://127.0.0.1:8080/IM_Web/webqq.ws";
			if ('WebSocket' in window) {
				ws = new WebSocket(url);
			} else if ('MozWebSocket' in window) {
				ws = new MozWebSocket(url);
			} else {
				alert('Your browser Unsupported WebSocket!');
				return;
			}
			ws.onmessage = function(event) {
				console.log(event.data);
				com.sky.qq.handler.CommandHandler.execute(JSON.parse(event.data));
            };
		},
		//关闭连接
		close:function(){
			ws.close();
		},
		//写入数据
		write:function(data){
			ws.send(JSON.stringify(data));
		},
		//连接文件传输服务器
		connectFileTrans:function(){
			/*
			var fileTransWs;
						var fileTransUrl="ws://127.0.0.1:8080/IM_Web/webqq.ws?type=byte";
						if ('WebSocket' in window) {
							fileTransWs = new WebSocket(fileTransUrl);
							fileTransWs.binaryType = "arraybuffer";
						} else if ('MozWebSocket' in window) {
							fileTransWs = new MozWebSocket(fileTransUrl);
							fileTransWs.binaryType = "arraybuffer";
						} else {
							alert('Your browser Unsupported WebSocket!');
							return;
						}
						fileTransWs.onmessage=function(event){
							console.log("鎺ユ敹鏁版嵁");
							// var ii=event.data;
							// var aar=new Uint8Array(ii);
							// alert(String.fromCharCode.apply(null, aar));
							// var fr=new FileReader();
							// var tempDB=fr.readAsBinaryString(ii)
							// var iii=10;						};*/
			
			var worker = new Worker("models/download.js"); 
			worker.postMessage();
			worker.onmessage=function(event){
				//console.log(event.data);
			}
			//worker.postMessage("welcome");
		}
	}, {
		init : function() {

		}
	});

});
