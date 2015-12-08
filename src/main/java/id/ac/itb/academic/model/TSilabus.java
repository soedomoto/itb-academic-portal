package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="silabus")
public class TSilabus {
	@DatabaseField(generatedId=true)
	Integer kdSilabus;
	@DatabaseField(columnName="kdMatkul", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references matakuliah(kdMatkul) on delete restrict")
	TMataKuliah matkul;
	@DatabaseField
	Integer pertemuan;
	@DatabaseField
	String materi;
	@DatabaseField
	String file;
}
