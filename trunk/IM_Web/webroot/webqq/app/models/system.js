var z = 5;
// 命名空间声明
var net = {};
net.sky = {};
net.sky.qq = {};
net.sky.qq.entity = {};
net.sky.qq.biz = {};

var com = {};
com.js = {};
com.js.util = {};

function KeyDown() {
	if ((window.event.altKey) && ((window.event.keyCode == 37) || //屏蔽   Alt+   方向键   ← 
	(window.event.keyCode == 39))) //屏蔽   Alt+   方向键   → 
	{
		event.keyCode = 0;
		event.returnValue = false;
	}
	if (event.keyCode == 116) //屏蔽   F5   刷新键 
	{
		event.keyCode = 0;
		event.returnValue = false;
	}
	return true;
}

function clickIE4() {
	if (event.button == 2) {
		return false;
	}
}

function clickNS4(e) {
	if (document.layers || document.getElementById && !document.all) {
		if (e.which == 2 || e.which == 3) {
			return false;
		}
	}
}

function OnDeny() {
	if (event.ctrlKey || event.keyCode == 78 && event.ctrlKey
			|| event.altKey || event.altKey && event.keyCode == 115) {
		return false;
	}
}

if (document.layers) {
	document.captureEvents(Event.MOUSEDOWN);
	document.onmousedown = clickNS4;
	document.onkeydown = OnDeny();
} else if (document.all && !document.getElementById) {
	document.onmousedown = clickIE4;
	document.onkeydown = OnDeny();
}

document.oncontextmenu = new Function("return false");

Date.prototype.Format = function (fmt) { 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
