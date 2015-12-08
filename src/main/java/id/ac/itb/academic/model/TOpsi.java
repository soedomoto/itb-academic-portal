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
}
