package id.ac.itb.academic.library;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PDimension;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.sejda.impl.icepdf.PdfToMultipleImageTask;
import org.sejda.model.exception.TaskException;
import org.sejda.model.exception.TaskOutputVisitException;
import org.sejda.model.input.PdfStreamSource;
import org.sejda.model.output.MultipleTaskOutput;
import org.sejda.model.output.SingleTaskOutput;
import org.sejda.model.output.TaskOutputDispatcher;
import org.sejda.model.parameter.image.AbstractPdfToMultipleImageParameters;
import org.sejda.model.parameter.image.PdfToJpegParameters;
import org.sejda.model.pdf.page.PageRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageConverter {
	static Logger LOG = LoggerFactory.getLogger(ImageConverter.class);
	
	public static void fromPdf(String pdfFilename, InputStream is) {
		String outfile = pdfFilename.replace(".pdf", "");
		new File(outfile).mkdirs();
		
		Document document = new Document();
		try {
			document.setInputStream(is, pdfFilename);
			for(int p=0; p<document.getNumberOfPages(); p++) {
				Page currentPage = document.getPageTree().getPage(p);
				currentPage.init();
		        PDimension pageDimensions = currentPage.getSize(0, 1.0f);
		        BufferedImage currentImage = new BufferedImage((int) pageDimensions.getWidth(), 
		        		(int) pageDimensions.getHeight(), BufferedImage.TYPE_INT_RGB);
		        Graphics2D g = currentImage.createGraphics();
		        currentPage.paint(g, GraphicsRenderingHints.PRINT, Page.BOUNDARY_CROPBOX, 0, 1.0f);
		        g.dispose();
		        
		        ImageIO.write(currentImage, "jpg", new FileOutputStream(outfile + File.separator + (p+1) + ".jpg"));
			}
		} catch (PDFException | PDFSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*PdfStreamSource input = PdfStreamSource.newInstanceNoPassword(is, pdfFilename);
		ADirectoryTaskOutput output = new ADirectoryTaskOutput(pdfFilename.replace(".pdf", ""));
		
		PdfToJpegParameters params = new PdfToJpegParameters();
		params.setSource(input);
		params.setOutput(output);
		params.addPageRange(new PageRange(1));
		params.setOverwrite(true);
		
		try {
			PdfToMultipleImageTask<AbstractPdfToMultipleImageParameters> t = new PdfToMultipleImageTask<>();
			t.getNotifiableTaskMetadata();
			t.before(params);
			t.execute(params);
			t.after();
		} catch (TaskException e) {
			LOG.error("Error while converting " + pdfFilename + " with message : " + e.getMessage());
		}*/
	}
	
	static class ADirectoryTaskOutput implements MultipleTaskOutput<String>, SingleTaskOutput<String> {
		private final Logger LOG = LoggerFactory.getLogger(ADirectoryTaskOutput.class);
		private String outDir;

		public ADirectoryTaskOutput(String outDir) {
			LOG.info("Output directory : " + outDir);
			this.outDir = outDir;
		}
		
		public boolean exists(String name) throws MalformedURLException {
			File of = new File(getDestination() + File.separator + name + ".jpg");
			return of.exists();
		}

		public void accept(File tmpFile, String name) throws IOException {
			File of = new File(getDestination() + File.separator + name + ".jpg");
			if(! new File(getDestination()).mkdirs()) LOG.error("{} is not created.", getDestination());;
			OutputStream os = new FileOutputStream(of);
			
			FileInputStream input = null;
			try {
				input = new FileInputStream(tmpFile);
	            IOUtils.copy(input, os);
	        } finally {
	        	LOG.info("Succesfully convert to {}.", of.getPath());
	            IOUtils.closeQuietly(input);
	            delete(tmpFile);
	        }
		}
		
		private void delete(File file) {
	        if (!file.delete()) {
	            LOG.warn("Unable to delete temporary file {}", file);
	        }
	    }

		@Override
		public String getDestination() {		
			return outDir;
		}

		@Override
		public void accept(TaskOutputDispatcher dispatcher) throws TaskOutputVisitException { }

	}

}
