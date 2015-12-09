package id.ac.itb.academic.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Locator;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.DefaultHashAlgorithm;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class MemcachedConfig {
	
	@Value("${memcached.address}")
	private String address;
	
	@Bean
	public MemcachedClient memcached() throws IOException {
		ConnectionFactoryBuilder cf = new ConnectionFactoryBuilder();
		cf.setProtocol(Protocol.TEXT);
		cf.setHashAlg (DefaultHashAlgorithm.CRC_HASH);
		cf.setFailureMode (FailureMode.Redistribute);
		cf.setLocatorType (Locator.CONSISTENT);
		
		MemcachedClient client = new MemcachedClient(cf.build(), AddrUtil.getAddresses(address));
		return client;
	}

}
