package id.ac.itb.academic.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		List<Map<String, Object>> responses = new ArrayList<>();
		
		try {
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			
			TBahanKuliah existingRes = new TBahanKuliah();
			existingRes.setPelaksanaan(klass);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(existingRes);
			for(final TBahanKuliah res : existingRescs) {
				Map<String, Object> response = new HashMap<>();
				response.put("kodeBahan", res.getKdBahan());
				response.put("fileName", res.getFile());
				response.put("deskripsi", res.getDeskripsi());
				response.put("tanggalUpload", new SimpleDateFormat("dd-MM-yyyy").format(res.getTanggalUpload()));
				//response.put("detailUrl", String.format("api/class/%s/resource/%s", classId, res.getKdBahan()));
				response.put("availableActions", new HashMap<String, Object>() {
					private static final long serialVersionUID = 6054270051416719110L;
					{
						put("info", String.format("api/class/%s/resource/%s", classId, res.getKdBahan()));
						put("edit", String.format("api/class/%s/resource/%s/edit", classId, res.getKdBahan()));
						put("delete", String.format("api/class/%s/resource/%s/delete", classId, res.getKdBahan()));
					}
				});
				
				responses.add(response);
			}
		} catch (SQLException e1) {
			LOG.error(e1.getMessage());
			
			Map<String, Object> response = new HashMap<>();
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
		
		return Response.ok(responses).build();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceInfo(@PathParam("classId") final String classId, @PathParam("id") final String id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			
			TBahanKuliah existingRes = new TBahanKuliah();
			existingRes.setKdBahan(Integer.valueOf(id));
			existingRes.setPelaksanaan(klass);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(existingRes);
			if(existingRescs.size() > 0) {
				TBahanKuliah res = existingRescs.get(0);
				response.put("kodeBahan", res.getKdBahan());
				response.put("fileName", res.getFile());
				response.put("deskripsi", res.getDeskripsi());
				response.put("tanggalUpload", new SimpleDateFormat("dd-MM-yyyy").format(res.getTanggalUpload()));
			} else {
				response.put("error", String.format("No resource #%s found in class #%s. %s.", 
						id, classId, klass.getJadwal().getDosenMatkul().getMatkul().getNama()));
				return Response.status(Status.NOT_FOUND).entity(response).build();
			}
		} catch (SQLException e1) {
			LOG.error(e1.getMessage());
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
		
		response.put("availableFormats", new HashMap<String, Object>() {
			private static final long serialVersionUID = 6054270051416719110L;
			{
				put("jpeg", String.format("api/class/%s/resource/%s/jpeg/{page}", classId, id));
				put("pdf", String.format("api/class/%s/resource/%s/pdf", classId, id));
				put("docx", String.format("api/class/%s/resource/%s/docx", classId, id));
				put("xlsx", String.format("api/class/%s/resource/%s/xslx", classId, id));
				put("pptx", String.format("api/class/%s/resource/%s/pptx", classId, id));
			}
		});
		
		return Response.ok(response).build();
	}
	
	@GET
	@Path("{id}/jpeg/{page}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceViewJpeg(@PathParam("classId") String classId, @PathParam("id") String id, 
			@PathParam("page") String page) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			
			TBahanKuliah existingRes = new TBahanKuliah();
			existingRes.setKdBahan(Integer.valueOf(id));
			existingRes.setPelaksanaan(klass);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(existingRes);
			if(existingRescs.size() > 0) {
				TBahanKuliah res = existingRescs.get(0);
				final String jpegPath = basePath + File.separator + FilenameUtils.getBaseName(res.getPath()) + File.separator + page + ".jpg";
				if(! new File(jpegPath).exists()) {
					response.put("error", "Image not found.");
					return Response.status(Status.NOT_FOUND).entity(response).build();
				}
				
				return Response.ok(new StreamingOutput() {
					@Override
					public void write(OutputStream output) throws IOException, WebApplicationException {
						IOUtils.copy(new FileInputStream(new File(jpegPath)), output);
					}
				}).type("image/jpeg").build();
			} else {
				response.put("error", String.format("No resource #%s found in class #%s.", id, classId));
				return Response.status(Status.NOT_FOUND).entity(response).build();
			}
		} catch (SQLException e1) {
			LOG.error(e1.getMessage());
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
	}
	
	@GET
	@Path("{id}/pdf")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceViewPdf(@PathParam("classId") String classId, @PathParam("id") String id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			
			TBahanKuliah existingRes = new TBahanKuliah();
			existingRes.setKdBahan(Integer.valueOf(id));
			existingRes.setPelaksanaan(klass);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(existingRes);
			if(existingRescs.size() > 0) {
				TBahanKuliah res = existingRescs.get(0);
				final String pdfPath = basePath + File.separator + FilenameUtils.getBaseName(res.getPath()) + ".pdf";
				if(! new File(pdfPath).exists()) {
					response.put("error", "Pdf file not found.");
					return Response.status(Status.NOT_FOUND).entity(response).build();
				}
				
				return Response.ok(new StreamingOutput() {
					@Override
					public void write(OutputStream output) throws IOException, WebApplicationException {
						IOUtils.copy(new FileInputStream(new File(pdfPath)), output);
					}
				}).type("application/pdf").build();
			} else {
				response.put("error", String.format("No resource #%s found in class #%s.", id, classId));
				return Response.status(Status.NOT_FOUND).entity(response).build();
			}
		} catch (SQLException e1) {
			LOG.error(e1.getMessage());
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
	}
	
	@GET
	@Path("{id}/docx")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceViewDocx(@PathParam("classId") String classId, @PathParam("id") String id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			
			TBahanKuliah existingRes = new TBahanKuliah();
			existingRes.setKdBahan(Integer.valueOf(id));
			existingRes.setPelaksanaan(klass);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(existingRes);
			if(existingRescs.size() > 0) {
				TBahanKuliah res = existingRescs.get(0);
				final String docxPath = basePath + File.separator + FilenameUtils.getBaseName(res.getPath()) + ".docx";
				if(! new File(docxPath).exists()) {
					response.put("error", "Docx file not found.");
					return Response.status(Status.NOT_FOUND).entity(response).build();
				}
				
				return Response.ok(new StreamingOutput() {
					@Override
					public void write(OutputStream output) throws IOException, WebApplicationException {
						IOUtils.copy(new FileInputStream(new File(docxPath)), output);
					}
				}).type("application/vnd.openxmlformats-officedocument.wordprocessingml.document").build();
			} else {
				response.put("error", String.format("No resource #%s found in class #%s.", id, classId));
				return Response.status(Status.NOT_FOUND).entity(response).build();
			}
		} catch (SQLException e1) {
			LOG.error(e1.getMessage());
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
	}
	
	@GET
	@Path("{id}/xlsx")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceViewXlsx(@PathParam("classId") String classId, @PathParam("id") String id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			
			TBahanKuliah existingRes = new TBahanKuliah();
			existingRes.setKdBahan(Integer.valueOf(id));
			existingRes.setPelaksanaan(klass);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(existingRes);
			if(existingRescs.size() > 0) {
				TBahanKuliah res = existingRescs.get(0);
				final String xlsxPath = basePath + File.separator + FilenameUtils.getBaseName(res.getPath()) + ".xlsx";
				if(! new File(xlsxPath).exists()) {
					response.put("error", "Xlsx file not found.");
					return Response.status(Status.NOT_FOUND).entity(response).build();
				}
				
				return Response.ok(new StreamingOutput() {
					@Override
					public void write(OutputStream output) throws IOException, WebApplicationException {
						IOUtils.copy(new FileInputStream(new File(xlsxPath)), output);
					}
				}).type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").build();
			} else {
				response.put("error", String.format("No resource #%s found in class #%s.", id, classId));
				return Response.status(Status.NOT_FOUND).entity(response).build();
			}
		} catch (SQLException e1) {
			LOG.error(e1.getMessage());
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
	}
	
	@GET
	@Path("{id}/pptx")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceViewPptx(@PathParam("classId") String classId, @PathParam("id") String id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			
			TBahanKuliah existingRes = new TBahanKuliah();
			existingRes.setKdBahan(Integer.valueOf(id));
			existingRes.setPelaksanaan(klass);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(existingRes);
			if(existingRescs.size() > 0) {
				TBahanKuliah res = existingRescs.get(0);
				final String pptxPath = basePath + File.separator + FilenameUtils.getBaseName(res.getPath()) + ".pptx";
				if(! new File(pptxPath).exists()) {
					response.put("error", "Pptx file not found.");
					return Response.status(Status.NOT_FOUND).entity(response).build();
				}
				
				return Response.ok(new StreamingOutput() {
					@Override
					public void write(OutputStream output) throws IOException, WebApplicationException {
						IOUtils.copy(new FileInputStream(new File(pptxPath)), output);
					}
				}).type("application/vnd.openxmlformats-officedocument.presentationml.presentation").build();
			} else {
				response.put("error", String.format("No resource #%s found in class #%s.", id, classId));
				return Response.status(Status.NOT_FOUND).entity(response).build();
			}
		} catch (SQLException e1) {
			LOG.error(e1.getMessage());
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
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
		response.put("file", detail.getFileName());
		
		final String ext = FilenameUtils.getExtension(detail.getFileName());
		final String outpath = basePath + File.separator + new Date().getTime() + "." + ext;
		final File outfile = new File(outpath);
		outfile.getParentFile().mkdirs();
		
		try {
			LOG.info("Received Class Resource File : " + detail.getFileName());
			long copiedBytes = IOUtils.copyLarge(uplIS, new FileOutputStream(outpath));
			response.put("message", "Succesfully uploaded with " + FileUtils.byteCountToDisplaySize(copiedBytes));
			
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));		
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
		} catch (IOException e) {
			LOG.error(e.getMessage());
			response.put("error", "Error in uploading file(s)");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		} catch (SQLException e1) {
			LOG.error(e1.getMessage());
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
		
		return Response.ok(response).build();
	}
	
	@GET
	@Path("{id}/edit")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editResource(
			@PathParam("classId") String classId, @PathParam("id") String id, 
			@FormDataParam("resfile") InputStream uplIS,
            @FormDataParam("resfile") final FormDataContentDisposition detail, 
            @FormDataParam("description") String desc
    ) {
		Map<String, Object> response = new HashMap<>();
		response.put("file", detail.getFileName());
		
		final String ext = FilenameUtils.getExtension(detail.getFileName());
		final String outpath = basePath + File.separator + new Date().getTime() + "." + ext;
		final File outfile = new File(outpath);
		outfile.getParentFile().mkdirs();
		
		try {
			LOG.info("Received Class Resource File : " + detail.getFileName());
			long copiedBytes = IOUtils.copyLarge(uplIS, new FileOutputStream(outpath));
			response.put("message", "Succesfully uploaded with " + FileUtils.byteCountToDisplaySize(copiedBytes));
			
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			
			TBahanKuliah existingRes = new TBahanKuliah();
			existingRes.setKdBahan(Integer.valueOf(id));
			existingRes.setPelaksanaan(klass);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(existingRes);
			if(existingRescs.size() > 0) {
				TBahanKuliah res = existingRescs.get(0);
				res.setFile(detail.getFileName());
				res.setPath(FilenameUtils.getBaseName(outpath) + "." + ext);
				res.setDeskripsi("");
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
			} else {
				response.put("error", String.format("No resource #%s found in class #%s.", id, classId));
				return Response.status(Status.NOT_FOUND).entity(response).build();
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
			response.put("error", "Error in uploading file(s)");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		} catch (SQLException e1) {
			LOG.error(e1.getMessage());
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
		
		return Response.ok(response).build();
	}
	
	@GET
	@Path("{id}/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteResource(@PathParam("classId") String classId, @PathParam("id") String id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));
			
			TBahanKuliah existingRes = new TBahanKuliah();
			existingRes.setKdBahan(Integer.valueOf(id));
			existingRes.setPelaksanaan(klass);
			
			List<TBahanKuliah> existingRescs = bahanKuliahDao.queryForMatching(existingRes);
			if(existingRescs.size() > 0) {
				TBahanKuliah res = existingRescs.get(0);
				if(bahanKuliahDao.delete(res) == 1) {
					response.put("message", String.format("Resource #%s within class #%s is successfully deleted.", 
							id, classId));
					return Response.ok(response).build();
				} else {
					response.put("message", String.format("Failed delete resource #%s within class #%s.", 
							id, classId));
					return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
				}
			} else {
				response.put("error", String.format("No resource #%s found in class #%s.", id, classId));
				return Response.status(Status.NOT_FOUND).entity(response).build();
			}
		} catch (SQLException e1) {
			LOG.error(e1.getMessage());
			response.put("error", "Error in database access");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
	}

}
