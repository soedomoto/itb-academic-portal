package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="fakultas")
public class TFakultas {
	@DatabaseField(id=true)
	String kdFakultas;
	@DatabaseField
	String fakultas;
}
