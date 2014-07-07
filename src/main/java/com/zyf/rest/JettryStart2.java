package com.zyf.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zyf.context.ServiceContext;

/**
 * 支付中心 服务启动类
 * 
 * @author yuhui.tang 2013-10-27 下午3:51:36
 */
public class JettryStart2 {
	static Log log = LogFactory.getLog(JettryStart2.class);

	/**
	 * 启动类
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final RestServer restServer = (RestServer) ServiceContext.getContext()
				.getBean("restServer");
		// RestServer restServer = new RestServer();
		// restServer.setContext("/");
		// restServer.setPort(8082);

		// 俺线程启动，否则hold住
		new Thread(new Runnable() {
			@Override
			public void run() {
				restServer.start();
			}
		}).start();
	}
}
