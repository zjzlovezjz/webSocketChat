<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>聊天页面</title>
   <script type="text/javascript" src="jquery-1.4.4.min.js"></script>
   <script type="text/javascript">
       var username = '${sessionScope.username}';
       //进入聊天页面，就打开socket通道
	   var ws;   //一个ws对象就是一个通信管道！！
       var target = "ws://localhost:8080/chat/chatSocket?username="+username;
       window.onload = function(){
	           if('WebSocket' in window){
	    	       ws = new WebSocket(target);
	           }else if('MozWebSocket' in window){
	    	       ws = new MozWebSocket(target);
	           }else{
	    	       alert("WebSocket is not supported by this browser");
	    	       return;
	           }     
	           
	           ws.onmessage = function(event){
	        	   eval("var msg = "+ event.data+";")
	        	   console.info(msg);
	        	   if(undefined != msg.welcome)
				   $("#content").append(msg.welcome);  
	        	   
	        	   if(undefined != msg.usernames){
	        		   $("#userList").html("");   //刷新列表前先清空
	        		   $(msg.usernames).each(function(){
						    $("#userList").append("<input type=checkbox value = '"+this+"' />" +this+"<br/>")
					   });
	        	   }
	        	   
	        	   if(undefined != content){
	        		   $("#content").append(msg.content);  
	        	   }
			   }
       }
      
       function subSend() {
    	   
    	   var ss = $("#userList :checked");
    	   var val = $("#msg").val();  //发送给服务器，在服务器里面是自动装配的
    	   console.info(ss.size());
    	   var obj = null;
    	   if(ss.size() == 0){
    		      obj={
		    		   msg:val,
		    		   type:1         //1广播，2单聊
		       }
    	   }else{
    		   var to = $("#userList :checked").val();
    		      obj={
		    		   to:to,
		    		   msg:val,
		    		   type:2         //1广播，2单聊
		       }
    	   }
    	   var str = JSON.stringify(obj);
    	   ws.send(str);  
    	   $("#msg").val("");
    	   /*
		       var val = $("#msg").val();  //发送给服务器，在服务器里面是自动装配的
		       var to = $("#userList :checked").val();
		       $("#msg").val("");  
		       var obj={
		    		   to:to,
		    		   msg:val,
		    		   type:1         //1广播，2单聊
		       }
		       ws.send(val);*/
	   }
   
   </script>
</head>
<body>
   <div id="container" style="border: 1px solid black; width: 400px;height: 400px;float: left;">
         <div id="content" style="height: 350px">
           
         </div>
         <div style="border-top: 1px solid black; width: 400px;height: 50px;">
              <input id="msg"/><button onclick="subSend();">send</button>
              
         </div>
          
   </div>
   <div id="userList" style="border: 1px solid black; width: 100px;height: 400px;float: left;">
     
   </div>

</body>
</html>