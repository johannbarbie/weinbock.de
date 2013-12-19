package de.weinbock.dao;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.restnucleus.dao.Model;

@PersistenceCapable
@Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME)
public class Account extends Model{
	private static final long serialVersionUID = -860017262192514098L;
	
	public static String getHostName(String email){
		String hostName = email.substring(email.indexOf("@") + 1, email.length());
		return hostName;
	}
	
	@Persistent
	@Index
	private String email;
	
	@Persistent
	@Index
	private String hostName;
	
	@Persistent
	private String password;
	
	@NotPersistent
	private String[] roles;

	public String getEmail() {
		return email;
	}
	public Account setEmail(String email) {
		this.email = email;
		this.setHostName(Account.getHostName(email));
		return this;
	}
	public String getPassword() {
		return password;
	}
	public Account setPassword(String password) {
		this.password = password;
		return this;
	}
	public String getHostName() {
		return hostName;
	}
	public Account setHostName(String hostName) {
		this.hostName = hostName;
		return this;
	}
	public String[] getRoles() {
		return roles;
	}
	public Account setRoles(String[] roles) {
		this.roles = roles;
		return this;
	}
	public void update(Model newInstance) {
		Account n = (Account) newInstance;
		if (null != n.getEmail())this.setEmail(n.getEmail());
		if (null != n.getPassword())this.setPassword(n.getPassword());
	}
	
}
