package id.ac.itb.academic.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="pelaksanaankuliah")
public class TPelaksanaanKuliah {
	@DatabaseField(generatedId=true)
	Integer kdPelaksanaan;
	@DatabaseField(columnName="kdJadwal", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references jadwalkuliah(kdJadwalKuliah) on delete restrict")
	TJadwalKuliah jadwal;
	@DatabaseField
	Date tanggal;
	@DatabaseField
	Integer sesi;
	@DatabaseField
	String ruang;
}
