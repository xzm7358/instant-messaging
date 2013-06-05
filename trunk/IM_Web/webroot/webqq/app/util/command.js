//模拟Java中的ArrayList集合
com.js.util.ArrayList = function() {
	var aList = [];
	this.getList = function() {
		return aList;
	}
	this.clear = function() {
		aList = [];
	}
	this.setData = function(data) {
		aList = data;
	}
}

com.js.util.ArrayList.prototype = {
	getSize : function() {
		return (function(score) {
			return score.getList().length;
		})(this);
	},
	add : function(obj) {
		this.getList().push(obj);
	},
	get : function(i) {
		return this.getList()[i];
	},
	clear : function() {
		this.aList = [];
	},
	removeAt : function(i) {
		var length = this.getList().length;
		if (i < 0 || i >= length) {
			throw new Error("ArrayIndexOutOfBoundsException");
		} else {
			this.setData(this.getList().slice(0, i).concat(this.getList().slice(i + 1)));
		}
	}
};

	//模拟Java中的HashMap
com.js.util.HashMap = function() {
	var data = [];
	this.size = 0;
	this.getMap = function() {
		return data;
	}
	this.clear = function() {
		data = [];
	}
}
com.js.util.HashMap.prototype = {
	put : function(key, value) {
		this.getMap()[key] = value;
		this.size++;
	},
	get : function(key) {
		var result = null;
		if (this.containsKey(key)) {
			result = this.getMap()[key];
		}
		return result;
	},
	containsKey : function(key) {
		return this.getMap().hasOwnProperty(key);
	},
	isEmpty : function() {
		return (this.getMap().length > 0) ? (false) : (true);
	},
	getSize : function() {
		return this.size;
	},
	keySet : function() {
		var result = [];
		for (var p in this.getMap()) {
			result.push(p);
		}
		return result;
	},
	values : function() {
		var result = [];
		var keys = this.keySet();
		for (var i in keys) {
			result.push(this.get(keys[i]));
		}
		return result;
	},
	remove : function(key) {
		var result = false;
		if (this.containsKey(key)) {
			result = true;
			delete this.getMap()[key];
			this.size--;
		}
		return result;
	}
}

com.js.util.screen=function(){
	
}

//屏幕工具类
com.js.util.screen.getWidth=function(){
	return document.body.scrollWidth;
};

com.js.util.screen.getHeight=function(){
	return document.body.scrollHeight;
}
com.js.util.screen.setLocation=function(id,width,height,align){
	var obj=$(id);
	var left=0;
	var top=0;
	if(align=="Center"){
		left=com.js.util.screen.getWidth() / 2 -  width / 2;
		top=com.js.util.screen.getHeight() / 2 - height / 2;
	}else if(align=="Right"){
		var swidth=com.js.util.screen.getWidth();
		var sheight=com.js.util.screen.getHeight();
		left = swidth - swidth * 0.05 - width;
		top = sheight * 0.1;
	}
	obj.css("left",left+"px");
	obj.css("top",top+"px");
}

com.js.util.Event = function() {

}

com.js.util.Event.darg=function(dragId,moveId)
{
	var dragObj=document.getElementById(dragId);
	var moveObj=document.getElementById(moveId);
	
   /*
    dragObj.onselectstart = function()
       {
           return(false);
       };*/
   

    dragObj.onmousedown = function(e) {
    	//this.style.cursor = "move";
    	console.log("onmousedown..........");
        e = e||window.event;
        var x=e.layerX||e.offsetX;
        var y=e.layerY||e.offsetY;
        x=x-document.body.scrollLeft;
        y=y-document.body.scrollTop;
        document.onmousemove = function(e)
        {
            e=e||window.event;
            moveObj.style.left=(e.clientX-x)+"px";
            moveObj.style.top=(e.clientY-y)+"px";
        };

        document.onmouseup=function()
        {
            document.onmousemove=null;
            document.onmouseup=null;
        };
    };
}
com.js.util.Encode=function(){
	
}
com.js.util.Encode.HtmlEncode=function(str){
	
	function replaceAll(src, oldStr, newStr){
		return src.replace(new RegExp(oldStr, "g"), newStr);
	}
	
	str=replaceAll(str,"&amp;","&");
	str=replaceAll(str,"&lt;","<");
	str=replaceAll(str,"&gt;",">");
	str=replaceAll(str,"&apos;","'");
	str=replaceAll(str,"&quot;","\"");
	str=replaceAll(str,"&#39;","\"");
	//str=replaceAll(str,"\r\n","<br />");
	return str;
}

com.js.util.String=function(){
	
}
//替换分割内容
com.js.util.String.replace=function(source,separatorBegin,separatorEnd,wrapTemplate){
	var buffer =[]; 
	var i=0;
	var temp=source;
	while(i<source.length){
		var a=temp.indexOf(separatorBegin);
		if(a!=-1){
			var z=temp.indexOf(separatorEnd);	separatorEnd
			if(a!=0){
				var insertStr=temp.substring(0,a);
				buffer.push(insertStr);
			}
			i+=z+3;
			var resource=temp.substring(a+separatorBegin.length, z);
			buffer.push(wrapTemplate.replace("???",resource));
			temp=temp.substring(z+separatorEnd.length);
		}else{
			break;
		}
	}
	if(i < source.length){
		buffer.push(temp);
	}
	return buffer.join("");
}
