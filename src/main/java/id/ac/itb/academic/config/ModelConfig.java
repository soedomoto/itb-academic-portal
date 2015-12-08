package id.ac.itb.academic.config;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.spring.DaoFactory;
import com.j256.ormlite.table.TableUtils;

import id.ac.itb.academic.model.TAbsensiDosen;
import id.ac.itb.academic.model.TAbsensiMahasiswa;
import id.ac.itb.academic.model.TAkun;
import id.ac.itb.academic.model.TBahanKuliah;
import id.ac.itb.academic.model.TDosen;
import id.ac.itb.academic.model.TDosenMatkul;
import id.ac.itb.academic.model.TFakultas;
import id.ac.itb.academic.model.TJadwalKuliah;
import id.ac.itb.academic.model.TJawabanTugas;
import id.ac.itb.academic.model.TMahasiswa;
import id.ac.itb.academic.model.TMataKuliah;
import id.ac.itb.academic.model.TNilai;
import id.ac.itb.academic.model.TOpsi;
import id.ac.itb.academic.model.TPelaksanaanKuliah;
import id.ac.itb.academic.model.TPengumuman;
import id.ac.itb.academic.model.TProdi;
import id.ac.itb.academic.model.TSilabus;
import id.ac.itb.academic.model.TTugas;

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
	
	@Bean(name = "akunDao")
	public Dao<TAkun, Object> akunDao() throws SQLException {
		Dao<TAkun, Object> dao = DaoFactory.createDao(conn(), TAkun.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "dosenDao")
	@DependsOn({ "akunDao" })
	public Dao<TDosen, Object> dosenDao() throws SQLException {
		Dao<TDosen, Object> dao = DaoFactory.createDao(conn(), TDosen.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "fakultasDao")
	public Dao<TFakultas, Object> fakultasDao() throws SQLException {
		Dao<TFakultas, Object> dao = DaoFactory.createDao(conn(), TFakultas.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "prodiDao")
	@DependsOn({ "fakultasDao" })
	public Dao<TProdi, Object> prodiDao() throws SQLException {
		Dao<TProdi, Object> dao = DaoFactory.createDao(conn(), TProdi.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "opsiDao")
	@DependsOn({ "prodiDao" })
	public Dao<TOpsi, Object> opsiDao() throws SQLException {
		Dao<TOpsi, Object> dao = DaoFactory.createDao(conn(), TOpsi.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "mataKuliahDao")
	@DependsOn({ "prodiDao", "opsiDao" })
	public Dao<TMataKuliah, Object> mataKuliahDao() throws SQLException {
		Dao<TMataKuliah, Object> dao = DaoFactory.createDao(conn(), TMataKuliah.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "dosenMatkulDao")
	@DependsOn({ "dosenDao", "mataKuliahDao" })
	public Dao<TDosenMatkul, Object> dosenMatkulDao() throws SQLException {
		Dao<TDosenMatkul, Object> dao = DaoFactory.createDao(conn(), TDosenMatkul.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "jadwalKuliahDao")
	@DependsOn({ "dosenMatkulDao" })
	public Dao<TJadwalKuliah, Object> jadwalKuliahDao() throws SQLException {
		Dao<TJadwalKuliah, Object> dao = DaoFactory.createDao(conn(), TJadwalKuliah.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "pelaksanaanKuliahDao")
	@DependsOn({ "jadwalKuliahDao" })
	public Dao<TPelaksanaanKuliah, Object> pelaksanaanKuliahDao() throws SQLException {
		Dao<TPelaksanaanKuliah, Object> dao = DaoFactory.createDao(conn(), TPelaksanaanKuliah.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "absensiDosenDao")
	@DependsOn({ "dosenDao", "pelaksanaanKuliahDao" })
	public Dao<TAbsensiDosen, Object> absensiDosenDao() throws SQLException {
		Dao<TAbsensiDosen, Object> dao = DaoFactory.createDao(conn(), TAbsensiDosen.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "mahasiswaDao")
	@DependsOn({ "akunDao", "prodiDao", "opsiDao" })
	public Dao<TMahasiswa, Object> mahasiswaDao() throws SQLException {
		Dao<TMahasiswa, Object> dao = DaoFactory.createDao(conn(), TMahasiswa.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "absensiMahasiswaDao")
	@DependsOn({ "mahasiswaDao", "pelaksanaanKuliahDao" })
	public Dao<TAbsensiMahasiswa, Object> absensiMahasiswaDao() throws SQLException {
		Dao<TAbsensiMahasiswa, Object> dao = DaoFactory.createDao(conn(), TAbsensiMahasiswa.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "tugasDao")
	@DependsOn({ "pelaksanaanKuliahDao" })
	public Dao<TTugas, Object> tugasDao() throws SQLException {
		Dao<TTugas, Object> dao = DaoFactory.createDao(conn(), TTugas.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "jawabanTugasDao")
	@DependsOn({ "tugasDao", "mahasiswaDao" })
	public Dao<TJawabanTugas, Object> jawabanTugasDao() throws SQLException {
		Dao<TJawabanTugas, Object> dao = DaoFactory.createDao(conn(), TJawabanTugas.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "nilaiDao")
	@DependsOn({ "dosenMatkulDao", "mahasiswaDao" })
	public Dao<TNilai, Object> nilaiDao() throws SQLException {
		Dao<TNilai, Object> dao = DaoFactory.createDao(conn(), TNilai.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "bahanKuliahDao")
	@DependsOn({ "pelaksanaanKuliahDao" })
	public Dao<TBahanKuliah, Object> bahanKuliahDao() throws SQLException {
		Dao<TBahanKuliah, Object> dao = DaoFactory.createDao(conn(), TBahanKuliah.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "silabusDao")
	@DependsOn({ "mataKuliahDao" })
	public Dao<TSilabus, Object> silabusDao() throws SQLException {
		Dao<TSilabus, Object> dao = DaoFactory.createDao(conn(), TSilabus.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
	@Bean(name = "pengumumanDao")
	@DependsOn({ "dosenDao" })
	public Dao<TPengumuman, Object> pengumumanDao() throws SQLException {
		Dao<TPengumuman, Object> dao = DaoFactory.createDao(conn(), TPengumuman.class);
		
		if(! dao.isTableExists()) {
			TableUtils.createTable(dao.getConnectionSource(), dao.getDataClass());
		} else {
			// TableInfo<TUser, Object> info = ((BaseDaoImpl<TUser, Object>) userDao).getTableInfo();
			// userDao.executeRaw("ALTER TABLE " + info.getTableName() + " ADD IF NOT EXISTS supervisor_id INTEGER DEFAULT NULL");
		}
		
		return dao;
	}
	
}
