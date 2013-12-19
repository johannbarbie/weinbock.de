package de.weinbock.test.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.restnucleus.dao.GenericRepository;
import org.restnucleus.dao.RNQuery;

import com.google.inject.Inject;

import de.weinbock.persistence.dto.Account;


@Path(HealthCheckResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class HealthCheckResource {
	public final static String PATH = "/healthcheck";
	
	@Inject protected GenericRepository dao;

	@GET
	public Map<String,String> healthcheck(){
		dao.count(new RNQuery(), Account.class);
		Map<String,String> rv = new HashMap<String, String>(1);
		rv.put("status", "ok!");
		return rv;
	}
	
}
