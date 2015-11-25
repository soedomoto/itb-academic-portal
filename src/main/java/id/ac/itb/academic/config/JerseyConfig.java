package id.ac.itb.academic.config;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import id.ac.itb.academic.filter.ResourceFilterBindingFeature;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
    	register(ResourceFilterBindingFeature.class);
    	register(MultiPartFeature.class);
        packages("id.ac.itb.academic");
    }
}
