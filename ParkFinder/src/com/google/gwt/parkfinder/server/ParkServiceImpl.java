package com.google.gwt.parkfinder.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
			BufferedReader in = new BufferedReader(new InputStreamReader(PARK_CSV.openStream()));
			CSVReader reader = new CSVReader(in);
			reader.readNext();

			// Clear the old list of park
			pm.evictAll();

			String[] nextLine = null;
			// skip header row
			while ((nextLine = reader.readNext()) != null) {
				Park park = new Park();
				park.setParkID(nextLine[0]);
				park.setName(nextLine[1]);
				park.setStreetNumber(nextLine[3]);
				park.setStreetName(nextLine[4]);
				park.setGoogleMapDest(nextLine[7]);
				park.setNeighbourhoodName(nextLine[9]);
				park.setNeighbourhoodURL(nextLine[10]);

				pm.makePersistent(park);
			}
			reader.close();

		} finally {
			pm.close();
		}
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
		try {
			Query q = pm.newQuery(Park.class, "ParkID == id");
			q.declareParameters(id);
			Park park = (Park) q.execute(id);
			return park;
		} finally {
			pm.close();
		}
	}

	@Override
	public List<Park> searchName(String name) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<Park> nameMatched = new ArrayList<Park>();
		try {
		      Query q = pm.newQuery(Park.class, "Name == name");
		      q.declareParameters(name);
		      List<Park> parks = (List<Park>) q.execute();
		      for (Park park : parks) {
		        nameMatched.add(park);
			}
			return nameMatched;
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
