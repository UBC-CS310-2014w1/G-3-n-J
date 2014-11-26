package com.google.gwt.parkfinder.server;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FavoritePark implements Serializable {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private User User;
	@Persistent
	private List<String> Parks;

	public FavoritePark(User user, List<String> parks) {
		this.User = user;
		this.Parks = parks;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return User;
	}

	public List<String> getParks() {
		return Parks;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(User user) {
		User = user;
	}

	public void setParks(List<String> parks) {
		Parks = parks;
	}
}