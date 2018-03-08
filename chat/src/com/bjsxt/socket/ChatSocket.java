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
		//��ǰ��webSocket��session����servlet�е�session
		String queryString = session.getQueryString();
		
		System.out.println(queryString);

		username = queryString.split("=")[1]; 
		
		this.names.add(username);      //���н������˶������������������ͱ�����������˵�����
		this.sessions.add(session);    //�����˲�����session���Ž�����������棬��ΪֻҪ���˴򿪣��ͻᾭ�����open����
		this.map.put(this.username, session);//�ѵ�ǰ���û�������Ӧ��session������
		
		String msg = "��ӭ"+this.username +"����������!!<br/>";
		
		Message message = new Message();
		message.setWelcome(msg);
		message.setUsernames(this.names);
		
		this.broadcast(sessions, message.toJson());
	}
	
	public void broadcast(List<Session> ss,String msg){
		//����ÿ���ܵ��㲥
		for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
			Session session = (Session) iterator.next();
			try {
				session.getBasicRemote().sendText(msg);  //��ÿ���ܵ��������ǹ㲥
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	//˭�ر�˭����������������ԣ��Ͱ�˭�����������,Ҳ�ǹ㲥
	@OnClose
	public void close(Session session){
		 this.sessions.remove(session);
		 this.names.remove(this.username);
		 
		 String msg = "����"+this.username +"�˳�������!!<br/>";
			
		 Message message = new Message();
		 message.setWelcome(msg);
		 message.setUsernames(this.names);
		 
		 broadcast(this.sessions, message.toJson());
	}
	
	private static Gson gson = new Gson();
	
	@OnMessage
	public void message(Session session,String json) {  //��Ӧǰ̨��������json,���������Զ�װ���
		 ContentVo vo = gson.fromJson(json, ContentVo.class);
		 if(vo.getType() == 1){
			  //�㲥
			 Message message = new Message();
			 message.setContent(this.username, vo.getMsg());
			 broadcast(sessions, message.toJson());	 
		 }else{
			  //����
			  //����username��������ҵ���Ӧ���username��session����Ϳ���ʵ�ֵ���
			  String to = vo.getTo();
			  Session to_session = this.map.get(to);
			  
			  Message message = new Message();
			  message.setContent(this.username, "<font color=red >˽�ģ�"+vo.getMsg()+"</font>");
			  
			  try {
				//���͵��ĵ�����
				to_session.getBasicRemote().sendText(message.toJson());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		 }
	}
	
	
	
	
	
	
	
	
	
	
	
}
