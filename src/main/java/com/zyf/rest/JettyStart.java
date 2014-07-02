package com.zyf.rest;

public class JettyStart {

	public static void main(String[] args) throws Exception {

		JavaMelodyMonitorServer javaMelodyMonitorServer = new JavaMelodyMonitorServer();
		javaMelodyMonitorServer.run();
		/*
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() { RestServer.start(); } }).start()
		 */;
	}
}
