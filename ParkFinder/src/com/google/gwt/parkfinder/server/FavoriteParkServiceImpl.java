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

import com.google.gwt.parkfinder.server.FavoritePark;
import com.google.gwt.parkfinder.client.FavoriteParkService;
import com.google.gwt.parkfinder.client.NotLoggedInException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FavoriteParkServiceImpl extends RemoteServiceServlet implements FavoriteParkService {

	private static final Logger LOG = Logger.getLogger(FavoriteParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public String[] getParks() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<String> favorites = new ArrayList<String>();
		try {
			Query q = pm.newQuery(FavoritePark.class, "User == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<FavoritePark> favoriteParks = (List<FavoritePark>) q.execute(getUser());
			for (FavoritePark favoritePark : favoriteParks) {
				favorites.add(favoritePark.getParkID());
			}
			return (String[]) favorites.toArray(new String[0]);
		} finally {
			pm.close();
		}
	}

	public void addPark(String id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new FavoritePark(getUser(), id));
		} finally {
			pm.close();
		}
	}

	public void removePark(String id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			long deleteCount = 0;
			Query q = pm.newQuery(FavoritePark.class, "User == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<FavoritePark> favoriteParks = (List<FavoritePark>) q.execute(getUser());
			for (FavoritePark favoritePark : favoriteParks) {
				if (id.equals(favoritePark.getParkID())) {
					deleteCount++;
					pm.deletePersistent(favoritePark);
				}
			}
			if (deleteCount != 1) {
				LOG.log(Level.WARNING, "removePark deleted " + deleteCount + " Parks");
			}
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