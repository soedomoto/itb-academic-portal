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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getNip() {
		return nip;
	}

	public void setNip(String nip) {
		this.nip = nip;
	}
}
