package id.ac.itb.academic.library;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PDimension;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageConverter {
	static Logger LOG = LoggerFactory.getLogger(ImageConverter.class);
	
	public static void fromPdf(File pdfFile) throws FileNotFoundException {
		String pdfFilename = pdfFile.getAbsolutePath();
		InputStream is = new FileInputStream(pdfFile);
		fromPdf(pdfFilename, is);
	}
	
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
			LOG.error("Error in converting {} to image. Message : {}", FilenameUtils.getBaseName(pdfFilename), e.getMessage());
		}
	}
	
	/**
	 * This function needs Microsoft Font
	 * If you are running in ubuntu, then : sudo apt-get install ttf-mscorefonts-installer
	 * Or for Win81 fonts, see http://www.webupd8.org/2013/07/how-to-download-and-install-windows-81.html
	 * @param docxFile
	 * @throws Docx4JException
	 * @throws IOException 
	 */
	public static void fromDocx(File docxFile) throws Docx4JException, IOException {
		String baseDir = docxFile.getParent();
		String fileName = FilenameUtils.getBaseName(docxFile.getAbsolutePath());
		String filePdfPath = baseDir + File.separator + fileName + ".pdf";
		
		/*try {
			WordprocessingMLPackage mlPackage = WordprocessingMLPackage.load(docxFile);
			
			FieldUpdater updater = new FieldUpdater(mlPackage);
			updater.update(true);
			
			mlPackage.setFontMapper(new IdentityPlusMapper());
			
			FOSettings foSettings = Docx4J.createFOSettings();
			foSettings.setWmlPackage(mlPackage);
			
			Docx4J.toFO(
				foSettings, 
				new FileOutputStream(filePdfPath), 
				Docx4J.FLAG_EXPORT_PREFER_XSL
			);
		} catch (Exception e) {
			LOG.error("Error in converting {} to pdf. Message : {}", 
					FilenameUtils.getBaseName(docxFile.getAbsolutePath()), 
					e.getMessage());
		} finally {
			fromPdf(new File(filePdfPath));
		}*/
		
		try {
			XWPFDocument document = new XWPFDocument(new FileInputStream(docxFile));
			PdfOptions options = PdfOptions.create();
			PdfConverter.getInstance().convert(document, new FileOutputStream(filePdfPath), options);
		} finally {
			fromPdf(new File(filePdfPath));
		}
	}
	
	public static void fromXlsx(File xlsxFile) throws Exception {
		throw new Exception("Unsupported yet");
	}
	
	public static void fromPptx(File pptxFile) throws Exception {
		String baseDir = pptxFile.getParent();
		String fileName = FilenameUtils.getBaseName(pptxFile.getAbsolutePath());
		String fileImgPath = baseDir + File.separator + fileName;
		
		new File(fileImgPath).mkdirs();
		
		XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(pptxFile));
		Dimension pgsize = ppt.getPageSize();
		int idx = 1;
	    for (XSLFSlide slide : ppt.getSlides()) {
	    	BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
	        Graphics2D graphics = img.createGraphics();
	        // clear the drawing area
	        graphics.setPaint(Color.white);
	        graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
	        // render
	        slide.draw(graphics);
	        // save the output
	        FileOutputStream out = new FileOutputStream(fileImgPath + File.separator + idx + ".jpg");
	        ImageIO.write(img, "jpg", out);
	        out.close();

	        idx++;
	    }
	    
	}

}
