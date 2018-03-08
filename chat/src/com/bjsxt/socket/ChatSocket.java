package com.bjsxt.socket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bjsxt.vo.ContentVo;
import com.bjsxt.vo.Message;
import com.google.gson.Gson;


@ServerEndpoint("/chatSocket")
public class ChatSocket {

	private String username;
	private static List<Session> sessions = new ArrayList<Session>();
	private static Map<String, Session> map = new HashMap<String, Session>();
	private static List<String> names = new ArrayList<String>();
	
	@OnOpen
	public void open(Session session) throws UnsupportedEncodingException {
		//当前的webSocket的session不是servlet中的session
		String queryString = session.getQueryString();
		
		System.out.println(queryString);

		username = queryString.split("=")[1]; 
		
		this.names.add(username);      //所有进来的人都会进这个方法，这样就保存好了所有人的名字
		this.sessions.add(session);    //所有人产生的session都放进这个集合里面，因为只要有人打开，就会经过这个open方法
		this.map.put(this.username, session);//把当前的用户和他对应的session绑定起来
		
		String msg = "欢迎"+this.username +"进入聊天室!!<br/>";
		
		Message message = new Message();
		message.setWelcome(msg);
		message.setUsernames(this.names);
		
		this.broadcast(sessions, message.toJson());
	}
	
	public void broadcast(List<Session> ss,String msg){
		//遍历每条管道广播
		for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
			Session session = (Session) iterator.next();
			try {
				session.getBasicRemote().sendText(msg);  //像每条管道发，就是广播
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	//谁关闭谁进入这个方法，所以，就把谁给清理掉就行,也是广播
	@OnClose
	public void close(Session session){
		 this.sessions.remove(session);
		 this.names.remove(this.username);
		 
		 String msg = "欢送"+this.username +"退出聊天室!!<br/>";
			
		 Message message = new Message();
		 message.setWelcome(msg);
		 message.setUsernames(this.names);
		 
		 broadcast(this.sessions, message.toJson());
	}
	
	private static Gson gson = new Gson();
	
	@OnMessage
	public void message(Session session,String json) {  //对应前台传过来的json,在这里是自动装配的
		 ContentVo vo = gson.fromJson(json, ContentVo.class);
		 if(vo.getType() == 1){
			  //广播
			 Message message = new Message();
			 message.setContent(this.username, vo.getMsg());
			 broadcast(sessions, message.toJson());	 
		 }else{
			  //单聊
			  //根据username如果可以找到对应这个username的session对象就可以实现单聊
			  String to = vo.getTo();
			  Session to_session = this.map.get(to);
			  
			  Message message = new Message();
			  message.setContent(this.username, "<font color=red >私聊："+vo.getMsg()+"</font>");
			  
			  try {
				//发送单聊的内容
				to_session.getBasicRemote().sendText(message.toJson());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		 }
	}
	
	
	
	
	
	
	
	
	
	
	
}
