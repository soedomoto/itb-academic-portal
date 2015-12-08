package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="absensimahasiswa")
public class TAbsensiMahasiswa {
	@DatabaseField(generatedId=true)
	Integer kdAbsensi;
	@DatabaseField(foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references mahasiswa(nim) on delete restrict")
	TMahasiswa mahasiswa;
	@DatabaseField(foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references pelaksanaankuliah(kdPelaksanaan) on delete restrict")
	TPelaksanaanKuliah pelaksanaan;
	@DatabaseField
	String statusHadir;
}
