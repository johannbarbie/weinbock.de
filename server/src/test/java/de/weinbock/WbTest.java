package de.weinbock;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.restnucleus.dao.Model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Cookies;

import de.weinbock.persistence.dto.Email;
import de.weinbock.persistence.dto.Lead;
import de.weinbock.test.resources.EmailAdminResource;
import de.weinbock.test.resources.EmailResource;
import de.weinbock.test.resources.HealthCheckResource;
import de.weinbock.test.resources.LeadAdminResource;
import de.weinbock.test.resources.LeadResource;

public class WbTest  {	
	public Cookies cookies = null;
	public String restUrl= "";
	public ObjectMapper om = new ObjectMapper();
	
	@Test
	public void testTime() throws JsonParseException, JsonMappingException,
			IOException {
		String rv = given()
			.expect().statusCode(200).when()
			.get(restUrl + HealthCheckResource.PATH).asString();
		Assert.assertNotNull(rv);
	}
	
	@Test
	public void testEmail() throws JsonParseException, JsonMappingException, IOException {
		Email m = new Email().setAddress("fail.me@nowhere.spam");
		//check fake mail host 
		given().body(m).contentType(ContentType.JSON)
			.expect().statusCode(417)
			.when().post(restUrl + EmailResource.PATH);
		//check fake email on dedicated resource
		given().queryParam("email", m.getAddress())
			.expect().statusCode(417)
			.when().get(restUrl + EmailResource.PATH_CHECK);
		//create email
		m.setAddress("johann@weinbock.de");
		given().body(m).contentType(ContentType.JSON)
			.expect().statusCode(200)
			.when().post(restUrl + EmailResource.PATH);
		//avoid double creation
		Email m2 = new Email().setAddress("johann@weinbock.de");
		given().body(m2).contentType(ContentType.JSON)
		.expect().statusCode(409)
		.when().post(restUrl + EmailResource.PATH);
		//get secret
		List<Email> created = om.readValue(
			given().queryParam("filter", "address=johann@weinbock.de")
				.expect().statusCode(200).body("size()", is(1))
				.when().get(restUrl + EmailAdminResource.PATH).asString(), 
			new TypeReference<List<Email>>() {});
		String secret = created.get(0).getSecret();
		//confirm email
		given().queryParam("secret", secret)
			.expect().statusCode(200)
			.when().put(restUrl + EmailResource.PATH_CONFIRM);
	}
	
	@Test
	public void testLead() throws JsonParseException, JsonMappingException, IOException {
		//form is being filled out
		Lead lead = new Lead().setGivenName("hans")
			.setEmail(new Email().setAddress("fail.me@nowhere.spam"))
			.setLanguage("de_DE").setCampaigns(Arrays.asList("beta", "monthly"));				
		// get Turing task
		Map<String,String> task = om.readValue(
		given()
			.expect().statusCode(200)
			.when().get(restUrl + LeadResource.PATH_SECRET).asString(),
		new TypeReference<Map<String,String>>() {});
		//check Turing test
		String oldSec = "66e6d975e1affd9828bdeaf727e80a053bd495c9ff74a66e9e7564826be6eb27";
		given().body(lead.setHint(oldSec).setSum(12)).contentType(ContentType.JSON)
			.expect().statusCode(417)
			.when().post(restUrl + LeadResource.PATH);
		// create lead
		lead.setHint(task.get("hint")).setSum(
				Integer.parseInt(task.get("a"))+Integer.parseInt(task.get("b")))
			.getEmail().setAddress("jacob@weinbock.de");
		given().body(lead).contentType(ContentType.JSON)
			.expect().statusCode(200)
			.when().post(restUrl + LeadResource.PATH);
		// avoid double creation
		Long id = Long.parseLong(
			given().contentType(ContentType.JSON)
				.body(lead.setCampaigns(Arrays.asList("third","beta", "monthly")))
			.expect().statusCode(200)
			.when().post(restUrl + LeadResource.PATH).asString());
		// cancel subscription
		Model cancel = new Lead().setCampaigns(Arrays.asList("monthly"))
			.setEmail(new Email().setAddress("jacob@weinbock.de"));
		given().body(cancel).contentType(ContentType.JSON).queryParam("salt", id)
			.expect().statusCode(204)
			.when().put(restUrl + LeadResource.PATH_CANCEL);
		// validate canceled
		String str = 
			given()
				.expect().statusCode(200)
				.when().get(restUrl + LeadAdminResource.PATH+"/{id}",id).asString();
		Lead existing = om.readValue(str, Lead.class);
		Assert.assertFalse(
				existing.getCampaigns().contains("monthly"));
		Assert.assertTrue(
				existing.getCampaigns().contains("third"));
	}	

}
