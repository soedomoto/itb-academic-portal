package id.ac.itb.academic.service;

import java.sql.SQLException;
import java.text.ParseException;
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

import id.ac.itb.academic.model.TAbsensiDosen;
import id.ac.itb.academic.model.TDosen;
import id.ac.itb.academic.model.TJadwalKuliah;
import id.ac.itb.academic.model.TPelaksanaanKuliah;
import id.ac.itb.academic.service.ServiceException.ClassNotFoundException;
import id.ac.itb.academic.service.ServiceException.PresenceNotFoundException;
import id.ac.itb.academic.service.ServiceException.RoomNotFoundException;
import id.ac.itb.academic.service.ServiceException.ScheduleNotFoundException;
import id.ac.itb.academic.service.ServiceException.TeacherNotFoundException;

@Path("api/absensi/dosen")
public class AbsensiDosenService {
	private Logger LOG = LoggerFactory.getLogger(AbsensiDosenService.class);
	
	@Autowired
	private Dao<TAbsensiDosen, Object> absensiDosenDao;
	@Autowired
	private Dao<TDosen, Object> dosenDao;
	@Autowired
	private Dao<TPelaksanaanKuliah, Object> pelaksanaanKuliahDao;
	@Autowired
	private Dao<TJadwalKuliah, Object> jadwalKuliahDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		try {
			List<TAbsensiDosen> absens = absensiDosenDao.queryForAll();
			if(absens.size() == 0) throw new PresenceNotFoundException
			("Tidak ada absensi dosen terekam. Silahkan input data terlebih dulu");
			
			return Response.ok(absens).build();
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		} catch (PresenceNotFoundException e) {
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
			final TDosen currDosen = dosenDao.queryForId(id);
			if(currDosen == null) throw new TeacherNotFoundException
				(String.format("Dosen #%s tidak ditemukan", id));
			
			List<TAbsensiDosen> absens = absensiDosenDao.queryForMatching(new TAbsensiDosen() {{
				setDosen(currDosen);
			}});
			return Response.ok(absens).build();
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
	@Path("nip/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByNip(@PathParam("id") final String id) {
		try {
			final List<TDosen> currDosens = dosenDao.queryForMatching(new TDosen() {{
				setNip(id);
			}});
			
			if(currDosens.size() == 0) throw new TeacherNotFoundException
				(String.format("Dosen nip %s tidak ditemukan", id));
			
			List<TAbsensiDosen> absens = absensiDosenDao.queryForMatching(new TAbsensiDosen() {{
				setDosen(currDosens.get(0));
			}});
			return Response.ok(absens).build();
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
	@Path("tanggal/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByDate(@PathParam("id") final String id) {
		try {
			final List<TPelaksanaanKuliah> currKuliahs = pelaksanaanKuliahDao.queryForMatching(new TPelaksanaanKuliah() {{
				setFormatedTanggal(id);
			}});
			
			if(currKuliahs.size() == 0) throw new ClassNotFoundException
				(String.format("Pelaksanaan kuliah tanggal %s tidak ditemukan. "
				+ "Cek kembali format tanggal yang diisikan. Gunakan format : dd-mm-yyyy", id));
			
			List<TAbsensiDosen> absens = absensiDosenDao.queryForMatching(new TAbsensiDosen() {{
				setPelaksanaan(currKuliahs.get(0));
			}});
			return Response.ok(absens).build();
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		} catch (ParseException e) {
			LOG.error(e.getMessage());
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Kemungkinan format tanggal salah. Gunakan format : dd-mm-yyyy");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		} catch (ClassNotFoundException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return Response.status(Status.NOT_FOUND).entity(response).build();
		}
	}
	
	@GET
	@Path("ruang/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByRoom(@PathParam("id") final String id) {
		try {
			final List<TPelaksanaanKuliah> currKuliahs = pelaksanaanKuliahDao.queryForMatching(new TPelaksanaanKuliah() {{
				setRuang(id);
			}});
			
			if(currKuliahs.size() == 0) throw new RoomNotFoundException
				(String.format("Pelaksanaan kuliah di ruang %s tidak ditemukan. "
				+ "Cek kembali isian ruang", id));
			List<TAbsensiDosen> absens = absensiDosenDao.queryForMatching(new TAbsensiDosen() {{
				setPelaksanaan(currKuliahs.get(0));
			}});
			return Response.ok(absens).build();
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		} catch (RoomNotFoundException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return Response.status(Status.NOT_FOUND).entity(response).build();
		} 
	}
	
	@GET
	@Path("hari/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByDay(@PathParam("id") final String id) {
		try {
			final List<TPelaksanaanKuliah> currKuliahs = pelaksanaanKuliahDao.queryForMatching(new TPelaksanaanKuliah() {{
				List<TJadwalKuliah> jadwals = jadwalKuliahDao.queryForMatching(new TJadwalKuliah() {{
					setHari(id);
				}});
				
				if(jadwals.size() == 0) throw new ScheduleNotFoundException
					(String.format("Pelaksanaan kuliah di hari %s tidak ditemukan. "
					+ "Cek kembali isian hari", id));
				setJadwal(jadwals.get(0));
			}});
			
			if(currKuliahs.size() == 0) throw new ScheduleNotFoundException
				(String.format("Pelaksanaan kuliah di hari %s tidak ditemukan. "
				+ "Cek kembali isian hari", id));
			List<TAbsensiDosen> absens = absensiDosenDao.queryForMatching(new TAbsensiDosen() {{
				setPelaksanaan(currKuliahs.get(0));
			}});
			return Response.ok(absens).build();
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		} catch (ScheduleNotFoundException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return Response.status(Status.NOT_FOUND).entity(response).build();
		} 
	}
	
	@GET
	@Path("hari/{id}/sesi/{sess}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByDayandSession(@PathParam("id") final String id, @PathParam("sess") final Integer sess) {
		try {
			final List<TPelaksanaanKuliah> currKuliahs = pelaksanaanKuliahDao.queryForMatching(new TPelaksanaanKuliah() {{
				List<TJadwalKuliah> jadwals = jadwalKuliahDao.queryForMatching(new TJadwalKuliah() {{
					setHari(id);
					setSesi(sess);
				}});
				
				if(jadwals.size() == 0) throw new ScheduleNotFoundException
					(String.format("Pelaksanaan kuliah di hari %s tidak ditemukan. "
					+ "Cek kembali isian hari", id));
				setJadwal(jadwals.get(0));
			}});
			
			if(currKuliahs.size() == 0) throw new ScheduleNotFoundException
				(String.format("Pelaksanaan kuliah di hari %s tidak ditemukan. "
				+ "Cek kembali isian hari", id));
			
			List<TAbsensiDosen> absens = absensiDosenDao.queryForMatching(new TAbsensiDosen() {{
				setPelaksanaan(currKuliahs.get(0));
			}});
			return Response.ok(absens).build();
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		} catch (ScheduleNotFoundException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("error", e.getMessage());
			return Response.status(Status.NOT_FOUND).entity(response).build();
		}
	}
}