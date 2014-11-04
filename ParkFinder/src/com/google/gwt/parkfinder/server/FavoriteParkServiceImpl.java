package com.google.gwt.parkfinder.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.parkfinder.client.FavoriteParkService;
import com.google.gwt.parkfinder.client.ParkService;
import com.google.gwt.parkfinder.client.NotLoggedInException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FavoriteParkServiceImpl extends RemoteServiceServlet implements
		FavoriteParkService {

	private static final Logger LOG = Logger
			.getLogger(FavoriteParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	public void addPark(String id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new Park(id));
		} finally {
			pm.close();
		}
	}

	public void removePark(String id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			long deleteCount = 0;
			Query q = pm.newQuery(Park.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<Park> parks = (List<Park>) q.execute(getUser());
			for (Park park : parks) {
				if (id.equals(park.getName())) {
					deleteCount++;
					pm.deletePersistent(park);
				}
			}
			if (deleteCount != 1) {
				LOG.log(Level.WARNING, "removePark deleted " + deleteCount
						+ " Parks");
			}
		} finally {
			pm.close();
		}
	}

	public String[] getParks() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<String> ids = new ArrayList<String>();
		try {
			Query q = pm.newQuery(Park.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			q.setOrdering("id");
			List<Park> parks = (List<Park>) q.execute(getUser());
			for (Park park : parks) {
				ids.add(park.getParkID());
			}
		} finally {
			pm.close();
		}
		return (String[]) ids.toArray(new String[0]);
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
