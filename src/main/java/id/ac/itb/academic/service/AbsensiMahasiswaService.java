package id.ac.itb.academic.service;

import java.sql.SQLException;
import java.text.ParseException;
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

import id.ac.itb.academic.model.TAbsensiMahasiswa;
import id.ac.itb.academic.model.TFakultas;
import id.ac.itb.academic.model.TJadwalKuliah;
import id.ac.itb.academic.model.TMahasiswa;
import id.ac.itb.academic.model.TOpsi;
import id.ac.itb.academic.model.TPelaksanaanKuliah;
import id.ac.itb.academic.model.TProdi;
import id.ac.itb.academic.service.ServiceException.FacultyNotFoundException;
import id.ac.itb.academic.service.ServiceException.OptionNotFoundException;
import id.ac.itb.academic.service.ServiceException.PresenceNotFoundException;
import id.ac.itb.academic.service.ServiceException.ProgramNotFoundException;
import id.ac.itb.academic.service.ServiceException.RoomNotFoundException;
import id.ac.itb.academic.service.ServiceException.ScheduleNotFoundException;
import id.ac.itb.academic.service.ServiceException.StudentNotFoundException;

@Path("api/absensi/mahasiswa")
public class AbsensiMahasiswaService {
	private Logger LOG = LoggerFactory.getLogger(AbsensiMahasiswaService.class);
	
	@Autowired
	private Dao<TAbsensiMahasiswa, Object> absensiMahasiswaDao;
	@Autowired
	private Dao<TMahasiswa, Object> mahasiswaDao;
	@Autowired
	private Dao<TPelaksanaanKuliah, Object> pelaksanaanKuliahDao;
	@Autowired
	private Dao<TJadwalKuliah, Object> jadwalKuliahDao;
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
			List<TAbsensiMahasiswa> absens = absensiMahasiswaDao.queryForAll();
			if(absens.size() == 0) throw new PresenceNotFoundException
				(String.format("Tidak ada absensi mahasiswa terekam. Silahkan input data terlebih dulu"));
			
			success = true;
			response.put("result", absens);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (PresenceNotFoundException e) {
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
			final TMahasiswa currMhs = mahasiswaDao.queryForId(id);
			if(currMhs == null) throw new StudentNotFoundException
				(String.format("Mahasiswa nim %s tidak ditemukan", id));
			
			List<TAbsensiMahasiswa> absens = absensiMahasiswaDao.queryForMatching(new TAbsensiMahasiswa() {{
				setMahasiswa(currMhs);
			}});
			
			success = true;
			response.put("result", absens);
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
			
			List<TAbsensiMahasiswa> allAbsens = new ArrayList<>();
			for(final TMahasiswa mhs : currMhs) {
				List<TAbsensiMahasiswa> absens = absensiMahasiswaDao.queryForMatching(new TAbsensiMahasiswa() {{
					setMahasiswa(mhs);
				}});
				
				allAbsens.addAll(absens);
			}
			
			success = true;
			response.put("result", allAbsens);
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
			
			List<TAbsensiMahasiswa> allAbsens = new ArrayList<>();
			for(final TMahasiswa mhs : currMhs) {
				List<TAbsensiMahasiswa> absens = absensiMahasiswaDao.queryForMatching(new TAbsensiMahasiswa() {{
					setMahasiswa(mhs);
				}});
				
				allAbsens.addAll(absens);
			}
			
			success = true;
			response.put("result", allAbsens);
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
			
			List<TAbsensiMahasiswa> allAbsens = new ArrayList<>();
			for(final TProdi prodi : prodis) {
				final List<TMahasiswa> currMhs = mahasiswaDao.queryForMatching(new TMahasiswa() {{
					setProdi(prodi);
				}});
				
				for(final TMahasiswa mhs : currMhs) {
					List<TAbsensiMahasiswa> absens = absensiMahasiswaDao.queryForMatching(new TAbsensiMahasiswa() {{
						setMahasiswa(mhs);
					}});
					
					allAbsens.addAll(absens);
				}
			}
			
			if(allAbsens.size() == 0) throw new PresenceNotFoundException
				(String.format("Tidak ada absen yang terekam di Fakultas #%s. "
						+ "Silahkan input terlebih dulu", id));
			
			success = true;
			response.put("result", allAbsens);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (FacultyNotFoundException | PresenceNotFoundException | ProgramNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("tanggal/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByDate(@PathParam("id") final String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final List<TPelaksanaanKuliah> currKuliahs = pelaksanaanKuliahDao.queryForMatching(new TPelaksanaanKuliah() {{
				setFormatedTanggal(id);
			}});
			
			if(currKuliahs.size() == 0) throw new ClassNotFoundException(String.format(
					"Pelaksanaan kuliah tanggal %s tidak ditemukan. "
					+ "Cek kembali format tanggal yang diisikan. Gunakan format : dd-mm-yyyy", id));
			
			List<TAbsensiMahasiswa> absens = absensiMahasiswaDao.queryForMatching(new TAbsensiMahasiswa() {{
				setPelaksanaan(currKuliahs.get(0));
			}});
			
			success = true;
			response.put("result", absens);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ParseException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Kemungkinan format tanggal salah. Gunakan format : dd-mm-yyyy");
		} catch (ClassNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("ruang/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByRoom(@PathParam("id") final String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final List<TPelaksanaanKuliah> currKuliahs = pelaksanaanKuliahDao.queryForMatching(new TPelaksanaanKuliah() {{
				setRuang(id);
			}});
			
			if(currKuliahs.size() == 0) throw new RoomNotFoundException
				(String.format("Pelaksanaan kuliah di ruang %s tidak ditemukan. "
						+ "Cek kembali isian ruang", id));
			
			List<TAbsensiMahasiswa> absens = absensiMahasiswaDao.queryForMatching(new TAbsensiMahasiswa() {{
				setPelaksanaan(currKuliahs.get(0));
			}});
			
			success = true;
			response.put("result", absens);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (RoomNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		} 
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("hari/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByDay(@PathParam("id") final String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
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
			
			List<TAbsensiMahasiswa> absens = absensiMahasiswaDao.queryForMatching(new TAbsensiMahasiswa() {{
				setPelaksanaan(currKuliahs.get(0));
			}});
			
			success = true;
			response.put("result", absens);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ScheduleNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		} 
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("hari/{id}/sesi/{sess}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByDayandSession(@PathParam("id") final String id, @PathParam("sess") final Integer sess) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final List<TPelaksanaanKuliah> currKuliahs = pelaksanaanKuliahDao.queryForMatching(new TPelaksanaanKuliah() {{
				List<TJadwalKuliah> jadwals = jadwalKuliahDao.queryForMatching(new TJadwalKuliah() {{
					setHari(id);
					setSesi(sess);
				}});
				
				if(jadwals.size() == 0) throw new ScheduleNotFoundException
					(String.format("Pelaksanaan kuliah di hari %s sesi %s tidak ditemukan. "
					+ "Cek kembali isian hari dan sesi", id, sess));
				
				setJadwal(jadwals.get(0));
			}});
			
			if(currKuliahs.size() == 0) throw new ScheduleNotFoundException
				(String.format("Pelaksanaan kuliah di hari %s sesi %s tidak ditemukan. "
				+ "Cek kembali isian hari dan sesi", id, sess));
			
			List<TAbsensiMahasiswa> absens = absensiMahasiswaDao.queryForMatching(new TAbsensiMahasiswa() {{
				setPelaksanaan(currKuliahs.get(0));
			}});
			
			success = true;
			response.put("result", absens);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ScheduleNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
}