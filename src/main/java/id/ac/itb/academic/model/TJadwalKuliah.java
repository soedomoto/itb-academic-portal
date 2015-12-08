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
}
