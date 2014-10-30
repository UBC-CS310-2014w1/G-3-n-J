package com.google.gwt.parkfinder.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.parkfinder.client.Park;
import com.google.gwt.parkfinder.client.ParkService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import au.com.bytecode.opencsv.CSVReader;

public class ParkServiceImpl extends RemoteServiceServlet implements ParkService {
	
	private List<Park> parkList = new ArrayList<Park>();

	@Override
	public List<Park> getParkList() throws IOException {
		URL PARK_CSV = new URL("http://m.uploadedit.com/b041/1414532771299.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(PARK_CSV.openStream()));
		CSVReader reader = new CSVReader(in);
		String [] nextLine = null;
		//skip header row
		reader.readNext();
	    while ((nextLine = reader.readNext()) != null) {
	        Park park = new Park();
	        park.setParkID(nextLine[0]);
	        park.setName(nextLine[1]);
	        park.setStreetNumber(nextLine[3]);
	        park.setStreetName(nextLine[4]);
	        park.setGoogleMapDest(nextLine[7]);
	        park.setNeighbourhoodName(nextLine[9]);
	        park.setNeighbourhoodURL(nextLine[10]);
	        parkList.add(park);
	    }
	    
	    reader.close();
	    
	    System.out.println(parkList);
	    
		return parkList;
	}

	@Override
	public Park getPark(String ParkID) {
		// TODO Auto-generated method stub
		return null;
	}
}
