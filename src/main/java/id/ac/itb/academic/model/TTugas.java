package id.ac.itb.academic.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="tugas")
public class TTugas {
	@DatabaseField(generatedId=true)
	Integer kdTugas;
	@DatabaseField(columnName="kdPelaksanaan", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references pelaksanaankuliah(kdPelaksanaan) on delete restrict")
	TPelaksanaanKuliah pelaksanaan;
	@DatabaseField
	String deskripsi;
	@DatabaseField
	String soal;
	@DatabaseField
	Date tanggalTugas;
}
