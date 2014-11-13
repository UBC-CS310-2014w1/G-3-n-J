package com.google.gwt.parkfinder.server;

import java.io.Serializable;

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
	private Long entryID;
	@Persistent
	private String ParkID;
	@Persistent
	private User User;
	
	public FavoritePark(User user, String parkID) {
		this.User = user;
		this.ParkID = parkID;
	}

	public Long getEntryID() {
		return entryID;
	}

	public String getParkID() {
		return ParkID;
	}

	public User getUser() {
		return User;
	}

	public void setEntryID(Long entryID) {
		this.entryID = entryID;
	}

	public void setParkID(String parkID) {
		ParkID = parkID;
	}

	public void setUser(User user) {
		User = user;
	}
}