package id.ac.itb.academic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="prodi")
public class TProdi {
	@DatabaseField(id=true)
	String kdProdi;
	@DatabaseField
	String prodi;
	@DatabaseField(columnName="kdFakultas", foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true, 
			columnDefinition="varchar references fakultas(kdFakultas) on delete restrict")
	TFakultas fakultas;
	
	public TProdi() {
		// TODO Auto-generated constructor stub
	}

	public String getKdProdi() {
		return kdProdi;
	}

	public void setKdProdi(String kdProdi) {
		this.kdProdi = kdProdi;
	}

	public String getProdi() {
		return prodi;
	}

	public void setProdi(String prodi) {
		this.prodi = prodi;
	}

	public TFakultas getFakultas() {
		return fakultas;
	}

	public void setFakultas(TFakultas fakultas) {
		this.fakultas = fakultas;
	}
}
