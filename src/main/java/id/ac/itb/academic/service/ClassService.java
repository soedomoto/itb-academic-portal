package id.ac.itb.academic.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api/class")
public class ClassService {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listClasses() {
		return Response.ok().build();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response classInfo(@PathParam("id") String id) {
		return Response.ok().build();
	}
	
	@GET
	@Path("{id}/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addClass(@PathParam("id") String id) {
		return Response.ok().build();
	}
	
	@GET
	@Path("{id}/edit")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editClass(@PathParam("id") String id) {
		return Response.ok().build();
	}
	
	@GET
	@Path("{id}/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteClass(@PathParam("id") String id) {
		return Response.ok().build();
	}
	
}
