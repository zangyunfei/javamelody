package com.zyf.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.zyf.context.ServiceContext;

public class RestApplication extends Application {
	private static final Log log = LogFactory.getLog(RestApplication.class);
	private Set<Object> singletons = new HashSet<Object>();

	public RestApplication() {
		ApplicationContext ctx = ServiceContext.getContext();
		singletons.add(ctx.getBean("helloRestWeb"));
		String sList = "";
		for (Object o : singletons) {
			sList += o.getClass().getSimpleName() + ",";
		}
		log.info("add Rest Interface:[" + sList + "]");
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
