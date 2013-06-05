steal( 
	  './css/body.css',
	  './css/login.css',
	  './css/main.css',
	  './css/chat.css',
	  './css/brow.css',
	  './css/searchFriend.css',
	  
	  
	  './models/entity.js',
	  './models/socket.js',
	  './models/biz.js',
	  
	  
	  './controller/login.js',
	  './controller/main.js',
	  './controller/chat.js',
	  './controller/brow.js',
	  './controller/searchFriend.js'
).then(function() {
	$("#container").qq_controll_login();
});
