package com.bjsxt.socket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")    //socket��������
public class EchoSocket {
	
	public EchoSocket() {
		System.out.println("EchoSocket.EchoSocket()");
	}

	@OnOpen
	public void open(Session session){
		//һ��WebSocket��Session����һ��ͨ�ŻỰ��
		System.out.println("sessionid"+session.getId());
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("session:"+session.getId()+"ͨ���ر��ˡ���������");
	}
	
	@OnMessage            //�����msg��ͨ������ע��ע�������
	public void message(Session session,String msg) throws Exception {
		System.out.println("�ͻ���˵"+msg);
		session.getBasicRemote().sendText("������˵ nihao too");
	}
}
