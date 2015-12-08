package id.ac.itb.academic.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
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
	public Response listResources(@PathParam("classId") String classId) {
		return Response.ok(classId).build();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response resourceInfo(@PathParam("classId") String classId, @PathParam("id") String id) {
		return Response.ok().build();
	}
	
	@POST
	@Path("add")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addResource(
			@PathParam("classId") String classId, 
			@FormDataParam("resfile") InputStream uplIS,
            @FormDataParam("resfile") final FormDataContentDisposition detail
	) {
		Map<String, Object> response = new HashMap<>();
		response.put("file", detail.getFileName());
		
		final String ext = FilenameUtils.getExtension(detail.getFileName());
		final String outpath = basePath + "/" + new Date().getTime() + "." + ext;
		final File outfile = new File(outpath);
		outfile.getParentFile().mkdirs();
		
		try {
			LOG.info("Received Class Resource File : " + detail.getFileName());
			long copiedBytes = IOUtils.copyLarge(uplIS, new FileOutputStream(outpath));
			response.put("message", "Succesfully uploaded with " + FileUtils.byteCountToDisplaySize(copiedBytes));
			
			TPelaksanaanKuliah klass = pelaksanaanKuliahDao.queryForId(Integer.valueOf(classId));		
			TBahanKuliah res = new TBahanKuliah(klass, detail.getFileName(), outpath, "Bahan", new Date());
			
			CreateOrUpdateStatus cru = bahanKuliahDao.createOrUpdate(res);
			if(cru.isCreated()) {
				LOG.info("New resource is successfully added : #{}", res.getKdBahan());
			} else if(cru.isUpdated()) {
				LOG.info("Resource is successfully updated : #{}", res.getKdBahan());
			}
			
			new Thread() {
				public void run() {
					if(ext.equalsIgnoreCase("pdf")) {
						LOG.info("Start converting " + detail.getFileName() + " to image(s)...");
						
						try {
							ImageConverter.fromPdf(outfile);
						} catch (FileNotFoundException e) {
							LOG.error("Error in converting {} to image(s). Message : {}", 
									detail.getFileName(), 
									e.getMessage());
						}
						
						LOG.info("Finished converting " + detail.getFileName() + " to image(s)...");
					}
					
					else if(ext.equalsIgnoreCase("docx")) {
						LOG.info("Start converting " + detail.getFileName() + " to image(s)...");
						
						try {
							ImageConverter.fromDocx(outfile);
						} catch (Exception e) {
							LOG.error("Error in converting {} to image(s). Message : {}", 
									detail.getFileName(), 
									e.getMessage());
						}
						
						LOG.info("Finished converting " + detail.getFileName() + " to image(s)...");
					}
					
					else if(ext.equalsIgnoreCase("xlsx")) {
						LOG.info("Start converting " + detail.getFileName() + " to image(s)...");
						
						try {
							ImageConverter.fromXlsx(outfile);
						} catch (Exception e) {
							LOG.error("Error in converting {} to image(s). Message : {}", 
									detail.getFileName(), 
									e.getMessage());
						}
						
						LOG.info("Finished converting " + detail.getFileName() + " to image(s)...");
					}
					
					else if(ext.equalsIgnoreCase("pptx")) {
						LOG.info("Start converting " + detail.getFileName() + " to image(s)...");
						
						try {
							ImageConverter.fromPptx(outfile);
						} catch (Exception e) {
							LOG.error("Error in converting {} to image(s). Message : {}", 
									detail.getFileName(), 
									e.getMessage());
						}
						
						LOG.info("Finished converting " + detail.getFileName() + " to image(s)...");
					}
				};
			}.start();
		} catch (IOException e) {
			LOG.error(e.getMessage());
			response.put("error", "Error in uploading file(s)");
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return Response.ok(response).build();
	}
	
	@GET
	@Path("{id}/edit")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editResource(@PathParam("classId") String classId, @PathParam("id") String id) {
		return Response.ok().build();
	}
	
	@GET
	@Path("{id}/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteResource(@PathParam("classId") String classId, @PathParam("id") String id) {
		return Response.ok().build();
	}

}
