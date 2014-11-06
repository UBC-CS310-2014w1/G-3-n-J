package com.google.gwt.parkfinder.server;

import java.io.Serializable;

import javax.jdo.annotations.Persistent;

public class Facility implements Serializable{
	@Persistent
	String FacilityCount;
	@Persistent
	String FacilityType;
	@Persistent
	String SpecialFeature;
	
	public String getFacilityCount() {
		return FacilityCount;
	}
	
	public void setFacilityCount(String facilityCount) {
		FacilityCount = facilityCount;
	}
	
	public String getFacilityType() {
		return FacilityType;
	}
	
	public void setFacilityType(String facilityType) {
		FacilityType = facilityType;
	}
}