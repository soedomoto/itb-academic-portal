package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="dosenmatkul")
public class TDosenMatkul {
	@DatabaseField(id=true)
	Integer kdDosenMatkul;
	@DatabaseField(columnName="kdDosen", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references dosen(kdDosen) on delete restrict")
	TDosen dosen;
	@DatabaseField(columnName="kdMatkul", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references matakuliah(kdMatkul) on delete restrict")
	TMataKuliah matkul;
	
	public TDosenMatkul() {}

	public TDosenMatkul(TDosen dosen, TMataKuliah matkul) {
		super();
		this.dosen = dosen;
		this.matkul = matkul;
	}

	public Integer getKdDosenMatkul() {
		return kdDosenMatkul;
	}

	public void setKdDosenMatkul(Integer kdDosenMatkul) {
		this.kdDosenMatkul = kdDosenMatkul;
	}

	public TDosen getDosen() {
		return dosen;
	}

	public void setDosen(TDosen dosen) {
		this.dosen = dosen;
	}

	public TMataKuliah getMatkul() {
		return matkul;
	}

	public void setMatkul(TMataKuliah matkul) {
		this.matkul = matkul;
	}
}
