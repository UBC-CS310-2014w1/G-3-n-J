package com.google.gwt.parkfinder.server;

import java.util.LinkedList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.parkfinder.client.Facility;
import com.google.gwt.parkfinder.client.Washroom;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Park {
	
	 @PrimaryKey
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
	 private LinkedList <Facility> Facility;
	 @Persistent
	 private LinkedList <Washroom> Washroom;
	 
	 public Park() {
			// TODO Auto-generated constructor stub
		}
	 
	 public Park(String Name, String StreetNumber, String StreetName,
			 String GoogleMapDest, String NeighbourhoodName, String NeighbourhoodURL, 
			 LinkedList <Facility> Facility, LinkedList <Washroom> Washroom){
		 this();
		 this.Name = Name;
		 this.StreetNumber = StreetNumber;
		 this.StreetName = StreetName;
		 this.GoogleMapDest = GoogleMapDest;
		 this.NeighbourhoodName = NeighbourhoodName;
		 this.NeighbourhoodURL = NeighbourhoodURL;
		 this.Facility = Facility;
		 this.Washroom = Washroom;
	 }

	 // Auto-generated getters and setters
	 
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getStreetNumber() {
		return StreetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		StreetNumber = streetNumber;
	}

	public String getStreetName() {
		return StreetName;
	}

	public void setStreetName(String streetName) {
		StreetName = streetName;
	}

	public String getGoogleMapDest() {
		return GoogleMapDest;
	}

	public void setGoogleMapDest(String googleMapDest) {
		GoogleMapDest = googleMapDest;
	}

	public String getNeighbourhoodName() {
		return NeighbourhoodName;
	}

	public void setNeighbourhoodName(String neighbourhoodName) {
		NeighbourhoodName = neighbourhoodName;
	}

	public String getNeighbourhoodURL() {
		return NeighbourhoodURL;
	}

	public void setNeighbourhoodURL(String neighbourhoodURL) {
		NeighbourhoodURL = neighbourhoodURL;
	}

	public LinkedList<Facility> getFacility() {
		return Facility;
	}

	public void setFacility(LinkedList<Facility> facility) {
		Facility = facility;
	}

	public LinkedList<Washroom> getWashroom() {
		return Washroom;
	}

	public void setWashroom(LinkedList<Washroom> washroom) {
		Washroom = washroom;
	}
}

