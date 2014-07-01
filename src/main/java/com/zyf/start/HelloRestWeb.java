package com.zyf.start;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path(value = "echo")
public class HelloRestWeb {
	@GET
	@Path(value = "{message}")
	// @Consumes("text/html")
	@Produces("text/plain;charset=utf-8")
	public String echoService(@PathParam("message") String message) {
		System.out.println(message);
		return message;
	}
}
