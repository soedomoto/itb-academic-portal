package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="matakuliah")
public class TMataKuliah {
	@DatabaseField(id=true)
	String kdMatkul;
	@DatabaseField
	String nama;
	@DatabaseField
	Integer sks;
	/*@DatabaseField(columnName="kdProdi", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references prodi(kdProdi) on delete restrict")
	TProdi prodi;
	@DatabaseField(columnName="kdOpsi", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references opsi(kdOpsi) on delete restrict")
	TOpsi opsi;*/
	
	public TMataKuliah() {}

	public TMataKuliah(String nama, Integer sks/*, TProdi prodi, TOpsi opsi*/) {
		super();
		this.nama = nama;
		this.sks = sks;
		/*this.prodi = prodi;
		this.opsi = opsi;*/
	}

	public String getKdMatkul() {
		return kdMatkul;
	}

	public void setKdMatkul(String kdMatkul) {
		this.kdMatkul = kdMatkul;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Integer getSks() {
		return sks;
	}

	public void setSks(Integer sks) {
		this.sks = sks;
	}

	/*public TProdi getProdi() {
		return prodi;
	}

	public void setProdi(TProdi prodi) {
		this.prodi = prodi;
	}

	public TOpsi getOpsi() {
		return opsi;
	}

	public void setOpsi(TOpsi opsi) {
		this.opsi = opsi;
	}*/
}
