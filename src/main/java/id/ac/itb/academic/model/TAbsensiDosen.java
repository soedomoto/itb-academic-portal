package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="absensidosen")
public class TAbsensiDosen {
	@DatabaseField(generatedId=true)
	Integer kdAbsensi;
	@DatabaseField(columnName="kdDosen", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references dosen(kdDosen) on delete restrict")
	TDosen dosen;
	@DatabaseField(columnName="kdPelaksanaan", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references pelaksanaankuliah(kdPelaksanaan) on delete restrict")
	TPelaksanaanKuliah pelaksanaan;
	@DatabaseField
	String materi;
	@DatabaseField
	String statusHadir;
	
	public TAbsensiDosen() {
		// TODO Auto-generated constructor stub
	}

	public Integer getKdAbsensi() {
		return kdAbsensi;
	}

	public void setKdAbsensi(Integer kdAbsensi) {
		this.kdAbsensi = kdAbsensi;
	}

	public TDosen getDosen() {
		return dosen;
	}

	public void setDosen(TDosen dosen) {
		this.dosen = dosen;
	}

	public TPelaksanaanKuliah getPelaksanaan() {
		return pelaksanaan;
	}

	public void setPelaksanaan(TPelaksanaanKuliah pelaksanaan) {
		this.pelaksanaan = pelaksanaan;
	}

	public String getMateri() {
		return materi;
	}

	public void setMateri(String materi) {
		this.materi = materi;
	}

	public String getStatusHadir() {
		return statusHadir;
	}

	public void setStatusHadir(String statusHadir) {
		this.statusHadir = statusHadir;
	}
}
