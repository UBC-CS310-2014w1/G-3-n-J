package com.google.gwt.parkfinder.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.parkfinder.server.Park;

public class NeighbourhoodFilter implements ParkFilter {
	String neighbourhood;

	public NeighbourhoodFilter(String neighbourhood) {
		this.neighbourhood = neighbourhood;
	}

	@Override
	public List<Park> filter(List<Park> input) {
		List<Park> output = new LinkedList<Park>();
		for (Park park: input){
			if (park.getNeighbourhoodName().equals(neighbourhood)) {
				output.add(park);
			}
		}	
		return output;
	}
}