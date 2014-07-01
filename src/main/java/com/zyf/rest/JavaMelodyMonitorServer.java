package com.zyf.rest;

import java.util.EventListener;

import javax.servlet.Filter;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.thread.QueuedThreadPool;
import org.springframework.web.context.ContextLoaderListener;

/**
 * 启动一个jetty容器，结合javamelody用于监控应用性能
 * 
 */
public class JavaMelodyMonitorServer {

	private Logger log = Logger.getLogger(JavaMelodyMonitorServer.class);
	Server webServer;

	/**
	 * 
	 * @param serverName
	 *            应用名称
	 * @param host
	 *            绑定的IP地址
	 * @param serverPort
	 *            应用端口，jetty启动的端口默认会在此基础上加1000,如果配置文件有配置jetty.listen.port则配置优先
	 */
	public JavaMelodyMonitorServer(String serverName, String host,
			int serverPort) {
		init(serverName, host, serverPort);
		start();
		final JavaMelodyMonitorServer server = this;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					log.info("shutdown mointorServer:{}");
					server.stop();
				} catch (Exception e) {
					log.error("run main stop error!", e);
				}
			}

		});
	}

	private void init(String serverName, String host, int serverPort) {
		int port = serverPort;
		Connector connector = new SocketConnector();
		webServer = new Server();
		QueuedThreadPool pool = new QueuedThreadPool();
		pool.setMinThreads(3);
		pool.setMaxThreads(32);
		String server = host;
		pool.setName(serverName + "-monitor");
		pool.setDaemon(true);
		webServer.setThreadPool(pool);
		connector = new SocketConnector();

		connector.setPort(port);
		connector.setHost(server);
		connector.setMaxIdleTime(60000); // 1 min
		webServer.addConnector(connector);

		ContextHandlerCollection col = new ContextHandlerCollection();
		Context context = new Context(col, "/", Context.SESSIONS);
		ResourceHandler resourceHandler = new ResourceHandler();
		webServer.setHandlers(new Handler[] { col, resourceHandler });
		webServer.addHandler(context);
		// Set Java Melody storage Directory
		System.setProperty("javamelody.storage-directory",
				"javamelody-" + pool.getName());

		/** add filter */
		Filter monitoringFilter = new net.bull.javamelody.MonitoringFilter();
		context.addFilter(new FilterHolder(monitoringFilter), "/monitoring",
				Handler.REQUEST);

		/*
		 * Map<String, String> initParams = new HashMap<String, String>();
		 * initParams.put("contextConfigLocation",
		 * "classpath:net/bull/javamelody/monitoring-spring.xml");
		 * context.setInitParams(initParams);
		 */

		/** add listener */
		EventListener listener = new ContextLoaderListener();
		context.addEventListener(listener);

		/** add Servlet */
		ServletHolder servlet = new ServletHolder(HttpServletDispatcher.class);
		servlet.setInitParameter("javax.ws.rs.Application",
				RestApplication.class.getName());
		context.addServlet(servlet, "/*");

	}

	private void start() {
		try {

			webServer.start();
			webServer.join();
		} catch (Exception e) {
			log.error("Error starting httpserver", e);
		}
	}

	private void stop() {
		try {
			webServer.stop();
			webServer.destroy();
		} catch (Exception e) {
			log.error("Error stop httpserver", e);
		}

	}
}
