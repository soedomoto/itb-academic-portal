package id.ac.itb.academic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class IndexService {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAllServices() {
		Map<String, Object> services = new HashMap<>();
		
		Map<String, String> absenDosen = new HashMap<>();
		absenDosen.put("listAbsensiDosen", "GET /api/absensi/dosen");
		absenDosen.put("getAbsensiDosenById", "GET /api/absensi/dosen/{id}");
		absenDosen.put("getAbsensiDosenByNip", "GET /api/absensi/dosen/nip/{nip}");
		absenDosen.put("getAbsensiDosenByTanggalKuliah", "GET /api/absensi/dosen/tanggal/{tanggal}");
		absenDosen.put("getAbsensiDosenByRuangKuliah", "GET /api/absensi/dosen/ruang/{ruang}");
		absenDosen.put("getAbsensiDosenByHariKuliah", "GET /api/absensi/dosen/hari/{hari}");
		absenDosen.put("getAbsensiDosenByHariDanSesiKuliah", "GET /api/absensi/dosen/hari/{hari}/sesi/{sesi}");
		services.put("absenDosen", absenDosen);
		
		Map<String, String> absenMahasiswa = new HashMap<>();
		absenMahasiswa.put("listAbsensiMahasiswa", "GET /api/absensi/mahasiswa");
		absenMahasiswa.put("getAbsensiMahasiswaByNim", "GET /api/absensi/mahasiswa/{nim}");
		absenMahasiswa.put("getAbsensiMahasiswaByOpsi", "GET /api/absensi/mahasiswa/opsi/{idOpsi}");
		absenMahasiswa.put("getAbsensiMahasiswaByProdi", "GET /api/absensi/mahasiswa/prodi/{idProdi}");
		absenMahasiswa.put("getAbsensiMahasiswaByFakultas", "GET /api/absensi/mahasiswa/fakultas/{idFakultas}");
		absenMahasiswa.put("getAbsensiMahasiswaByTanggalKuliah", "GET /api/absensi/mahasiswa/tanggal/{tanggal}");
		absenMahasiswa.put("getAbsensiMahasiswaByRuangKuliah", "GET /api/absensi/mahasiswa/ruang/{ruang}");
		absenMahasiswa.put("getAbsensiMahasiswaByHariKuliah", "GET /api/absensi/mahasiswa/hari/{hari}");
		absenMahasiswa.put("getAbsensiMahasiswaByHariDanSesiKuliah", "GET /api/absensi/mahasiswa/hari/{hari}/sesi/{sesi}");
		services.put("absenMahasiswa", absenMahasiswa);
		
		Map<String, Object> bahanKuliah = new HashMap<>();
		bahanKuliah.put("listAllBahanByPelaksanaan", "GET /api/class/{idPelaksanaan}/resource");
		bahanKuliah.put("getDetailByPelaksanaanAndBahan", "GET /api/class/{idPelaksanaan}/resource/{idBahan}");
		bahanKuliah.put("viewBahanInJpeg", "GET /api/class/{idPelaksanaan}/resource/{idBahan}/jpeg/{page}");
		bahanKuliah.put("viewBahanInPdf", "GET /api/class/{idPelaksanaan}/resource/{idBahan}/pdf");
		bahanKuliah.put("viewBahanInDocx", "GET /api/class/{idPelaksanaan}/resource/{idBahan}/docx");
		bahanKuliah.put("viewBahanInXlsx", "GET /api/class/{idPelaksanaan}/resource/{idBahan}/xlsx");
		bahanKuliah.put("viewBahanInPptx", "GET /api/class/{idPelaksanaan}/resource/{idBahan}/pptx");
		
		final List<String> resPostField = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
		{
			add("resfile");
			add("description");
		}};
		
		bahanKuliah.put("addBahan", new HashMap<String, Object>() {
			private static final long serialVersionUID = -5867433411268018381L;
		{
			put("method", "POST /api/class/{idPelaksanaan}/resource/add");
			put("field", resPostField);
			
		}});
		bahanKuliah.put("editBahan", new HashMap<String, Object>() {
			private static final long serialVersionUID = -5867433411268018381L;
		{
			put("method", "POST /api/class/{idPelaksanaan}/resource/{idBahan}/edit");
			put("field", resPostField);
			
		}});
		bahanKuliah.put("deleteBahan", "GET /api/class/{idPelaksanaan}/resource/{idBahan}/delete");
		services.put("bahanKuliah", bahanKuliah);
		
		Map<String, String> profilDosen = new HashMap<>();
		profilDosen.put("listAllDosenProfil", "GET /api/dosen");
		profilDosen.put("getDosenProfilById", "GET /api/dosen/{id}");
		profilDosen.put("getDosenProfilByNip", "GET /api/dosen/nip/{nip}");
		services.put("profilDosen", profilDosen);
		
		Map<String, String> profilMahasiswa = new HashMap<>();
		profilMahasiswa.put("listAllMahasiswaProfil", "GET /api/mahasiswa");
		profilMahasiswa.put("getMahasiswaByNim", "GET /api/mahasiswa/{nim}");
		profilMahasiswa.put("getMahasiswaByOpsi", "GET /api/mahasiswa/opsi/{idOpsi}");
		profilMahasiswa.put("getMahasiswaByProdi", "GET /api/mahasiswa/prodi/{idProdi}");
		profilMahasiswa.put("getMahasiswaByFakultas", "GET /api/mahasiswa/fakultas/{idFakultas}");
		services.put("profilMahasiswa", profilMahasiswa);
		
		return Response.ok(services).build();
	}

}
