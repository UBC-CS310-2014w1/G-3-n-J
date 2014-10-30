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
import com.google.gwt.parkfinder.client.ParkService;
import com.google.gwt.parkfinder.client.NotLoggedInException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FavoriteParkServiceImpl extends RemoteServiceServlet implements
		ParkService {

	private static final Logger LOG = Logger
			.getLogger(FavoriteParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	public void addPark(String Name) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new Park(getUser(), Name));
		} finally {
			pm.close();
		}
	}

	public void removeParks(String name) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			long deleteCount = 0;
			Query q = pm.newQuery(Park.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<Park> parks = (List<Park>) q.execute(getUser());
			for (Park park : parks) {
				if (name.equals(park.getName())) {
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
		List<String> symbols = new ArrayList<String>();
		try {
			Query q = pm.newQuery(Park.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			q.setOrdering("name");
			List<Park> parks = (List<Park>) q.execute(getUser());
			for (Park park : parks) {
				symbols.add(park.getName());
			}
		} finally {
			pm.close();
		}
		return (String[]) symbols.toArray(new String[0]);
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
