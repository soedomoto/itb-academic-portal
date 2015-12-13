package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="dosen")
public class TDosen {
	@DatabaseField(id=true)
	String kdDosen;
	@DatabaseField
	String nama;
	@DatabaseField
	String nip;
	@DatabaseField
	String alamat;
	@DatabaseField(columnName="username", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references akun(username) on delete restrict")
	TAkun akun;
	
	public TDosen() {
		// TODO Auto-generated constructor stub
	}

	public String getKdDosen() {
		return kdDosen;
	}

	public void setKdDosen(String kdDosen) {
		this.kdDosen = kdDosen;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNip() {
		return nip;
	}

	public void setNip(String nip) {
		this.nip = nip;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public TAkun getAkun() {
		return akun;
	}

	public void setAkun(TAkun akun) {
		this.akun = akun;
	}
}
