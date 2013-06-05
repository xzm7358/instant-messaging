onmessage = function() {
	var fileTransWs;
	var fileTransUrl = "ws://127.0.0.1:8080/IM_Web/webqq.ws?type=byte";
	fileTransWs = new WebSocket(fileTransUrl);
		fileTransWs.binaryType = "arraybuffer";
	fileTransWs.onmessage = function(event) {
		postMessage("接收数据");
		//console.log("鎺ユ敹鏁版嵁");
		// var ii=event.data;
		// var aar=new Uint8Array(ii);
		// alert(String.fromCharCode.apply(null, aar));
		// var fr=new FileReader();
		// var tempDB=fr.readAsBinaryString(ii)
		// var iii=10;
	};
	//postMessage(Math.random());
}
