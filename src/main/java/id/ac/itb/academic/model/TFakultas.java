package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="fakultas")
public class TFakultas {
	@DatabaseField(id=true)
	String kdFakultas;
	@DatabaseField
	String fakultas;
	
	public TFakultas() {
		// TODO Auto-generated constructor stub
	}

	public String getKdFakultas() {
		return kdFakultas;
	}

	public void setKdFakultas(String kdFakultas) {
		this.kdFakultas = kdFakultas;
	}

	public String getFakultas() {
		return fakultas;
	}

	public void setFakultas(String fakultas) {
		this.fakultas = fakultas;
	}
}
