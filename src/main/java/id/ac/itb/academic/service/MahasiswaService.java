package id.ac.itb.academic.service;

import java.sql.SQLException;
import java.util.ArrayList;
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

import id.ac.itb.academic.model.TFakultas;
import id.ac.itb.academic.model.TMahasiswa;
import id.ac.itb.academic.model.TOpsi;
import id.ac.itb.academic.model.TProdi;
import id.ac.itb.academic.service.ServiceException.FacultyNotFoundException;
import id.ac.itb.academic.service.ServiceException.OptionNotFoundException;
import id.ac.itb.academic.service.ServiceException.ProgramNotFoundException;
import id.ac.itb.academic.service.ServiceException.StudentNotFoundException;

@Path("api/mahasiswa")
public class MahasiswaService {
	private Logger LOG = LoggerFactory.getLogger(MahasiswaService.class);
	
	@Autowired
	private Dao<TMahasiswa, Object> mahasiswaDao;
	@Autowired
	private Dao<TOpsi, Object> opsiDao;
	@Autowired
	private Dao<TProdi, Object> prodiDao;
	@Autowired
	private Dao<TFakultas, Object> fakultasDao;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			List<TMahasiswa> mhses = mahasiswaDao.queryForAll();
			if(mhses.size() == 0) throw new StudentNotFoundException
				(String.format("Tidak ada mahasiswa terdaftar. Silahkan input data terlebih dulu"));
			
			success = true;
			response.put("result", mhses);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (StudentNotFoundException e) {
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
			TMahasiswa mhs = mahasiswaDao.queryForId(id);
			if(mhs == null) throw new StudentNotFoundException
				(String.format("Tidak ada mahasiswa #{id} terdaftar", id));
			
			success = true;
			response.put("result", mhs);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (StudentNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("opsi/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByOption(@PathParam("id") String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final TOpsi opsi = opsiDao.queryForId(id);
			if(opsi == null) throw new OptionNotFoundException
				(String.format("Mata kuliah Opsi #%s tidak ditemukan", id));
			
			final List<TMahasiswa> currMhs = mahasiswaDao.queryForMatching(new TMahasiswa() {{
				setOpsi(opsi);
			}});
			if(currMhs == null) throw new StudentNotFoundException
				(String.format("Tidak ada mahasiswa yang terdaftar di Opsi #%s", id));
			
			success = true;
			response.put("result", currMhs);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (OptionNotFoundException | StudentNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("prodi/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByProgram(@PathParam("id") String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final TProdi prodi = prodiDao.queryForId(id);
			if(prodi == null) throw new ProgramNotFoundException
				(String.format("Program Studi #%s tidak ditemukan", id));
			
			final List<TMahasiswa> currMhs = mahasiswaDao.queryForMatching(new TMahasiswa() {{
				setProdi(prodi);
			}});
			if(currMhs == null) throw new StudentNotFoundException
				(String.format("Tidak ada mahasiswa yang terdaftar di Program Studi #%s", id));
			
			success = true;
			response.put("result", currMhs);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ProgramNotFoundException | StudentNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("fakultas/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByFaculty(@PathParam("id") String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final TFakultas fakultas = fakultasDao.queryForId(id);
			if(fakultas == null) throw new FacultyNotFoundException
				(String.format("Fakultas #%s tidak ditemukan", id));
			
			List<TProdi> prodis = prodiDao.queryForMatching(new TProdi() {{
				setFakultas(fakultas);
			}});
			if(prodis.size() == 0) throw new ProgramNotFoundException
				(String.format("Program studi pada Fakultas #%s tidak ditemukan", id));
			
			List<TMahasiswa> allMhses = new ArrayList<>();
			for(final TProdi prodi : prodis) {
				final List<TMahasiswa> currMhs = mahasiswaDao.queryForMatching(new TMahasiswa() {{
					setProdi(prodi);
				}});
				
				allMhses.addAll(currMhs);
			}
			
			if(allMhses.size() == 0) throw new StudentNotFoundException
				(String.format("Tidak ada mahasiswa yang terdaftar di Program Studi #%s", id));
			
			success = true;
			response.put("result", allMhses);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (FacultyNotFoundException | StudentNotFoundException | ProgramNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}

}
