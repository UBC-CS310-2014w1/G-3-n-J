package com.google.gwt.parkfinder.filter;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.parkfinder.server.Park;

public class DistanceFilter implements ParkFilter{
	
	private float range;
	private float lat;
	private float lon;

	// this is a filter which doesn't make a lot of sense to actually use
	// but i made it as a proof of concept because it was was part of what
	// i imagined when i thought up the filter interface
	
	public DistanceFilter(float radius, float lat, float lon) {
		super();
		this.range = radius;
		this.lat = lat;
		this.lon = lon;
	}

	@Override
	public List<Park> filter(List<Park> input) {
		List<Park> output = new LinkedList<Park>();
		for (Park park: input){
			float x = park.getLon() - lon;
			float y = park.getLon() - lat;
			if (x*x + y*y < range*range) {
				output.add(park);
			}
		}
		return output;
	}
}