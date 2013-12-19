package de.weinbock.test.resources;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Hex;
import org.restnucleus.dao.GenericRepository;
import org.restnucleus.dao.RNQuery;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Injector;

import de.weinbock.persistence.dto.Email;
import de.weinbock.persistence.dto.Lead;

@Path(LeadResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class LeadResource {
	public static final String PATH = "/lead";
	public static final String CANCEL = "/cancel";
	public static final String SECRET = "/secret";
	public static final String PATH_CANCEL = PATH + CANCEL;
	public static final String PATH_SECRET = PATH + SECRET;
	private static final String SECRET_KEY = "kauibauibo!";
	private static SecretKey S_KEY = null;
	static {
		SecretKeyFactory factory = null;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(),
					SECRET_KEY.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			S_KEY = new SecretKeySpec(tmp.getEncoded(), "AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String ENCRYPT(String message) throws Exception {
		Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aes.init(Cipher.ENCRYPT_MODE, S_KEY);
		byte[] ciphertext = aes.doFinal(message.getBytes());
		return Hex.encodeHexString(ciphertext);
	}

	public static String DECRYPT(String cipher) throws Exception {
		Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aes.init(Cipher.DECRYPT_MODE, S_KEY);
		byte[] ciphertext = Hex.decodeHex(cipher.toCharArray());
		String cleartext = new String(aes.doFinal(ciphertext));
		return cleartext;
	}
	
	@Inject protected GenericRepository dao;
	@Inject protected Injector injector;
	
	@POST
	public long create(Lead lead) {
		if (lead.getGivenName()==null || lead.getEmail() ==null || lead.getEmail().getAddress() == null 
				|| lead.getHint() == null || lead.getCampaigns()==null || lead.getCampaigns().size()<1){
			throw new WebApplicationException("mandatory data missing", Response.Status.BAD_REQUEST);
		}
		//Turing task has to be solved correctly within 10 minutes
		Map<String,Long> input = null;
		try{
			input = new ObjectMapper().readValue(
					DECRYPT(lead.getHint()),
					new TypeReference<Map<String,Long>>() {});
		}catch(Exception e){
			e.printStackTrace();
		}
		if ( lead.getSum() == null  || input == null || input.get("a") == null 
			|| input.get("b") == null || input.get("a") + input.get("b") != lead.getSum()){
			throw new WebApplicationException("Turing test failed", Response.Status.EXPECTATION_FAILED);
		}
		if (input.get("salt") == null 
			|| System.currentTimeMillis() - input.get("salt") > 1000L * 60L * 10L){
			throw new WebApplicationException("Turing test timed out.", Response.Status.EXPECTATION_FAILED);
		}
		//reuse email resource to hande email address
		EmailResource emailResource = injector.getInstance(EmailResource.class);
		Email email = emailResource.createEmail(lead.getEmail().getAddress()); 
		//email existed already
		if (email != null && email.getId() != null){
			//also get existing lead then
			RNQuery q = new RNQuery().addQueryObject("email", email);
			Lead existing = dao.queryEntity(q, Lead.class);
			//reusing existing object
			for (String campaign: lead.getCampaigns()){
				if (!existing.getCampaigns().contains(campaign)){
					existing.getCampaigns().add(campaign);
				}
			}
			return existing.getId();
		}else{ //email is new
//			lead.setEmail(email);
			dao.add(lead);
			return lead.getId();
		}
	}
	
	
	@Path(CANCEL)
	@PUT
	public void cancel(Lead l,@QueryParam("salt") Long id) {
		if (id == null || l.getEmail() == null || l.getEmail().getAddress() ==null
				|| l.getCampaigns() == null || l.getCampaigns().size()<1){
			throw new WebApplicationException("mandatory data missing", Response.Status.BAD_REQUEST);
		}
		Lead lead = dao.getObjectById(id, Lead.class);
		//id of the object and email have to match
		if (!lead.getEmail().getAddress().equalsIgnoreCase(l.getEmail().getAddress())){
			throw new WebApplicationException("Object to delete not the same", Response.Status.EXPECTATION_FAILED);
		}
		for (String campaign : l.getCampaigns()){
			lead.getCampaigns().remove(campaign);
		}
	}
	
	@Path(SECRET)
	@GET
	public Map<String,String> getSectret(){
		Map<String,String> rv = new HashMap<>();
		Random rand = new Random();
		int a = rand.nextInt(10);
		int b = rand.nextInt(10);
		long salt = System.currentTimeMillis();
		rv.put("a", String.valueOf(a));
		rv.put("b", String.valueOf(b));
		try {
			rv.put("hint", ENCRYPT("{\"a\":"+a+",\"b\":"+b+",\"salt\":"+salt+"}"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rv;
	}

}
