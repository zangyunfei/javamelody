package com.zyf.rest;

/**
 * 启动一个jetty容器，结合javamelody用于监控应用性能
 * 
 */
public interface JavaMelodyMonitorServerInterface {
	public void start(String serverName, String host, int serverPort);

}
