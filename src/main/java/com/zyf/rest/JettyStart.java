package com.zyf.rest;

public class JettyStart {

	public static void main(String[] args) throws Exception {
		new JavaMelodyMonitorServer("test", "127.0.0.1", 80);

		/*
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() { RestServer.start(); } }).start()
		 */;
	}
}
