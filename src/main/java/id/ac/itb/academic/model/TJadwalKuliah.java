package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="jadwalkuliah")
public class TJadwalKuliah {
	@DatabaseField(generatedId=true)
	Integer kdJadwalKuliah;
	@DatabaseField(columnName="kdDosenMatkul", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references dosenmatkul(kdDosenMatkul) on delete restrict")
	TDosenMatkul dosenMatkul;
	@DatabaseField
	String hari;
	@DatabaseField
	Integer sesi;
	@DatabaseField
	String ruang;
	
	public TJadwalKuliah() {}

	public TJadwalKuliah(TDosenMatkul dosenMatkul, String hari, Integer sesi, String ruang) {
		super();
		this.dosenMatkul = dosenMatkul;
		this.hari = hari;
		this.sesi = sesi;
		this.ruang = ruang;
	}

	public Integer getKdJadwalKuliah() {
		return kdJadwalKuliah;
	}

	public void setKdJadwalKuliah(Integer kdJadwalKuliah) {
		this.kdJadwalKuliah = kdJadwalKuliah;
	}

	public TDosenMatkul getDosenMatkul() {
		return dosenMatkul;
	}

	public void setDosenMatkul(TDosenMatkul dosenMatkul) {
		this.dosenMatkul = dosenMatkul;
	}

	public String getHari() {
		return hari;
	}

	public void setHari(String hari) {
		this.hari = hari;
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
