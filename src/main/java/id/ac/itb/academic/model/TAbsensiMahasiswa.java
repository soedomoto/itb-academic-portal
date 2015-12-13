package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="absensimahasiswa")
public class TAbsensiMahasiswa {
	@DatabaseField(generatedId=true)
	Integer kdAbsensi;
	@DatabaseField(columnName="nim", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references mahasiswa(nim) on delete restrict")
	TMahasiswa mahasiswa;
	@DatabaseField(columnName="kdPelaksanaan",  foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references pelaksanaankuliah(kdPelaksanaan) on delete restrict")
	TPelaksanaanKuliah pelaksanaan;
	@DatabaseField
	String statusHadir;
	
	public TAbsensiMahasiswa() {
		// TODO Auto-generated constructor stub
	}

	public Integer getKdAbsensi() {
		return kdAbsensi;
	}

	public void setKdAbsensi(Integer kdAbsensi) {
		this.kdAbsensi = kdAbsensi;
	}

	public TMahasiswa getMahasiswa() {
		return mahasiswa;
	}

	public void setMahasiswa(TMahasiswa mahasiswa) {
		this.mahasiswa = mahasiswa;
	}

	public TPelaksanaanKuliah getPelaksanaan() {
		return pelaksanaan;
	}

	public void setPelaksanaan(TPelaksanaanKuliah pelaksanaan) {
		this.pelaksanaan = pelaksanaan;
	}

	public String getStatusHadir() {
		return statusHadir;
	}

	public void setStatusHadir(String statusHadir) {
		this.statusHadir = statusHadir;
	}
}
