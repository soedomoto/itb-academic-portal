package id.ac.itb.academic.service;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.j256.ormlite.dao.Dao;

import id.ac.itb.academic.model.TUser;

@Path("api/user")
public class UserService {
	Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	Dao<TUser, Object> userDao;
	
	@GET
	@Path("list")
	public Response list () {
		try {
			List<TUser> allUsers = userDao.queryForAll();
			return Response.ok(allUsers).build();
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Something wrong happened").build();
		}
	}

}
