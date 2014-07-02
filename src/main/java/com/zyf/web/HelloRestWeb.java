package com.zyf.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.zyf.context.ServiceContext;
import com.zyf.dao.TestDAO;
import com.zyf.po.TestPO;

@Path(value = "echo")
public class HelloRestWeb {
	private TestDAO testDAO;

	@GET
	@Path(value = "{message}")
	@Produces("text/plain;charset=utf-8")
	public String echoService(@PathParam("message") String message) {
		TestPO po = new TestPO();
		po.setRemark(message);
		if (testDAO == null) {
			testDAO = (TestDAO) ServiceContext.getContext().getBean("testDAO");
		}
		testDAO.insert(po);
		return message;
	}
}
