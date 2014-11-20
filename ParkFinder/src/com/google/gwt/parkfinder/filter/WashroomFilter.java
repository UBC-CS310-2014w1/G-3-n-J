package com.google.gwt.parkfinder.filter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.parkfinder.server.Park;

public class WashroomFilter implements ParkFilter{

	@Override
	public List<Park> filter(List<Park> input) {
		List<Park> output = new ArrayList<Park>();
		for (Park park: input){
			if (!park.getWashroom().equals("No available washrooms.")) {
				output.add(park);
			}
		}
		return output;
	}
}