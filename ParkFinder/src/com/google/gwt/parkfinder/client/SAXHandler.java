package com.google.gwt.parkfinder.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Handler for SAX Events.
 */
class SAXHandler extends DefaultHandler {

  List<Park> parkList = new ArrayList<>();
  Park park = null;
  String content = null;
  @Override
  //Triggered when the start of tag is found.
  public void startElement(String uri, String localName, 
                           String qName, Attributes attributes) 
                           throws SAXException {

    switch(qName){
      //Create a new Park object when the start tag is found
      case "Name":
        park = new Park();
        park.Name = attributes.getValue("Name");
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, 
		  String qName) throws SAXException {
	  switch(qName){
	  //Add the park to list once end tag is found
	  case "Park":
		  parkList.add(park);       
		  break;
		  //For all other end tags the park has to be updated with respective attributes.
	  case "StreetNumber":
		  park.StreetNumber = content;
		  break;
	  case "StreetName":
		  park.StreetName = content;
		  break;
	  case "GoogleMapDest":
		  park.GoogleMapDest = content;
		  break;
	  case "NeighbourhoodName":
		  park.NeighbourhoodName = content;
		  break;
	  case "NeighbourhoodURL":
		  park.NeighbourhoodURL = content;
		  break;
	  }
  }

  @Override
  public void characters(char[] ch, int start, int length) 
          throws SAXException {
    content = String.copyValueOf(ch, start, length).trim();
  }

}