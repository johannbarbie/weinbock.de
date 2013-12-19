package de.weinbock.dao;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.restnucleus.dao.Model;

@PersistenceCapable
@Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME)
public class TasteProfile extends Model{
	private static final long serialVersionUID = -707859541424223150L;
	
	@Persistent
	private Account owner;
	@Persistent
	private Integer fruity;
	
	public Integer getFruity() {
		return fruity;
	}
	public TasteProfile setFruity(Integer fruity) {
		this.fruity = fruity;
		return this;
	}
	
	public Account getOwner() {
		return owner;
	}
	public TasteProfile setOwner(Account owner) {
		this.owner = owner;
		return this;
	}
	
	public void update(Model newInstance) {
		TasteProfile n = (TasteProfile)newInstance;
		if (null != n.getFruity())this.setFruity(n.getFruity());
	}
	

}
