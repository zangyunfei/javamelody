/**
 * 
 */
package com.zyf.context;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zyf.rest.JavaMelodyMonitorServer;

public class WebApplicationContext {

	private static ApplicationContext context;

	static {
		getCtx();
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		WebApplicationContext.context = context;
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
			context = WebApplicationContextUtils
					.getWebApplicationContext(JavaMelodyMonitorServer
							.getContext().getServletContext());
		}
		return context;
	}

	public static Object getBean(String str) {
		return context.getBean(str);
	}
}
