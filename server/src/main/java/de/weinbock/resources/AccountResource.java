package de.weinbock.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.restnucleus.dao.GenericRepository;
import org.restnucleus.dao.RNQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.weinbock.dao.Account;
import de.weinbock.pojo.AccountPolicy;
import de.weinbock.pojo.AccountRequest;
import de.weinbock.pojo.PasswordRequest;


@Path(AccountResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
	public final static String PATH = "/account";
	public static Logger log = LoggerFactory.getLogger(AccountResource.class);
	
	private final Cache cache;
	
	private final GenericRepository dao;
	
	private final HttpServletRequest httpReq;
	
	private final AccountPolicy accountPolicy;

	@Inject
	public AccountResource(Cache cache,
			ServletRequest request,
			AccountPolicy accountPolicy){
		this.cache = cache;
		httpReq = (HttpServletRequest)request;
		dao = (GenericRepository)httpReq.getAttribute("gr");
		this.accountPolicy = accountPolicy;
	}
	
	/**
	 * allow front-end to notify user about taken account 
	 * @param email
	 */
	@GET
	@Path("/check")
	public void checkEmail(@QueryParam("email") String email){
		//check it's a valid email
		if (!AccountPolicy.isValidEmail(email)){
			throw new WebApplicationException("email not valid", Response.Status.BAD_REQUEST);
		}
		//how to avoid account fishing?
		Element e = cache.get(getRemoteAddress());
		if (e!=null){
			if (e.getHitCount()>50){
				throw new WebApplicationException("to many requests", Response.Status.FORBIDDEN);
			}
		}
		//check it's not taken already
		Account a = dao.queryEntity(new RNQuery().addFilter("email", email), Account.class, false);
		if (null!=a){
			throw new WebApplicationException("email used", Response.Status.CONFLICT);
		}
	}
	
	@GET
	@Path("/profile")
	@RolesAllowed({"customer"})
	public Account getProfile(@Context SecurityContext context){
		RNQuery q = new RNQuery().addFilter("email", context.getUserPrincipal().getName());
		Account a = dao.queryEntity(q, Account.class);
		return a.setRoles(new String[]{"customer"});
	}
	
	/**
	 * an account-request is validated, and cached, then email is send
	 * @param accountRequest
	 */
	@POST
	public void register(AccountRequest accountRequest){
		// no ticket, no service
		if (null==cache.get("ticket"+accountRequest.getTicket())){
			log.debug("ticket required for this operation.");
			throw new WebApplicationException("ticket required for this operation.", Response.Status.BAD_REQUEST);
		}
		Account a = new Account()
			.setEmail(accountRequest.getEmail())
			.setPassword(accountRequest.getPassword());
		//#############validate email#################
		//check regex
		if (null==a.getEmail() || !AccountPolicy.isValidEmail(a.getEmail())){
			log.debug("send a valid email plz :D");
			throw new WebApplicationException("send a valid email plz :D", Response.Status.BAD_REQUEST);
		}
		if (accountPolicy.isEmailMxLookup()){
			//check db for active email with same domain
			RNQuery q = new RNQuery()
				.addFilter("hostName", a.getHostName());
			List<Account> accounts = dao.queryList(q, Account.class);
			if (accounts==null || accounts.size() == 0){
				//check host mx record
				boolean isValidMX = false;
				try{
					isValidMX = AccountPolicy.isValidMX(a.getEmail());
				}catch(Exception e){
					System.out.println("EmailRes.->check: "+ a.getEmail() + " not valid due: " + e.getMessage());
				}
				if(!isValidMX ){
					throw new WebApplicationException("This email's hostname does not have mx record.", Response.Status.EXPECTATION_FAILED);
				}
			}
		}
		//################validate password############
		boolean isValid = accountPolicy.validatePassword(a.getPassword());
		if (!isValid){
			log.debug("password does not pass account policy");
			throw new WebApplicationException("password does not pass account policy", Response.Status.BAD_REQUEST);
		}
		//put it into cache, and wait for email validation
		String token = RandomStringUtils.random(14, "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ123456789");
		sendCreateEmail(accountRequest.getEmail() ,token);
		AccountRequest ar = new AccountRequest()
			.setEmail(a.getEmail())
			.setPassword(a.getPassword());
		cache.put(new Element("create"+token,ar));
	}
	
	/**
	 * an account-request is taken from cache and put into the database
	 * @param accountRequest
	 */
	@POST
	@Path("/create")
	public void createAccount(AccountRequest accountRequest){
		Element e = cache.get("create"+accountRequest.getToken());
		if (null!=e){
			accountRequest = (AccountRequest)e.getObjectValue();
			Account a = new Account()
				.setEmail(accountRequest.getEmail())
				.setPassword(accountRequest.getPassword());
			dao.add(a);
			cache.remove("create"+accountRequest.getToken());
		}else{
			throw new WebApplicationException("not found or expired", Response.Status.NOT_FOUND);
		}
	}

	
	private String getRemoteAddress(){
		String ip = httpReq.getRemoteAddr();
		//TODO: actually we need to check the proxy header too
		return ip;
	}
	
	/**
	 * a password-request is validated, cached and email send out
	 * @param pwRequest
	 */
	@POST
	@Path("/password/request")
	public void requestPwReset(PasswordRequest pwRequest){
		// no ticket, no service
		Element e = cache.get("ticket"+pwRequest.getTicket());
		if (null==e){
			throw new WebApplicationException("ticket required for this request.", Response.Status.BAD_REQUEST);
		}else{
			if (e.getHitCount()>3){
				cache.remove("ticket"+pwRequest.getTicket());
				throw new WebApplicationException("to many requests", Response.Status.BAD_REQUEST);
			}
		}
		//fetch account by email, then send email
		Account a = dao.queryEntity(new RNQuery().addFilter("email", pwRequest.getEmail()), Account.class,false);
		if (a == null)
			throw new WebApplicationException("account not found", Response.Status.NOT_FOUND);
		String token = RandomStringUtils.random(14, "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ123456789");
		sendResetEmail(pwRequest.getEmail(), token);
		PasswordRequest pwr = new PasswordRequest().setToken(token).setAccountId(a.getId());
		cache.put(new Element("reset"+token, pwr));
	}
	
	@GET
	@Path("/password/ticket")
	public Pair<String,String> validateToken(@QueryParam("ticket") String ticket){
		Element e = cache.get("reset"+ticket);
		if (null!=e){
			return Pair.of("status", "active");
		}else{
			return Pair.of("status", "inactive");
		}
	}
	
	/**
	 * a password-request is taken from the cache and executed, then account-changes persisted
	 * @param pwRequest
	 */
	@POST
	@Path("/password/reset")
	public void reset(PasswordRequest pwRequest){
		Element e = cache.get("reset"+pwRequest.getToken());
		if (null!=e){
			String newPw = pwRequest.getPassword();
			boolean isValid = accountPolicy.validatePassword(newPw);
			if (!isValid)
				throw new WebApplicationException("password does not pass account policy", Response.Status.BAD_REQUEST);
			pwRequest = (PasswordRequest)e.getObjectValue();
			Account a = dao.getObjectById(pwRequest.getAccountId(), Account.class);
			a.setPassword(newPw);
			cache.remove("reset"+pwRequest.getToken());
		}else{
			throw new WebApplicationException("not found or expired", Response.Status.NOT_FOUND);
		}
	}
	
	private void sendResetEmail(String email, String token){
		System.out.println(token);
		//TODO: send email here
	}
	
	private void sendCreateEmail(String email, String token){
		System.out.println(token);
		//TODO: send email here
	}
	
}
