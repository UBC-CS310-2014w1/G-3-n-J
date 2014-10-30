package com.google.gwt.parkfinder.server;

import java.util.LinkedList;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Park {
	
	 @PrimaryKey 
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
	 private LinkedList <Facility> Facility;
	 @Persistent
	 private LinkedList <Washroom> Washroom;
	 
	 // Used when adding to favorite
	 public Park(User user, String name) {
		 this.User = user;
		 this.Name = name;
		}
	 
	 // Used when storing to database
	 public Park(String name, String streetNumber, String streetName,
			 String googleMapDest, String neighbourhoodName, String neighbourhoodURL, 
			 LinkedList <Facility> facility, LinkedList <Washroom> washroom){
		 this.Name = name;
		 this.StreetNumber = streetNumber;
		 this.StreetName = streetName;
		 this.GoogleMapDest = googleMapDest;
		 this.NeighbourhoodName = neighbourhoodName;
		 this.NeighbourhoodURL = neighbourhoodURL;
		 this.Facility = facility;
		 this.Washroom = washroom;
	 }

	// Auto-generated getters and setters
	 
	public User getUser() {
		return User;
	}

	public void setUser(User user) {
		User = user;
	}

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

