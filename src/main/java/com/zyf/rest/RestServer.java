/**
 * 
 */
package com.zyf.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

public class RestServer {
	private static Log log = LogFactory.getLog(RestServer.class);

	private static String context = "/";
	private static int port = 80;

	/**
	 * 启动查询接口服务器
	 */
	public static void start() {
		final Server server = new Server(port); // jetty server
		/** 自定义 servlet */
		ServletHolder servlet = new ServletHolder(HttpServletDispatcher.class);
		servlet.setInitParameter("javax.ws.rs.Application",
				RestApplication.class.getName());
		// resteasyServlet.setInitOrder(0); 加载优先级

		ContextHandler contextHandler = new ContextHandler(context);
		// ch.setClassLoader(Thread.currentThread().getContextClassLoader());
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(servlet, "/*");
		contextHandler.setHandler(handler);
		server.addHandler(contextHandler);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				log.info("FeedRest server is down!");
				try {
					server.stop();
				} catch (Exception e) {
					// TODO Auto-generated catch block
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
