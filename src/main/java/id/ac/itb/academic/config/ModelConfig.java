package id.ac.itb.academic.config;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.spring.DaoFactory;
import com.j256.ormlite.table.TableInfo;
import com.j256.ormlite.table.TableUtils;

import id.ac.itb.academic.model.TUser;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class ModelConfig {
	
	@Value("${database.url}")
	private String dbUrl;
	@Value("${database.username}")
	private String dbUsername;
	@Value("${database.password}")
	private String dbPassword;
	
	@Bean(name = "connectionSource")
	public JdbcConnectionSource conn() throws SQLException {
		JdbcConnectionSource conn = new JdbcConnectionSource();
		conn.setUrl(dbUrl);
		conn.setUsername(dbUsername);
		conn.setPassword(dbPassword);
		conn.initialize();
		return conn;
	}
	
	@Bean(name = "userDao")
	@DependsOn("connectionSource")
	public Dao<TUser, Object> userDao() throws SQLException {
		Dao<TUser, Object> userDao = DaoFactory.createDao(conn(), TUser.class);
		
		if(! userDao.isTableExists()) {
			// Do create table
			TableUtils.createTable(userDao.getConnectionSource(), userDao.getDataClass());
		} else {
			// Do alter table
			TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return userDao;
	}
	
}
