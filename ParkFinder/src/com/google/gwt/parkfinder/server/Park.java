package com.google.gwt.parkfinder.server;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Park implements Serializable {

	@PrimaryKey
	@Persistent
	private String ParkID;
	@Persistent
	private User User;
	@Persistent
	private String Name;
	@Persistent
	private String StreetNumber;
	@Persistent
	private String StreetName;
	@Persistent
	private String GoogleMapDest;
	@Persistent
	private String NeighbourhoodName;
	@Persistent
	private String NeighbourhoodURL;
	@Persistent
	private ArrayList<Facility> Facility;
	@Persistent
	private ArrayList<Washroom> Washroom;
	
	// Use when storing to database
	public Park() {
		// TODO Auto-generated constructor stub
	}

	// Use when adding to favorite
	public Park(User user, String parkID) {
		this();
		this.User = user;
		this.ParkID = parkID;
	}

	// Auto-generated getters and setters

	public String getParkID() {
		return ParkID;
	}

	public User getUser() {
		return User;
	}

	public String getName() {
		return Name;
	}

	public String getStreetNumber() {
		return StreetNumber;
	}

	public String getStreetName() {
		return StreetName;
	}

	public String getGoogleMapDest() {
		return GoogleMapDest;
	}

	public String getNeighbourhoodName() {
		return NeighbourhoodName;
	}

	public String getNeighbourhoodURL() {
		return NeighbourhoodURL;
	}

	public ArrayList<Facility> getFacility() {
		return Facility;
	}

	public ArrayList<Washroom> getWashroom() {
		return Washroom;
	}

	public void setParkID(String parkID) {
		ParkID = parkID;
	}

	public void setUser(User user) {
		User = user;
	}

	public void setName(String name) {
		Name = name;
	}

	public void setStreetNumber(String streetNumber) {
		StreetNumber = streetNumber;
	}

	public void setStreetName(String streetName) {
		StreetName = streetName;
	}

	public void setGoogleMapDest(String googleMapDest) {
		GoogleMapDest = googleMapDest;
	}

	public void setNeighbourhoodName(String neighbourhoodName) {
		NeighbourhoodName = neighbourhoodName;
	}

	public void setNeighbourhoodURL(String neighbourhoodURL) {
		NeighbourhoodURL = neighbourhoodURL;
	}

	public void setFacility(ArrayList<Facility> facility) {
		Facility = facility;
	}

	public void setWashroom(ArrayList<Washroom> washroom) {
		Washroom = washroom;
	}
}
