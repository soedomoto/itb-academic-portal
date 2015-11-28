package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="user")
public class TUser {
	@DatabaseField(generatedId=true)
	Integer id;
	@DatabaseField(canBeNull=true)
	String username;
	@DatabaseField(canBeNull=true)
	String password;
	@DatabaseField(canBeNull=false)
	String fullname;
	@DatabaseField(canBeNull=true)
	String nip;
	
	public TUser() {}
}
