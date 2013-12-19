package de.weinbock.persistence.dto;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.restnucleus.dao.Model;

@PersistenceCapable
@Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME)
public class Verification extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7133870710343434799L;
	
	@Persistent
	private String message;

	@Persistent
	private String email;

	@Persistent
	private String locale;
	
	@Persistent
	private String name;

	public String getMessage() {
		return message;
	}

	public Verification setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Verification setEmail(String email) {
		this.email = email;
		return this;
	}
	
	public String getLocale() {
		return locale;
	}

	public Verification setLocale(String locale) {
		this.locale = locale;
		return this;
	}

	public void update(Model newInstance) {
		Verification n = (Verification) newInstance;
		if (null != n.getMessage())
			this.setMessage(n.getMessage());
		if (null != n.getLocale())
			this.setLocale(n.getLocale());
		if (null != n.getEmail())
			this.setEmail(n.getEmail());
	}

	public String getName() {
		return name;
	}

	public Verification setName(String name) {
		this.name = name;
		return this;
	}

}
