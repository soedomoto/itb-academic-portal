package id.ac.itb.academic.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="pelaksanaankuliah")
public class TPelaksanaanKuliah {
	@DatabaseField(generatedId=true)
	Integer kdPelaksanaan;
	@DatabaseField(columnName="kdJadwal", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references jadwalkuliah(kdJadwalKuliah) on delete restrict")
	TJadwalKuliah jadwal;
	@DatabaseField
	Date tanggal;
	@DatabaseField
	Integer sesi;
	@DatabaseField
	String ruang;
	
	public TPelaksanaanKuliah() {}
	
	public TPelaksanaanKuliah(TJadwalKuliah jadwal, Date tanggal, Integer sesi, String ruang) {
		super();
		this.jadwal = jadwal;
		this.tanggal = tanggal;
		this.sesi = sesi;
		this.ruang = ruang;
	}

	public Integer getKdPelaksanaan() {
		return kdPelaksanaan;
	}

	public void setKdPelaksanaan(Integer kdPelaksanaan) {
		this.kdPelaksanaan = kdPelaksanaan;
	}

	public TJadwalKuliah getJadwal() {
		return jadwal;
	}

	public void setJadwal(TJadwalKuliah jadwal) {
		this.jadwal = jadwal;
	}

	public Date getTanggal() {
		return tanggal;
	}

	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}

	public Integer getSesi() {
		return sesi;
	}

	public void setSesi(Integer sesi) {
		this.sesi = sesi;
	}

	public String getRuang() {
		return ruang;
	}

	public void setRuang(String ruang) {
		this.ruang = ruang;
	}
}
