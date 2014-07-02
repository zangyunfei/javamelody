package com.zyf.web;

import org.junit.Test;

import com.zyf.util.HttpClientUtil;

public class HelloRestWebTest {
	private String url = "http://127.0.0.1/echo/";

	@Test
	public void testMonitor() {
		String resMes = null;

		for (int i = 0; i < 1000; i++) {
			try {
				resMes = HttpClientUtil.get(url + "zyfqfghjtime:" + i, null);
				Thread.sleep(1000);
				System.out.println(resMes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
