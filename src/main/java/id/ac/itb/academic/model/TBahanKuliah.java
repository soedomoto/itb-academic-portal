package id.ac.itb.academic.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="bahankuliah")
public class TBahanKuliah {
	@DatabaseField(generatedId=true)
	Integer kdBahan;
	@DatabaseField(columnName="kdPelaksanaan", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references pelaksanaankuliah(kdPelaksanaan) on delete restrict")
	TPelaksanaanKuliah pelaksanaan;
	@DatabaseField
	String file;
	@DatabaseField
	String path;
	@DatabaseField
	String deskripsi;
	@DatabaseField
	Date tanggalUpload;
}
