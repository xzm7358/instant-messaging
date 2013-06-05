steal('jquery/controller', 'jquery/view/ejs').then('/IM_Web/webqq/app/view/brow.ejs', function($) {

	$.Controller('com.sky.qq.ui.Brow',
	{},
	{
	   init:function(element,proc,callBack){
	   		this.id=proc.id;
	   		this.callBack=callBack;
	   		var html=$.View("/IM_Web/webqq/app/view/brow.ejs", {id:this.id});
	   		element.append(html);
	   		this.show(proc);
	   },
	   show:function(proc){
	   		var browPanel=$("#brows_panel_"+proc.id);
	   		browPanel.css("left",proc.x);
	   		browPanel.css("top",proc.y);
	   		browPanel.css("z-index",++window.z);
	   		browPanel.css("display","block");
	   },
	   dispose:function(){
	   		this.destroy();
	   		$("#brows_panel_"+this.id).remove();
	   },
	   "a click":function(el){
	   		this.callBack(el.attr("tag"))
	   }
	});
});