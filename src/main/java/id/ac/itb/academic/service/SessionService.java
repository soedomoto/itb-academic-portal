package id.ac.itb.academic.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import net.spy.memcached.MemcachedClient;

@Path("api/session")
public class SessionService {
	
	@Autowired
	private MemcachedClient memcached;
	
	@GET
	@Path("{key}")
	public Response getValue(@PathParam("key") String key) {
		return Response.ok(memcached.get(key)).build();
	}

}
