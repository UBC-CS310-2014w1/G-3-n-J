package com.google.gwt.parkfinder.server;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Park implements Serializable {

	@PrimaryKey
	@Persistent
	private String ParkID;
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
	private String Facility;
	@Persistent
	private String Washroom;

	public Park() {
		// TODO Auto-generated constructor stub
	}

	public String getParkID() {
		return ParkID;
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

	public String getFacility() {
		return Facility;
	}

	public String getWashroom() {
		return Washroom;
	}

	public void setParkID(String parkID) {
		ParkID = parkID;
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

	public void setFacility(String facility) {
		Facility = facility;
	}

	public void setWashroom(String washroom) {
		Washroom = washroom;
	}
}