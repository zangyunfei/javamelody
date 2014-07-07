/**
 * 
 */
package com.zyf.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.stereotype.Service;

/**
 * 查询接口服务器
 * 
 * @author 冷水
 * 
 */
@Service
public class RestServer {
	private static Log log = LogFactory.getLog(RestServer.class);
	private String context = "/";

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private int port = 80;

	/**
	 * 启动查询接口服务器
	 */
	public void start() {
		final Server server = new Server(port);

		ContextHandler ch = new ContextHandler(context);
		ch.setClassLoader(this.getClass().getClassLoader());
		// ch.addEventListener(new ResteasyBootstrap());
		// ch.addEventListener(new SpringContextLoaderListener());

		// 如果用spring加载则不用scan
		// Map<String,String> contextParams=new HashMap<String,String>();
		// //自动检测restease的Annotion
		// contextParams.put("resteasy.scan", "true");
		// contextParams.put("contextConfigLocation",
		// "classpath*:spring/spring-app.xml");
		// ch.setInitParams(contextParams);

		ServletHandler servletHandler = new ServletHandler();
		ServletHolder resteasyServlet = new ServletHolder(
				HttpServletDispatcher.class);
		resteasyServlet.setInitParameter("javax.ws.rs.Application",
				RestApplication.class.getName());
		resteasyServlet.setInitOrder(0);
		servletHandler.addServletWithMapping(resteasyServlet, "/*");
		servletHandler.addFilterWithMapping(
				net.bull.javamelody.MonitoringFilter.class, "/*",
				Handler.DEFAULT);
		ch.addHandler(servletHandler);
		server.addHandler(ch);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				log.info("FeedRest server is down!");
				try {
					server.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
				server.destroy();
			}
		});

		try {
			server.start();
			server.join();
			log.info("FeedRest server is up! port:" + port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
