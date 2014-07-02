/**
 * 
 */
package com.zyf.context;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServiceContext {

	private static ApplicationContext context;

	static {
		getCtx();
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		ServiceContext.context = context;
	}

	/**
	 * 
	 * @return
	 */
	public static PropertyPlaceholderConfigurer getConfigProperties() {
		if (context != null) {
			return (PropertyPlaceholderConfigurer) context
					.getBean("placeholderConfig");
		}
		return null;
	}

	private static ApplicationContext getCtx() {
		if (context == null) {
			context = new ClassPathXmlApplicationContext(
					"classpath*:spring/spring-app.xml");
		}
		return context;
	}
}
