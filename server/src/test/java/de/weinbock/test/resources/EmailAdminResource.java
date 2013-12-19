package de.weinbock.test.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.restnucleus.dao.GenericRepository;
import org.restnucleus.dao.RNQuery;

import com.google.inject.Inject;

import de.weinbock.persistence.dto.Email;

@Path(EmailAdminResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class EmailAdminResource {
	public static final String PATH = "/emailAdmin";
	
	@Inject protected GenericRepository dao;
	
	@Inject protected RNQuery query;
	
	@GET
	public List<Email> read() {
		//TODO: add authentication
		List<Email> rv = dao.queryList(query, Email.class);
		return rv;
	}

}
