package com.google.gwt.parkfinder.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.parkfinder.server.Park;

public class NeighbourhoodFilter implements ParkFilter {
	HashMap<String, String> neighbourhoods;

	public NeighbourhoodFilter(HashMap<String, String> neighbourhoods) {
		this.neighbourhoods = neighbourhoods;
	}

	@Override
	public List<Park> filter(List<Park> input) {
		List<Park> output = new LinkedList<Park>();

		for (Park park: input){
			if (neighbourhoods.containsKey(park.getNeighbourhoodName())){
				output.add(park);
			}
		}
		return output;
	}
}