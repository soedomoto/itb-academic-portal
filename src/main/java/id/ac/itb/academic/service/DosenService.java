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
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			List<TDosen> mhses = dosenDao.queryForAll();
			if(mhses.size() == 0) throw new TeacherNotFoundException
				(String.format("Tidak ada dosen terdaftar. Silahkan input data terlebih dulu"));
			
			success = true;
			response.put("result", mhses);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (TeacherNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			TDosen mhs = dosenDao.queryForId(id);
			if(mhs == null) throw new TeacherNotFoundException
				(String.format("Tidak ada dosen #{id} terdaftar", id));
			
			success = true;
			response.put("result", mhs);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (TeacherNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("nip/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByNip(@PathParam("id") final String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final List<TDosen> currDosens = dosenDao.queryForMatching(new TDosen() {{
				setNip(id);
			}});
			
			if(currDosens.size() == 0) throw new TeacherNotFoundException
				(String.format("Dosen nip %s tidak ditemukan", id));
			
			success = true;
			response.put("result", currDosens.get(0));
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (TeacherNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}

}
