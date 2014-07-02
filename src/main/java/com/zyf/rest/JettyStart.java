package com.zyf.rest;

import com.zyf.context.ServiceContext;

public class JettyStart {

	public static void main(String[] args) throws Exception {
		JavaMelodyMonitorServerInterface server = (JavaMelodyMonitorServerInterface) ServiceContext
				.getContext().getBean("javaMelodyMonitorServer");
		server.start("test", "127.0.0.1", 80);

		/*
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() { RestServer.start(); } }).start()
		 */;
	}
}
