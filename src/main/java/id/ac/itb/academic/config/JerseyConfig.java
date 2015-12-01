package id.ac.itb.academic.config;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import id.ac.itb.academic.filter.ResourceFilterBindingFeature;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
    	register(ResourceFilterBindingFeature.class);
    	register(MultiPartFeature.class);
        packages("id.ac.itb.academic");
    }
    
    @Value("${class.resource.basePath}")
	private String classResourceBasePath;
	
	@Bean(name = "classResourceBasePath")
	public String classResourceBasePath() {
		return classResourceBasePath;
	}
}
