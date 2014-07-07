package com.zyf.rest;

import com.zyf.context.ServiceContext;

public class JettyStart {

	public static void main(String[] args) throws Exception {

		JavaMelodyMonitorServer javaMelodyMonitorServer = (JavaMelodyMonitorServer) ServiceContext
				.getContext().getBean("javaMelodyMonitorServer");
		javaMelodyMonitorServer.run();
	}
}
