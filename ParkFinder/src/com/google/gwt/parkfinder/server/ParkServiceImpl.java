package com.google.gwt.parkfinder.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.parkfinder.server.Park;
import com.google.gwt.parkfinder.client.ParkService;
import com.google.gwt.parkfinder.client.NotLoggedInException;
import com.google.gwt.parkfinder.server.ParkServiceImpl;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import au.com.bytecode.opencsv.CSVReader;

public class ParkServiceImpl extends RemoteServiceServlet implements
		ParkService {

	private static final Logger LOG = Logger.getLogger(ParkServiceImpl.class
			.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	public List<Park> parkList = new ArrayList<Park>();

	@Override
	public List<Park> getParkList() throws IOException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();

		URL PARK_CSV = new URL("http://m.uploadedit.com/b041/1414532771299.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				PARK_CSV.openStream()));
		CSVReader reader = new CSVReader(in);
		String[] nextLine = null;
		// skip header row
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
			try {
				pm.makePersistent(park);
			} finally {
				pm.close();
			}
		}
		reader.close();
		System.out.println(parkList);
		return parkList;
	}

	@Override
	public Park getParkInfo(String id) throws NotLoggedInException {
		if (parkList.isEmpty())
			return null;
		for (int index = 0; index <= parkList.size(); index++) {
			Park park = parkList.get(index);
			if (park.getParkID() == id)
				return park;
		}
		return null;
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