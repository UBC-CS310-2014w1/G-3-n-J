package com.google.gwt.parkfinder.filter;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.parkfinder.server.Park;

public class PlaygroundFilter implements ParkFilter {

	@Override
	public List<Park> filter(List<Park> input) {
		List<Park> output = new LinkedList<Park>();
		for (Park park: input){
			if (park.getFacility().indexOf("Playgrounds") != -1) {
				output.add(park);
			}
		}
		return output;
	}

}
