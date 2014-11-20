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

	public ArrayList<String> getNeighbourhoodNames() {
		ArrayList<String> neighbourhoods = new ArrayList<String>();
		neighbourhoods.add("Downtown");
		neighbourhoods.add("Arbutus Ridge");
		neighbourhoods.add("Dunbar-Southlands");
		neighbourhoods.add("Fairview");
		neighbourhoods.add("Grandview-Woodland");
		neighbourhoods.add("Hastings-Sunrise");
		neighbourhoods.add("Kensington-Cedar Cottage");
		neighbourhoods.add("Kerrisdale");
		neighbourhoods.add("Killarney");
		neighbourhoods.add("Kitsilano");
		neighbourhoods.add("Marpole");
		neighbourhoods.add("Mount Pleasant");
		neighbourhoods.add("Oakridge");
		neighbourhoods.add("Renfrew-Collingwood");
		neighbourhoods.add("Riley-Little Mountain");
		neighbourhoods.add("Shaughnessy");
		neighbourhoods.add("South Cambie");
		neighbourhoods.add("Strathcona");
		neighbourhoods.add("Sunset");
		neighbourhoods.add("Victoria-Fraserview");
		neighbourhoods.add("West End");
		neighbourhoods.add("West Point Grey");

		return neighbourhoods;
	}
}