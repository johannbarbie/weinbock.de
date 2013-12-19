package de.weinbock.test.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.restnucleus.dao.GenericRepository;
import org.restnucleus.dao.RNQuery;

import com.google.inject.Inject;

import de.weinbock.persistence.dto.Lead;

@Path(LeadAdminResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class LeadAdminResource {
	public static final String PATH = "/leadAdmin";
	
	@Inject protected GenericRepository dao;
	
	@Inject protected RNQuery query;
	
	@GET
	@Path("/{id}")
	public Lead read(@PathParam("id") Long id) {
		//TODO: add authentication
		Lead rv =  dao.getObjectById(id, Lead.class);
		rv.getEmail();
		rv.getCampaigns();
		return rv;
	}

}
