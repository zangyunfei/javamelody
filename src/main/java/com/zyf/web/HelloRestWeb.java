package com.zyf.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zyf.dao.TestDAO;
import com.zyf.po.TestPO;

@Path(value = "echo")
@Component
public class HelloRestWeb {
	@Autowired
	private TestDAO testDAO;

	@GET
	@Path(value = "{message}")
	@Produces("text/plain;charset=utf-8")
	public String echoService(@PathParam("message") String message) {
		TestPO po = new TestPO();
		po.setRemark(message);
		testDAO.insert(po);
		return message;
	}
}
