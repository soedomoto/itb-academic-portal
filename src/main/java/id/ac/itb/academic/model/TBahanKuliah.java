package id.ac.itb.academic.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="bahankuliah")
public class TBahanKuliah {
	@DatabaseField(generatedId=true)
	Integer kdBahan;
	@DatabaseField(columnName="kdPelaksanaan", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references pelaksanaankuliah(kdPelaksanaan) on delete restrict")
	TPelaksanaanKuliah pelaksanaan;
	@DatabaseField
	String file;
	@DatabaseField
	String path;
	@DatabaseField
	String deskripsi;
	@DatabaseField
	Date tanggalUpload;
	
	public TBahanKuliah() {}
	
	public TBahanKuliah(TPelaksanaanKuliah pelaksanaan, String file, String path, String deskripsi,
			Date tanggalUpload) {
		super();
		this.pelaksanaan = pelaksanaan;
		this.file = file;
		this.path = path;
		this.deskripsi = deskripsi;
		this.tanggalUpload = tanggalUpload;
	}

	public Integer getKdBahan() {
		return kdBahan;
	}

	public void setKdBahan(Integer kdBahan) {
		this.kdBahan = kdBahan;
	}

	public TPelaksanaanKuliah getPelaksanaan() {
		return pelaksanaan;
	}

	public void setPelaksanaan(TPelaksanaanKuliah pelaksanaan) {
		this.pelaksanaan = pelaksanaan;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public Date getTanggalUpload() {
		return tanggalUpload;
	}

	public void setTanggalUpload(Date tanggalUpload) {
		this.tanggalUpload = tanggalUpload;
	}
}
