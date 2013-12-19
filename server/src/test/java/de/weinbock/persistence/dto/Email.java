package de.weinbock.persistence.dto;

import java.util.Date;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.validation.constraints.NotNull;

import org.restnucleus.dao.Model;

@PersistenceCapable
@Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME)
public class Email extends Model {
	private static final long serialVersionUID = 1L;

	@Index
	@Persistent
	@NotNull
	private String address;

	// the email secret which needs to be returned before email becomes visible
	@Index
	@Persistent
	private String secret;
	
	// active is initially false, will be set to true when verified, and false again on bounce
	@Persistent
	private Boolean isActive;
	
	// 
	@Persistent
	private Date confirmationTime;
	
	//
	@Persistent
	private String confirmationIp;

	// the language of the user
	@Persistent
	private String hostName;
	
	public String getAddress() {
		return address;
	}

	public Email setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getSecret() {
		return secret;
	}

	public Email setSecret(String secret) {
		this.secret = secret;
		return this;
	}
	
	public Date getConfirmationTime() {
		return confirmationTime;
	}

	public Email setConfirmationTime(Date confirmationTime) {
		this.confirmationTime = confirmationTime;
		return this;
	}
	
	public String getConfirmationIp() {
		return confirmationIp;
	}

	public Email setConfirmationIp(String confirmationIp) {
		this.confirmationIp = confirmationIp;
		return this;
	}

	public String getHostName() {
		return hostName;
	}

	public Email setHostName(String hostName) {
		this.hostName = hostName;
		return this;
	}
	
	public Boolean getIsActive() {
		return isActive;
	}

	public Email setIsActive(Boolean isActive) {
		this.isActive = isActive;
		return this;
	}

	public void update(Model newInstance) {
		Email n = (Email) newInstance;
		if (null != n.getConfirmationTime())this.setConfirmationTime(n.getConfirmationTime());
		if (null != n.getConfirmationIp())this.setConfirmationIp(n.getConfirmationIp());
		if (null != n.getAddress())this.setAddress(n.getAddress());
		if (null != n.getSecret())this.setSecret(n.getSecret());
		if (null != n.getIsActive())this.setIsActive(n.getIsActive());
		if (null != n.getHostName())this.setHostName(n.getHostName());
	}
}
