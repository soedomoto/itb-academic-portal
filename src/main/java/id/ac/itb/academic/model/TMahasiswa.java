package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="mahasiswa")
public class TMahasiswa {
	@DatabaseField(id=true)
	String nim;
	@DatabaseField
	String nama;
	@DatabaseField(columnName="kdProdi", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references prodi(kdProdi) on delete restrict")
	TProdi prodi;
	@DatabaseField(columnName="kdOpsi", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references opsi(kdOpsi) on delete restrict")
	TOpsi opsi;
	@DatabaseField(columnName="username", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references akun(username) on delete restrict")
	TAkun akun;
	
	public TMahasiswa() {
		// TODO Auto-generated constructor stub
	}

	public String getNim() {
		return nim;
	}

	public void setNim(String nim) {
		this.nim = nim;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public TProdi getProdi() {
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
	}

	public TAkun getAkun() {
		return akun;
	}

	public void setAkun(TAkun akun) {
		this.akun = akun;
	}
}
