package com.bjsxt.socket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")    //socket连接请求
public class EchoSocket {
	
	public EchoSocket() {
		System.out.println("EchoSocket.EchoSocket()");
	}

	@OnOpen
	public void open(Session session){
		//一个WebSocket的Session代表一个通信会话！
		System.out.println("sessionid"+session.getId());
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("session:"+session.getId()+"通道关闭了。。。。。");
	}
	
	@OnMessage            //这里的msg是通过依赖注入注入进来的
	public void message(Session session,String msg) throws Exception {
		System.out.println("客户端说"+msg);
		session.getBasicRemote().sendText("服务器说 nihao too");
	}
}
