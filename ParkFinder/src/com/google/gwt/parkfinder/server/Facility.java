package com.google.gwt.parkfinder.server;

import javax.jdo.annotations.Persistent;

public class Facility {
	@Persistent
	String FacilityCount;
	@Persistent
	String FacilityType;
	@Persistent
	String SpecialFeature;
}