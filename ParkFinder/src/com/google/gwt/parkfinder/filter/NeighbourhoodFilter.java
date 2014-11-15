package com.google.gwt.parkfinder.filter;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.parkfinder.server.Park;

public class NeighbourhoodFilter implements ParkFilter {
	List<String> neighbourhoods;

	public NeighbourhoodFilter(List<String> neighbourhoods) {
		this.neighbourhoods = neighbourhoods;
	}

	@Override
	public List<Park> filter(List<Park> input) {
		List<Park> output = new LinkedList<Park>();
		for (Park park: input){
			for (String neighbourhood: neighbourhoods){
				if (park.getNeighbourhoodName().equals(neighbourhood)) {
					output.add(park);
				}
			}
		}
		return output;
	}
}