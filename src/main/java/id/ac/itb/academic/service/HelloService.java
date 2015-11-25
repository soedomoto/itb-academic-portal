package id.ac.itb.academic.service;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("hello")
public class HelloService {
	
	@GET
	@Path("world")
	public String hello() {
		return "Hello World";
	}
	
	@GET
	@Path("time")
	@Produces(MediaType.APPLICATION_JSON)
	public Date now() {
		return new Date();
	}

}
