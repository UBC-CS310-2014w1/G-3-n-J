package com.google.gwt.parkfinder.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.google.gwt.parkfinder.client.NotLoggedInException;

import au.com.bytecode.opencsv.CSVReader;

public class ParkServiceImplTest {
	
	

	@Test
	public void testparkParser() throws IOException {
		
		// Park CSV
		URL PARK_CSV = new URL("http://m.uploadedit.com/b041/1414532771299.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(PARK_CSV.openStream()));
		CSVReader reader = new CSVReader(in);
		
		// Create park list to iterate over
		List<String[]> parks = reader.readAll();
		Iterator<String[]> parksIterator = parks.iterator();
		
		// Skip header row
		parksIterator.next();
		
		// Park 1: Arbutus Village Park
		String[] record = parksIterator.next();
		assertEquals(record[0], "1");
		assertEquals(record[1], "Arbutus Village Park");
		
		// Park 2: Carnarvon Park
		record = parksIterator.next();
		assertEquals(record[0], "2");
		assertEquals(record[1], "Carnarvon Park");
		
		// Park 3: Prince of Wales Park
		record = parksIterator.next();
		assertEquals(record[0], "3");
		assertEquals(record[1], "Prince of Wales Park");
	}

	@Test
	public void testGetFacility() throws IOException {
		
		ParkServiceImpl parser = new ParkServiceImpl();
		
		// Facility CSV
		URL FACILITIES_CSV = new URL("http://m.uploadedit.com/b042/1415280168943.txt");
		BufferedReader inFacility = new BufferedReader(new InputStreamReader(FACILITIES_CSV.openStream()));
		CSVReader readerFacility = new CSVReader(inFacility);
		
		// Create facilities list to iterate over
		List<String[]> facilities = readerFacility.readAll();
		
		// Park 1
		assertEquals(parser.getFacility("1", facilities), "1 Playgrounds, ");
		
		// Park 2, multiple facilities
		assertEquals(parser.getFacility("2", facilities), "1 Field Houses, 1 Playgrounds, 2 Softball, 3 Baseball Diamonds, 1 Football Fields, 1 Soccer Fields, ");
		
		// Park 9, no facilities
		assertEquals(parser.getFacility("9", facilities), "No available facilities.");
		
		// Park 110
		assertEquals(parser.getFacility("110", facilities), "1 Dogs Off-Leash Areas, ");
		
	}

	@Test
	public void testGetWashroom() throws IOException {
		
		ParkServiceImpl parser = new ParkServiceImpl();
		
		// Washroom CSV
		URL WASHROOMS_CSV = new URL("http://m.uploadedit.com/b042/1415280351920.txt");
		BufferedReader inWashroom = new BufferedReader(new InputStreamReader(WASHROOMS_CSV.openStream()));
		CSVReader readerWashroom = new CSVReader(inWashroom);
		
		// Create washrooms list to iterate over
		List<String[]> washrooms = readerWashroom.readAll();
		
		// Park 1 no washrooms
		assertEquals(parser.getWashroom("1", washrooms), "No available washrooms.");
		
		// Park 2
		assertEquals(parser.getWashroom("2", washrooms), "LOCATION: West side, field house, ");
		
		// Park 221
		assertEquals(parser.getWashroom("221", washrooms), "LOCATION: Centre, ");

	}

	@Test
	public void testGetParkImgUrl() throws IOException {
		
		ParkServiceImpl parser = new ParkServiceImpl();
		
		// ParkImgUrl CSV
		URL PARKIMGURL_CSV = new URL("http://m.uploadedit.com/b042/141586933684.txt");
		BufferedReader inImgUrl = new BufferedReader(new InputStreamReader(PARKIMGURL_CSV.openStream()));
		CSVReader readerImgUrl = new CSVReader(inImgUrl);
		
		// Create ParkImgUrl list to iterate over
		List<String[]> parkImages = readerImgUrl.readAll();
		
		// Park 1
		assertEquals(parser.getParkImgUrl("1", parkImages), "http://www.vancouver.ca/parks/parks/images/arbutusvillage03.jpg");
		
		// Park 158
		assertEquals(parser.getParkImgUrl("158", parkImages), "http://www.vancouver.ca/parks/parks/images/price01.jpg");
	}
}
