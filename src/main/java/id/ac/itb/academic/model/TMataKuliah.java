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
	@DatabaseField(columnName="kdProdi", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references prodi(kdProdi) on delete restrict")
	TProdi prodi;
	@DatabaseField(columnName="kdOpsi", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references opsi(kdOpsi) on delete restrict")
	TOpsi opsi;
}
