package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="nilai")
public class TNilai {
	@DatabaseField(generatedId=true)
	Integer kdNilai;
	@DatabaseField(columnName="kdDosenMatkul", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references dosenmatkul(kdDosenMatkul) on delete restrict")
	TDosenMatkul dosenMatkul;
	@DatabaseField(columnName="nim", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references mahasiswa(nim) on delete restrict")
	TMahasiswa mahasiswa;
	@DatabaseField
	Integer nilaiTugas;
	@DatabaseField
	Integer nilaiUTS;
	@DatabaseField
	Integer nilaiUAS;
	@DatabaseField
	Integer nilaiTotal;
	@DatabaseField
	String grade;
}
