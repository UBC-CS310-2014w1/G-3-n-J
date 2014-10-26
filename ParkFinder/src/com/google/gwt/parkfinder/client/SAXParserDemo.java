package com.google.gwt.parkfinder.client;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

public class SAXParserDemo {

	private static final String PARK_DATA = "http://data.vancouver.ca/datacatalogue/parkListing.htm";

	public static void parse() throws Exception {


		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		SAXHandler handler = new SAXHandler();
		parser.parse(new InputSource(new URL(PARK_DATA).openStream()), 
				handler);


		// Printing the list of employees obtained from XML
		for (Park park : handler.parkList){
			System.out.println(park);
		}
	}
}
