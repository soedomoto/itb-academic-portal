package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="opsi")
public class TOpsi {
	@DatabaseField(id=true)
	String kdOpsi;
	@DatabaseField
	String opsi;
	@DatabaseField(columnName="kdJurusan", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references jurusan(kdJurusan) on delete restrict")
	TProdi jurusan;
	
	public TOpsi() {
		// TODO Auto-generated constructor stub
	}

	public String getKdOpsi() {
		return kdOpsi;
	}

	public void setKdOpsi(String kdOpsi) {
		this.kdOpsi = kdOpsi;
	}

	public String getOpsi() {
		return opsi;
	}

	public void setOpsi(String opsi) {
		this.opsi = opsi;
	}

	public TProdi getJurusan() {
		return jurusan;
	}

	public void setJurusan(TProdi jurusan) {
		this.jurusan = jurusan;
	}
}
