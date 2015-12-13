package id.ac.itb.academic.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/*
 * Lutfi Rahmatuti M.
 * 23216046
 */

@Path("profil")
public class ProfileService {

	@GET
	@Path("/read/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, String> profil(@PathParam("username") String username) {
		HashMap<String, String> profil = new HashMap<>();
		String host = "localhost";
		String port = "3306";
		String db = "itbakademik";
		String user = "root";
		String pw = "r00t";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pw);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		String sqlProfilMahasiswa = "SELECT `nim`, `nama`, `username`, `Opsi`, `Prodi` FROM (`mahasiswa` a left join opsi b on a.kdOpsi=b.kdOpsi), Prodi c WHERE a.kdProdi=c.kdProdi and username='"
				+ username + "'";
		String sqlProfilDosen = "select * from dosen where username='" + username + "'";
		try {
			Statement stMahasiswa = conn.createStatement();
			ResultSet rsMahasiswa = stMahasiswa.executeQuery(sqlProfilMahasiswa);
			if (rsMahasiswa.next()) {
				profil.put("nim", rsMahasiswa.getString(1));
				profil.put("nama", rsMahasiswa.getString(2));
				profil.put("opsi", rsMahasiswa.getString(4));
				profil.put("prodi", rsMahasiswa.getString(5));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		try {
			ResultSet rsDosen = conn.createStatement().executeQuery(sqlProfilDosen);
			if (rsDosen.next()) {
				profil.put("kode", rsDosen.getString(1));
				profil.put("nama", rsDosen.getString(2));
				profil.put("nip", rsDosen.getString(3));
				profil.put("alamat", rsDosen.getString(4));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return profil;
	}

	@GET
	@Path("/readMahasiswa/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, String> profilMahasiswa(@PathParam("username") String username) {
		HashMap<String, String> profil = new HashMap<>();
		String host = "localhost";
		String port = "3306";
		String db = "itbakademik";
		String user = "root";
		String pw = "r00t";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pw);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		String sqlProfilMahasiswa = "SELECT `nim`, `nama`, `username`, `Opsi`, `Prodi` FROM (`mahasiswa` a left join opsi b on a.kdOpsi=b.kdOpsi), Prodi c WHERE a.kdProdi=c.kdProdi and username='"
				+ username + "'";
		try {
			ResultSet rsMahasiswa = conn.createStatement().executeQuery(sqlProfilMahasiswa);
			if (rsMahasiswa.next()) {
				profil.put("nim", rsMahasiswa.getString(1));
				profil.put("nama", rsMahasiswa.getString(2));
				profil.put("opsi", rsMahasiswa.getString(4));
				profil.put("prodi", rsMahasiswa.getString(5));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return profil;
	}

	@GET
	@Path("/readDosen/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, String> profilDosen(@PathParam("username") String username) {
		HashMap<String, String> profil = new HashMap<>();
		String host = "localhost";
		String port = "3306";
		String db = "itbakademik";
		String user = "root";
		String pw = "r00t";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pw);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		String sqlProfilDosen = "select * from dosen where username='" + username + "'";

		try {
			ResultSet rsDosen = conn.createStatement().executeQuery(sqlProfilDosen);
			if (rsDosen.next()) {
				profil.put("kode", rsDosen.getString(1));
				profil.put("nama", rsDosen.getString(2));
				profil.put("nip", rsDosen.getString(3));
				profil.put("alamat", rsDosen.getString(4));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return profil;
	}
}