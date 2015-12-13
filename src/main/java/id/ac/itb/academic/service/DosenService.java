package id.ac.itb.academic.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.j256.ormlite.dao.Dao;

import id.ac.itb.academic.model.TDosen;
import id.ac.itb.academic.service.ServiceException.TeacherNotFoundException;

@Path("api/dosen")
public class DosenService {
	private Logger LOG = LoggerFactory.getLogger(DosenService.class);
	
	@Autowired
	private Dao<TDosen, Object> dosenDao;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		try {
			List<TDosen> mhses = dosenDao.queryForAll();
			if(mhses.size() == 0) throw new TeacherNotFoundException
				(String.format("Tidak ada dosen terdaftar. Silahkan input data terlebih dulu"));
			
			return Response.ok(mhses).build();
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		} catch (TeacherNotFoundException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return Response.status(Status.NOT_FOUND).entity(response).build();
		}
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") String id) {
		try {
			TDosen mhs = dosenDao.queryForId(id);
			if(mhs == null) throw new TeacherNotFoundException
				(String.format("Tidak ada mahasiswa #{id} terdaftar", id));
			
			return Response.ok(mhs).build();
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		} catch (TeacherNotFoundException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return Response.status(Status.NOT_FOUND).entity(response).build();
		}
	}

}
