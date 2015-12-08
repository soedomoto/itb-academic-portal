package id.ac.itb.academic.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="pengumuman")
public class TPengumuman {
	@DatabaseField(generatedId=true)
	Integer kdPengumuman;
	@DatabaseField(columnName="kdDosen", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references dosen(kdDosen) on delete restrict")
	TDosen kdDosen;
	@DatabaseField
	String judulPengumuman;
	@DatabaseField
	String isiPengumuman;
	@DatabaseField
	Date tanggalPengumuman;
	@DatabaseField
	Date tanggalEvent;
}
