package com.zyf.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.springframework.context.ApplicationContext;

import com.zyf.context.ServiceContext;

public class RestApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();

	public RestApplication() {
		ApplicationContext ctx = ServiceContext.getContext();
		singletons.add(ctx.getBean("helloRestWeb"));
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
