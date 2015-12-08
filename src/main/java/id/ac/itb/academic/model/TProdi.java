package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="prodi")
public class TProdi {
	@DatabaseField
	String kdProdi;
	@DatabaseField
	String prodi;
	@DatabaseField(columnName="kdFakultas", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references fakultas(kdFakultas) on delete restrict")
	TFakultas fakultas;
}
