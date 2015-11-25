package id.ac.itb.academic.filter;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ResourceFilterBindingFeature implements DynamicFeature {
	Logger LOG = LoggerFactory.getLogger(ResourceFilterBindingFeature.class);

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		context.register(CORSResponseFilter.class);
	}

}
