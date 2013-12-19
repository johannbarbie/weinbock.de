package de.weinbock.test.resources;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.restnucleus.dao.GenericRepository;
import org.restnucleus.dao.RNQuery;

import com.google.inject.Inject;

import de.weinbock.persistence.dto.Email;
import de.weinbock.persistence.dto.Lead;

@Path(EmailResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class EmailResource {
	public static final String PATH = "/email";
	public static final String CONFIRM = "/confirm";
	public static final String CHECK = "/check";
	public static final String DEACTIVATE = "/deactivate";
	public static final String PATH_CONFIRM = PATH + CONFIRM;
	public static final String PATH_CHECK = PATH + CHECK;
	public static final String PATH_DEACTIVATE = PATH + DEACTIVATE;
	
	public static String createEmailSecret() {
		Random rand = new Random();
		String es =  String.valueOf(rand.nextInt());
		return es;
	}
	
	
	//Regular Expression Test
	public static boolean isValidRegex(String email){
		String regex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		if(m.find()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static String getHostName(String email){
		String hostName = email.substring(email.indexOf("@") + 1, email.length());
		return hostName;
	}

	//from http://www.rgagnon.com/javadetails/java-0452.html
	//MX (Mail Exchange) Domain Test
	//also used http://www.tomred.net/tutorials/tomred-java-extended-email-validation-using-dns-mx-lookup.html
	public static boolean isValidMX(String email) throws NamingException{
			String hostName = getHostName(email);
			// is it one of the common domains. 
			String [] hosts = {"gmail.com","hotmail.com","googlemail.com","yahoo.com"};
			for (String host : hosts)  
				if(hostName.trim().equalsIgnoreCase(host))
					return true;
		    Hashtable<String,String> env = new Hashtable<>();
		    env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
		    DirContext ictx = new InitialDirContext( env );
		    Attributes attrs = ictx.getAttributes(hostName, new String[] { "MX" });
		    Attribute attr = attrs.get( "MX" );

		    if( attr == null ){
		    	return false;
		    }
		    else{
		    	return true;
		    }
	}
	

	@Inject protected GenericRepository dao;
	
	@POST
	public Long create(Email email){
		email = createEmail(email.getAddress());
		//check existing
		if (email.getId() != null)
			throw new WebApplicationException("this address exists already.", Response.Status.CONFLICT);
		dao.add(email);
		return email.getId();		
	}
	
	public Email createEmail(String addr){
		if (addr == null){
			throw new WebApplicationException("email mandadory.", Response.Status.BAD_REQUEST);
		}
		checkEmail(addr);
		//check existing
		RNQuery q = new RNQuery().addFilter("address", addr);
		Email existing = dao.queryEntity(q, Email.class, false);
		if (existing != null)
			return existing;
		//prepare new
		Email newMail = new Email().setAddress(addr)
			.setHostName(getHostName(addr)).setSecret(createEmailSecret());
		return newMail;
	}

	@Path(CONFIRM)
	@PUT
	public Long confirm(@QueryParam("secret") String secret)  {
		if (secret == null){
			throw new WebApplicationException("no secret passed", Response.Status.BAD_REQUEST);
		}
		RNQuery q = new RNQuery().addFilter("secret", secret);
		Email email = dao.queryEntity(q,Email.class);
		email.setConfirmationTime(new Date());
		email.setConfirmationIp(Request.getCurrent().getClientInfo().getAddress());
		email.setIsActive(true);
		email.setSecret(null);
		return email.getId();
	}
		
	@Path(CHECK)
	@GET
	public void checkEmail(@QueryParam("email") String email){
		//check regex
		if (null==email || !isValidRegex(email)){
			throw new WebApplicationException("send a valid email plz :D", Response.Status.BAD_REQUEST);
		}
		//check db for active email with same domain
		RNQuery q = new RNQuery()
			.addFilter("hostName", getHostName(email))
			.addFilter("isActive", true);
		List<Email> actives = dao.queryList(q, Email.class);
		if (actives.size() > 0)
			return;
		//check host mx record
		boolean isValidMX = false;
		try{
			isValidMX = isValidMX(email);
		}catch(Exception e){
			System.out.println("EmailRes.->check: "+email + " not valid due: " + e.getMessage());
		}
		if(!isValidMX ){
			throw new WebApplicationException("This email's hostname does not have mx record.", Response.Status.EXPECTATION_FAILED);
		}
	}
	
	@Path(DEACTIVATE)
	@GET
	public void deactivate(@QueryParam("email") String email, @QueryParam("salt") Long leadId){
		//the lead id is meant to protect from abuse
		Lead lead = dao.getObjectById(leadId, Lead.class);
		if (!lead.getEmail().getAddress().equalsIgnoreCase(email)){
			throw new WebApplicationException("lead email and email don't match.", Response.Status.EXPECTATION_FAILED);
		}
		lead.getEmail().setIsActive(false);
	}

}
