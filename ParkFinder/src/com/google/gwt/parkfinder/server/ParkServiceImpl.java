package com.google.gwt.parkfinder.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.parkfinder.server.Park;
import com.google.gwt.parkfinder.client.ParkService;
import com.google.gwt.parkfinder.client.NotLoggedInException;
import com.google.gwt.parkfinder.server.ParkServiceImpl;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import au.com.bytecode.opencsv.CSVReader;

public class ParkServiceImpl extends RemoteServiceServlet implements ParkService {

	private static final Logger LOG = Logger.getLogger(ParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public void storeParkList() throws IOException, NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {

			URL PARK_CSV = new URL("http://m.uploadedit.com/b041/1414532771299.txt");
			URL FACILITIES_CSV = new URL("http://m.uploadedit.com/b042/1415280168943.txt");
			URL WASHROOMS_CSV = new URL("http://m.uploadedit.com/b042/1415280351920.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(PARK_CSV.openStream()));
			CSVReader reader = new CSVReader(in);
			// Skip header row
			reader.readNext();

			// Clear the old list of park
			pm.evictAll();

			String[] nextLine = null;
			while ((nextLine = reader.readNext()) != null) {
				Park park = new Park();
				park.setParkID(nextLine[0]);
				park.setName(nextLine[1]);
				park.setStreetNumber(nextLine[3]);
				park.setStreetName(nextLine[4]);
				park.setGoogleMapDest(nextLine[7]);
				park.setNeighbourhoodName(nextLine[9]);
				park.setNeighbourhoodURL(nextLine[10]);
				park.setFacility(getFacility(park.getParkID()));
				System.out.println(park.getParkID());
				System.out.println(park.getFacility());
				//System.out.println(park.getFacility()[0].getFacilityType());

				pm.makePersistent(park);
			}
			reader.close();

		} finally {
			pm.close();
		}
	}

	public ArrayList<Facility> getFacility(String parkID) throws IOException {
		List<Facility> facilities = new ArrayList<Facility>();
		URL FACILITIES_CSV = new URL("http://m.uploadedit.com/b042/1415280168943.txt");
		BufferedReader inFacility = new BufferedReader(new InputStreamReader(FACILITIES_CSV.openStream()));
		CSVReader readerFacility = new CSVReader(inFacility);
		// Skip header row
		readerFacility.readNext();
		String[] nextLineFacility = null;
		while ((nextLineFacility = readerFacility.readNext()) != null) {
			if (nextLineFacility[0].toString().equals(parkID.toString())) {
				System.out.println(nextLineFacility[0].toString().equals(parkID.toString()));
				System.out.println("Facility ID:" + nextLineFacility[0]);
				System.out.println("ParkID:" + parkID);
				Facility facility = new Facility();
				facility.setFacilityCount(nextLineFacility[1]);
				facility.setFacilityType(nextLineFacility[2]);
				facilities.add(facility);
				System.out.println(facility.getFacilityCount() + ":" + facility.getFacilityType());
			}
		}
		readerFacility.close();
		System.out.println(facilities);
		return (ArrayList<Facility>) facilities;
	}

	@Override
	public List<Park> getParkList() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<Park> listOfPark = new ArrayList<Park>();
		try {
			Query q = pm.newQuery(Park.class);
			List<Park> parks = (List<Park>) q.execute();
			for (Park park : parks) {
				listOfPark.add(park);
			}
			return listOfPark;
		} finally {
			pm.close();
		}
	}

	@Override
	public Park getParkInfo(String id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<Park> listOfPark = new ArrayList<Park>();
		try {
			Query q = pm.newQuery(Park.class, "ParkID == idParam");
			q.declareParameters("int idParam");
			List<Park> parks = (List<Park>) q.execute(id);
			for (Park park : parks) {
				listOfPark.add(park);
			}
			return listOfPark.get(0);
		} finally {
			pm.close();
		}
	}

	@Override
	public List<Park> searchName(String name) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<Park> listOfPark = new ArrayList<Park>();
		try {
			Query q = pm.newQuery(Park.class, "Name == nameParam");
			q.declareParameters("String nameParam");
			List<Park> parks = (List<Park>) q.execute(name);
			for (Park park : parks) {
				listOfPark.add(park);
			}
			return listOfPark;
		} finally {
			pm.close();
		}
	}

	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
			throw new NotLoggedInException("Not logged in.");
		}
	}

	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
		return userService.getCurrentUser();
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}
