package com.google.gwt.parkfinder.client;

import java.io.Serializable;
import java.util.ArrayList;

public class Park implements Serializable {
	
	private String ParkID;
	private String Name;
	private String StreetNumber;
	private String StreetName;
	private String GoogleMapDest;
	private String NeighbourhoodName;
	private String NeighbourhoodURL;
	private ArrayList <Facility> Facility;
	private ArrayList <Washroom> Washroom;
	
	public String getParkID() {
		return ParkID;
	}
	
	public void setParkID(String parkID) {
		this.ParkID = parkID;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		this.Name = name;
	}
	
	public String getStreetNumber() {
		return StreetNumber;
	}
	
	public void setStreetNumber(String streetNumber) {
		this.StreetNumber = streetNumber;
	}
	
	public String getStreetName() {
		return StreetName;
	}
	
	public void setStreetName(String streetName) {
		this.StreetName = streetName;
	}
	
	public String getGoogleMapDest() {
		return GoogleMapDest;
	}
	
	public void setGoogleMapDest(String googleMapDest) {
		this.GoogleMapDest = googleMapDest;
	}
	
	public String getNeighbourhoodName() {
		return NeighbourhoodName;
	}
	
	public void setNeighbourhoodName(String neighbourhoodName) {
		this.NeighbourhoodName = neighbourhoodName;
	}
	
	public String getNeighbourhoodURL() {
		return NeighbourhoodURL;
	}
	
	public void setNeighbourhoodURL(String neighbourhoodURL) {
		this.NeighbourhoodURL = neighbourhoodURL;
	}
	
	public ArrayList<Facility> getFacility() {
		return Facility;
	}
	
	public void setFacility(ArrayList<Facility> facility) {
		this.Facility = facility;
	}
	
	public ArrayList<Washroom> getWashroom() {
		return Washroom;
	}
	
	public void setWashroom(ArrayList<Washroom> washroom) {
		this.Washroom = washroom;
	}
	
	

}
