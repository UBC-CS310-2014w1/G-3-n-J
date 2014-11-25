package com.google.gwt.parkfinder.filter;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.parkfinder.server.Park;

public class CommunityFilter implements ParkFilter{

	@Override
	public List<Park> filter(List<Park> input) {
		List<Park> output = new LinkedList<Park>();
		for (Park park: input){
			if (park.getFacility().indexOf("Community") != -1) {
				output.add(park);
			}
		}
		return output;
	}
}
