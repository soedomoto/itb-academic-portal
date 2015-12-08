package id.ac.itb.academic.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="jawaban")
public class TJawabanTugas {
	@DatabaseField(generatedId=true)
	Integer kdJawaban;
	@DatabaseField(columnName="kdTUgas", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references tugas(kdTugas) on delete restrict")
	TTugas tugas;
	@DatabaseField(columnName="nim", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references mahasiswa(nim) on delete restrict")
	TMahasiswa mahasiswa;
	@DatabaseField
	String jawaban;
	@DatabaseField
	Date tanggalKumpul;
	@DatabaseField
	Integer nilai;
}
