package id.ac.itb.academic.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

import id.ac.itb.academic.library.ImageConverter;
import id.ac.itb.academic.model.TBahanKuliah;
import id.ac.itb.academic.model.TPelaksanaanKuliah;
import id.ac.itb.academic.service.ServiceException.ClassNotFoundException;
import id.ac.itb.academic.service.ServiceException.DocxNotFoundException;
import id.ac.itb.academic.service.ServiceException.ImageNotFoundException;
import id.ac.itb.academic.service.ServiceException.PdfNotFoundException;
import id.ac.itb.academic.service.ServiceException.PptxNotFoundException;
import id.ac.itb.academic.service.ServiceException.ResourceNotFoundException;
import id.ac.itb.academic.service.ServiceException.StreamNotFoundException;
import id.ac.itb.academic.service.ServiceException.XlsxNotFoundException;;

@Path("api/class/{classId}/resource")
public class ClassResourceService {
	private Logger LOG = LoggerFactory.getLogger(ClassResourceService.class);
	
	@Autowired
	@Qualifier("classResourceBasePath")
	private String basePath;
	@Autowired
	private Dao<TBahanKuliah, Object> bahanKuliahDao;
	@Autowired
	private Dao<TPelaksanaanKuliah, Object> pelaksanaanKuliahDao;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response listResources(@PathParam("classId") final String classId) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			if(klass == null) throw new ClassNotFoundException(String.format("Perkuliahan #%s tidak ditemukan", classId));
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(new TBahanKuliah() {{
				setPelaksanaan(klass);
			}});
			
			success = true;
			response.put("result", existingRescs);
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ClassNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceInfo(@PathParam("classId") final String classId, @PathParam("id") final Integer id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			if(klass == null) throw new ClassNotFoundException(
				String.format("Perkuliahan #%s tidak ditemukan", classId)
			);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(new TBahanKuliah() {{
				setKdBahan(id);
				setPelaksanaan(klass);
			}});
			
			if(existingRescs.size() == 0) throw new ResourceNotFoundException(
					String.format("No resource #%s found in class #%s. %s.", 
					id, classId, klass.getJadwal().getDosenMatkul().getMatkul().getNama()));
			
			success = true;
			response.put("result", existingRescs.get(0));
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ClassNotFoundException | ResourceNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		} 
		
		response.put("viewFormat", new HashMap<String, Object>() {
			private static final long serialVersionUID = 6054270051416719110L;
			{
				put("jpeg", String.format("api/class/%s/resource/%s/jpeg/{page}", classId, id));
				put("pdf", String.format("api/class/%s/resource/%s/pdf", classId, id));
				put("docx", String.format("api/class/%s/resource/%s/docx", classId, id));
				put("xlsx", String.format("api/class/%s/resource/%s/xslx", classId, id));
				put("pptx", String.format("api/class/%s/resource/%s/pptx", classId, id));
			}
		});
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("{id}/jpeg/{page}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceViewJpeg(@PathParam("classId") String classId, @PathParam("id") final String id, 
			@PathParam("page") String page) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			if(klass == null) throw new ClassNotFoundException(
				String.format("Perkuliahan #%s tidak ditemukan", classId)
			);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(new TBahanKuliah() {{
				setKdBahan(Integer.valueOf(id));
				setPelaksanaan(klass);
			}});
			
			if(existingRescs.size() == 0) throw new ResourceNotFoundException(
					String.format("No resource #%s found in class #%s. %s.", 
					id, classId, klass.getJadwal().getDosenMatkul().getMatkul().getNama()));
			
			TBahanKuliah res = existingRescs.get(0);
			final String jpegPath = basePath + File.separator + FilenameUtils.getBaseName(res.getPath()) + File.separator + page + ".jpg";
			if(! new File(jpegPath).exists()) throw new ImageNotFoundException("Image not found");
			
			return Response.ok(new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					IOUtils.copy(new FileInputStream(new File(jpegPath)), output);
				}
			}).type("image/jpeg").build();
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ClassNotFoundException | ResourceNotFoundException | ImageNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("{id}/pdf")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceViewPdf(@PathParam("classId") String classId, @PathParam("id") final String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			if(klass == null) throw new ClassNotFoundException(
				String.format("Perkuliahan #%s tidak ditemukan", classId)
			);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(new TBahanKuliah() {{
				setKdBahan(Integer.valueOf(id));
				setPelaksanaan(klass);
			}});
			
			if(existingRescs.size() == 0) throw new ResourceNotFoundException(
					String.format("No resource #%s found in class #%s. %s.", 
					id, classId, klass.getJadwal().getDosenMatkul().getMatkul().getNama()));
			
			TBahanKuliah res = existingRescs.get(0);
			
			final String pdfPath = basePath + File.separator + FilenameUtils.getBaseName(res.getPath()) + ".pdf";
			if(! new File(pdfPath).exists()) throw new PdfNotFoundException("PDF not found");
			
			return Response.ok(new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					IOUtils.copy(new FileInputStream(new File(pdfPath)), output);
				}
			}).type("application/pdf").build();
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ResourceNotFoundException | PdfNotFoundException | ClassNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("{id}/docx")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceViewDocx(@PathParam("classId") String classId, @PathParam("id") final String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			if(klass == null) throw new ClassNotFoundException(
				String.format("Perkuliahan #%s tidak ditemukan", classId)
			);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(new TBahanKuliah() {{
				setKdBahan(Integer.valueOf(id));
				setPelaksanaan(klass);
			}});
			
			if(existingRescs.size() == 0) throw new ResourceNotFoundException(
					String.format("No resource #%s found in class #%s. %s.", 
					id, classId, klass.getJadwal().getDosenMatkul().getMatkul().getNama()));
			
			TBahanKuliah res = existingRescs.get(0);
			
			final String docxPath = basePath + File.separator + FilenameUtils.getBaseName(res.getPath()) + ".docx";
			if(! new File(docxPath).exists()) throw new DocxNotFoundException("Docx file not found");
			
			return Response.ok(new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					IOUtils.copy(new FileInputStream(new File(docxPath)), output);
				}
			}).type("application/vnd.openxmlformats-officedocument.wordprocessingml.document").build();
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ResourceNotFoundException | DocxNotFoundException | ClassNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("{id}/xlsx")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceViewXlsx(@PathParam("classId") String classId, @PathParam("id") final String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			if(klass == null) throw new ClassNotFoundException(
				String.format("Perkuliahan #%s tidak ditemukan", classId)
			);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(new TBahanKuliah() {{
				setKdBahan(Integer.valueOf(id));
				setPelaksanaan(klass);
			}});
			
			if(existingRescs.size() == 0) throw new ResourceNotFoundException(
					String.format("No resource #%s found in class #%s. %s.", 
					id, classId, klass.getJadwal().getDosenMatkul().getMatkul().getNama()));
			
			TBahanKuliah res = existingRescs.get(0);
			
			final String docxPath = basePath + File.separator + FilenameUtils.getBaseName(res.getPath()) + ".xlsx";
			if(! new File(docxPath).exists()) throw new XlsxNotFoundException("Xlsx file not found");
			
			return Response.ok(new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					IOUtils.copy(new FileInputStream(new File(docxPath)), output);
				}
			}).type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").build();
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ResourceNotFoundException | XlsxNotFoundException | ClassNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("{id}/pptx")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceViewPptx(@PathParam("classId") String classId, @PathParam("id") final String id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			if(klass == null) throw new ClassNotFoundException(
				String.format("Perkuliahan #%s tidak ditemukan", classId)
			);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(new TBahanKuliah() {{
				setKdBahan(Integer.valueOf(id));
				setPelaksanaan(klass);
			}});
			
			if(existingRescs.size() == 0) throw new ResourceNotFoundException(
					String.format("No resource #%s found in class #%s. %s.", 
					id, classId, klass.getJadwal().getDosenMatkul().getMatkul().getNama()));
			
			TBahanKuliah res = existingRescs.get(0);
			
			final String docxPath = basePath + File.separator + FilenameUtils.getBaseName(res.getPath()) + ".pptx";
			if(! new File(docxPath).exists()) throw new PptxNotFoundException("Pptx file not found");
			
			return Response.ok(new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					IOUtils.copy(new FileInputStream(new File(docxPath)), output);
				}
			}).type("application/vnd.openxmlformats-officedocument.presentationml.presentation").build();
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ResourceNotFoundException | PptxNotFoundException | ClassNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@POST
	@Path("add")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addResource(
			@PathParam("classId") String classId, 
			@FormDataParam("resfile") InputStream uplIS,
            @FormDataParam("resfile") final FormDataContentDisposition detail, 
            @FormDataParam("description") String desc
	) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			if(uplIS == null) throw new StreamNotFoundException(
				String.format("No uploaded resource found. Double check field name : %s", "resfile"));
			if(desc == null) desc = "";			
			
			response.put("file", detail.getFileName());
			final String ext = FilenameUtils.getExtension(detail.getFileName());
			final String outpath = basePath + File.separator + new Date().getTime() + "." + ext;
			final File outfile = new File(outpath);
			outfile.getParentFile().mkdirs();
			
			LOG.info("Received Class Resource File : " + detail.getFileName());
			long copiedBytes = IOUtils.copyLarge(uplIS, new FileOutputStream(outpath));
			response.put("message", "Succesfully uploaded with " + FileUtils.byteCountToDisplaySize(copiedBytes));
			
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			if(klass == null) throw new ClassNotFoundException(
				String.format("Perkuliahan #%s tidak ditemukan", classId)
			);
			
			TBahanKuliah res = new TBahanKuliah(klass, detail.getFileName(), 
					FilenameUtils.getBaseName(outpath) + "." + ext, desc, new Date());
			
			CreateOrUpdateStatus cru = bahanKuliahDao.createOrUpdate(res);
			if(cru.isCreated()) {
				LOG.info("New resource is successfully added : #{}", res.getKdBahan());
			} else if(cru.isUpdated()) {
				LOG.info("Resource is successfully updated : #{}", res.getKdBahan());
			}
			
			new Thread() {
				public void run() {
					LOG.info("Start converting " + detail.getFileName() + " to image(s)...");
					
					try {
						if(ext.equalsIgnoreCase("pdf")) {
							ImageConverter.fromPdf(outfile);
						}
						else if(ext.equalsIgnoreCase("docx")) {
							ImageConverter.fromDocx(outfile);
						}
						else if(ext.equalsIgnoreCase("xlsx")) {
							ImageConverter.fromXlsx(outfile);
						}
						else if(ext.equalsIgnoreCase("pptx")) {
							ImageConverter.fromPptx(outfile);
						}
					} catch (Exception e) {
						LOG.error("Error in converting {} to image(s). Message : {}", 
							detail.getFileName(), 
							e.getMessage()
						);
					}
					
					LOG.info("Finished converting " + detail.getFileName() + " to image(s)...");
				};
			}.start();
		} catch (StreamNotFoundException | ClassNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		} catch (FileNotFoundException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in uploading file(s)");
		} catch (IOException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in uploading file(s)");
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("{id}/edit")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editResource(
			@PathParam("classId") String classId, @PathParam("id") final Integer id, 
			@FormDataParam("resfile") InputStream uplIS,
            @FormDataParam("resfile") final FormDataContentDisposition detail, 
            @FormDataParam("description") String desc
    ) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			if(uplIS == null) throw new StreamNotFoundException(
				String.format("No uploaded resource found. Double check field name : %s", "resfile"));
			if(desc == null) desc = "";			
			
			response.put("file", detail.getFileName());
			final String ext = FilenameUtils.getExtension(detail.getFileName());
			final String outpath = basePath + File.separator + new Date().getTime() + "." + ext;
			final File outfile = new File(outpath);
			outfile.getParentFile().mkdirs();
			
			LOG.info("Received Class Resource File : " + detail.getFileName());
			long copiedBytes = IOUtils.copyLarge(uplIS, new FileOutputStream(outpath));
			response.put("message", "Succesfully uploaded with " + FileUtils.byteCountToDisplaySize(copiedBytes));
			
			final TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			if(klass == null) throw new ClassNotFoundException(
				String.format("Perkuliahan #%s tidak ditemukan", classId)
			);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(new TBahanKuliah() {{
				setPelaksanaan(klass);
				setKdBahan(id);
			}});
			
			if(existingRescs.size() == 0) throw new ResourceNotFoundException(
					String.format("No resource #%s found in class #%s. %s.", 
					id, classId, klass.getJadwal().getDosenMatkul().getMatkul().getNama()));
			
			TBahanKuliah res = existingRescs.get(0);
			res.setFile(detail.getFileName());
			res.setDeskripsi(desc);
			res.setPath(FilenameUtils.getBaseName(outpath));
			res.setTanggalUpload(new Date());
			
			CreateOrUpdateStatus cru = bahanKuliahDao.createOrUpdate(res);
			if(cru.isCreated()) {
				LOG.info("New resource is successfully added : #{}", res.getKdBahan());
			} else if(cru.isUpdated()) {
				LOG.info("Resource is successfully updated : #{}", res.getKdBahan());
			}
			
			new Thread() {
				public void run() {
					LOG.info("Start converting " + detail.getFileName() + " to image(s)...");
					
					try {
						if(ext.equalsIgnoreCase("pdf")) {
							ImageConverter.fromPdf(outfile);
						}
						else if(ext.equalsIgnoreCase("docx")) {
							ImageConverter.fromDocx(outfile);
						}
						else if(ext.equalsIgnoreCase("xlsx")) {
							ImageConverter.fromXlsx(outfile);
						}
						else if(ext.equalsIgnoreCase("pptx")) {
							ImageConverter.fromPptx(outfile);
						}
					} catch (Exception e) {
						LOG.error("Error in converting {} to image(s). Message : {}", 
							detail.getFileName(), 
							e.getMessage()
						);
					}
					
					LOG.info("Finished converting " + detail.getFileName() + " to image(s)...");
				};
			}.start();
		} catch (StreamNotFoundException | ClassNotFoundException | ResourceNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		} catch (FileNotFoundException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in uploading file(s)");
		} catch (IOException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in uploading file(s)");
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}
	
	@GET
	@Path("{id}/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteResource(@PathParam("classId") String classId, @PathParam("id") final Integer id) {
		Map<String, Object> response = new HashMap<>();
		boolean success = false;
		Status status = Status.OK;
		
		try {
			final TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			if(klass == null) throw new ClassNotFoundException(
				String.format("Perkuliahan #%s tidak ditemukan", classId)
			);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(new TBahanKuliah() {{
				setPelaksanaan(klass);
				setKdBahan(id);
			}});
			
			if(existingRescs.size() == 0) throw new ResourceNotFoundException(
					String.format("No resource #%s found in class #%s. %s.", 
					id, classId, klass.getJadwal().getDosenMatkul().getMatkul().getNama()));
			
			TBahanKuliah res = existingRescs.get(0);
			if(bahanKuliahDao.delete(res) == 1) {
				response.put("result", String.format("Resource #%s. %s within class #%s. %s is successfully deleted.", 
						id, res.getKdBahan(), classId, klass.getKdPelaksanaan()));
			} else {
				response.put("result", String.format("Failed delete resource #%s. %s within class #%s. %s", 
						id, res.getKdBahan(), classId, klass.getKdPelaksanaan()));
			}
		} catch (SQLException e) {
			status = Status.INTERNAL_SERVER_ERROR;
			LOG.error(e.getMessage());
			response.put("error", "Error in database access");
		} catch (ClassNotFoundException | ResourceNotFoundException e) {
			status = Status.NOT_FOUND;
			response.put("error", e.getMessage());
		}
		
		response.put("success", success);
		return Response.status(status).entity(response).build();
	}

}
