package com.zyf.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Controller;

import com.zyf.context.WebApplicationContext;
import com.zyf.dao.TestDAO;
import com.zyf.po.TestPO;

@Path(value = "echo")
@Controller
public class HelloRestWeb {

	@GET
	@Path(value = "{message}")
	@Produces("text/plain;charset=utf-8")
	public String echoService(@PathParam("message") String message) {
		TestPO po = new TestPO();
		po.setRemark(message);
		TestDAO testDAO = (TestDAO) WebApplicationContext.getBean("testDAO");
		testDAO.insert(po);
		return message;
	}
}
