package com.google.gwt.parkfinder.server;

import javax.jdo.annotations.Persistent;

public class Washroom {
	@Persistent
	String Location;
	@Persistent
	String Notes;
	@Persistent
	String SummerHours;
	@Persistent
	String WinterHours;
}
