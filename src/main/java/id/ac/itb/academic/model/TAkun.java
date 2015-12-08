package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="akun")
public class TAkun {
	@DatabaseField(id=true)
	String username;
	@DatabaseField
	String password;
}
