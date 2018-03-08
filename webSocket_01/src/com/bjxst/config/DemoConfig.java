package com.bjxst.config;

import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

public class DemoConfig implements ServerApplicationConfig {

	//注解方式的启动
	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scaned) {
		System.out.println("Config……"+ scaned.size());
		
		//返回,这样子提供了过滤的作用
		return scaned;
	}

	//接口方式的启动，既然使用注解，这个方法暂时不关注
	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> arg0) {
		// TODO Auto-generated method stub
		
		return null;
	}

}
