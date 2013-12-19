package de.weinbock.persistence.dto;

import java.util.List;
import java.util.Random;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Serialized;
import javax.validation.constraints.NotNull;

import org.restnucleus.dao.Model;

@PersistenceCapable
@Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME)
public class Lead extends Model {
	private static final long serialVersionUID = 792538765194459327L;


	public static String createEmailSecret() {
		Random rand = new Random();
		String es =  String.valueOf(rand.nextInt());
		return es;
	}

	//for Turing test
	@NotPersistent
	private String hint;
	@NotPersistent
	private Integer sum;

	@Persistent
	private String givenName;

	@Persistent
	private String familyName;

	// the email
	@Index
	@Persistent(defaultFetchGroup="true")
	@NotNull
	private Email email;

	// campaigns this email is involved in
	@Persistent(defaultFetchGroup="true")
	@Serialized
	@NotNull
	private List<String> campaigns;	

	// the language of the user
	@Persistent
	private String language;

	public Lead() {
		super();
	}
	
	public String getHint() {
		return hint;
	}

	public Lead setHint(String hint) {
		this.hint = hint;
		return this;
	}

	public Integer getSum() {
		return sum;
	}

	public Lead setSum(Integer sum) {
		this.sum = sum;
		return this;
	}
	
	public String getGivenName() {
		return givenName;
	}

	public Lead setGivenName(String givenName) {
		this.givenName = givenName;
		return this;
	}
	
	public String getFamilyName() {
		return familyName;
	}

	public Lead setFamilyName(String familyName) {
		this.familyName = familyName;
		return this;
	}

	public List<String> getCampaigns() {
		return campaigns;
	}

	public Lead setCampaigns(List<String> campaigns) {
		this.campaigns = campaigns;
		return this;
	}

	public String getLanguage() {
		return language;
	}

	public Lead setLanguage(String language) {
		this.language = language;
		return this;
	}


	public Email getEmail() {
		return email;
	}

	public Lead setEmail(Email email) {
		this.email = email;
		return this;
	}

	public void update(Model newInstance) {
		Lead n = (Lead) newInstance;
		if (null != n.getGivenName())this.setGivenName(n.getGivenName());
		if (null != n.getFamilyName())this.setFamilyName(n.getFamilyName());
		if (null != n.getCampaigns())this.setCampaigns(n.getCampaigns());
		if (null != n.getEmail())this.setEmail(n.getEmail());
		if (null != n.getLanguage())this.setLanguage(n.getLanguage());
	}
}
