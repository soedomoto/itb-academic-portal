package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="dosenmatkul")
public class TDosenMatkul {
	@DatabaseField(generatedId=true)
	Integer kdDosenMatkul;
	@DatabaseField(columnName="kdDosen", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references dosen(kdDosen) on delete restrict")
	TDosen dosen;
	@DatabaseField(columnName="kdMatkul", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references matakuliah(kdMatkul) on delete restrict")
	TMataKuliah matkul;
}
