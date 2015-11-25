package id.ac.itb.academic.library;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClasspathUtil {
	private URLClassLoader sysloader;
	private Method addUrl;

	public ClasspathUtil() throws NoSuchMethodException, SecurityException {
		sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		addUrl = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		addUrl.setAccessible(true);
	}
	
	public void addClasspath(File cp, boolean recursive) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
		if(recursive) {
			addClasspathRecursively(cp);
		} else {
			addToClasspath(cp);
		}
	}
	
	public void addDirectoryClasspath(File dir, boolean recursive) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
		if(recursive) {
			addToClasspath(dir);
			addDirectoryClasspathRecursively(dir);
		} else {
			addToClasspath(dir);
		}
	}
	
	private void addClasspathRecursively(File cp) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
		File[] children = cp.listFiles();
		if(children == null) {
			addToClasspath(cp);
		} else {
			for (File f : children) {
				if(f.isDirectory()) {
					addClasspathRecursively(f);
				} else {
					addToClasspath(f);
				}
			} 
		}
	}
	
	private void addDirectoryClasspathRecursively(File dir) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
		File[] children = dir.listFiles();
		if(children != null) {
			for(File f : children) {
				if(f.isDirectory()) {
					addToClasspath(f);
					
					if(f.listFiles().length > 0) {
						addDirectoryClasspathRecursively(f);
					}
				}
			}
		}
	}
	
	private void addToClasspath(File cp) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
		addUrl.invoke(sysloader, new Object[]{cp.toURI().toURL()});
		System.out.println("Classpath added : " + cp.toString());
	}
}
