package id.ac.itb.academic;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import id.ac.itb.academic.library.ClasspathUtil;

@SpringBootApplication
public class AcademicPortalApplication {
	
	public static void main(String[] args) {
		File cd = new File(System.getProperty("user.dir") + File.separator + "conf");
    	
    	ClasspathUtil cpU;
		try {
			cpU = new ClasspathUtil();
			cpU.addDirectoryClasspath(cd, false);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | MalformedURLException e1) {
			e1.printStackTrace();
		}
    	
    	SpringApplication.run(AcademicPortalApplication.class, args);
	}
	
}
