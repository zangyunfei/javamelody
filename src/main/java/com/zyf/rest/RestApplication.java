package com.zyf.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.zyf.start.HelloRestWeb;

public class RestApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> set = new HashSet<Class<?>>();
		set.add(HelloRestWeb.class);
		return set;
	}
}
