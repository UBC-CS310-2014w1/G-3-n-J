package com.google.gwt.parkfinder.filter;

import java.util.List;

import com.google.gwt.parkfinder.server.Park;

public interface ParkFilter {
	public List<Park> filter(List<Park> input);
}
