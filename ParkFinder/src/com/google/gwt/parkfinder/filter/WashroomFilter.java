package com.google.gwt.parkfinder.filter;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.parkfinder.server.Park;

public class WashroomFilter implements ParkFilter{

	@Override
	public List<Park> filter(List<Park> input) {
		List<Park> output = new LinkedList<Park>();
		for (Park park: input){
			if (park.getWashroom() != "No available washrooms.") { // or whatever
				output.add(park);
			}
		}
		return output;
	}
}