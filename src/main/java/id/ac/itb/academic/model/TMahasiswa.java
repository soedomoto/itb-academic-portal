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
}
