package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="absensidosen")
public class TAbsensiDosen {
	@DatabaseField(generatedId=true)
	Integer kdAbsensi;
	@DatabaseField(columnName="kdDosen", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references dosen(kdDosen) on delete restrict")
	TDosen dosen;
	@DatabaseField(columnName="kdPelaksanaan", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references pelaksanaankuliah(kdPelaksanaan) on delete restrict")
	TPelaksanaanKuliah pelaksanaan;
	@DatabaseField
	String materi;
	@DatabaseField
	String statusHadir;
}
