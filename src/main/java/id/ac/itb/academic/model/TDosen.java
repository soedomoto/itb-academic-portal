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
	@DatabaseField(foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references akun(username) on delete restrict")
	TAkun akun;
}
