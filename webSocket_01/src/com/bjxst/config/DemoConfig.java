package com.bjxst.config;

import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class DemoConfig implements ServerApplicationConfig {

	//ע�ⷽʽ������
	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scaned) {
		System.out.println("Config����"+ scaned.size());
		
		//����,�������ṩ�˹��˵�����
		return scaned;
	}

	//�ӿڷ�ʽ����������Ȼʹ��ע�⣬���������ʱ����ע
	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> arg0) {
		// TODO Auto-generated method stub
		
		return null;
	}

}
