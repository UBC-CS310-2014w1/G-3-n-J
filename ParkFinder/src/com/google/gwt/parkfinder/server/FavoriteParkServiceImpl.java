package com.google.gwt.parkfinder.server;

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
import com.google.gwt.parkfinder.server.FavoritePark;
import com.google.gwt.parkfinder.client.FavoriteParkService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FavoriteParkServiceImpl extends RemoteServiceServlet implements FavoriteParkService {

	private static final Logger LOG = Logger.getLogger(FavoriteParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	@Override
	public String[] getParks() {
		PersistenceManager pm = getPersistenceManager();
		List<String> parks = new ArrayList<String>();

		try {
			Query q = pm.newQuery(FavoritePark.class, "User == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<FavoritePark> favoriteParks = (List<FavoritePark>) q.execute(getUser());

			if (favoriteParks.size() == 0) {
				return (String[]) parks.toArray(new String[0]);
			} 
			else {
				for (String parkID : favoriteParks.get(0).getParks())
					parks.add(parkID);
			}
		} finally {
			pm.close();
		}
		return (String[]) parks.toArray(new String[0]);
	}

	@Override
	public void updateParks(List<String> parkIDs){
		PersistenceManager pm = getPersistenceManager();
		
		try {
			Query q = pm.newQuery(FavoritePark.class, "User == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<FavoritePark> favoriteParks = (List<FavoritePark>) q.execute(getUser());
			
			for (FavoritePark favoritePark : favoriteParks)
				pm.deletePersistent(favoritePark);
			
		} finally {
			pm.close();
		}
		updateParksHelper(parkIDs);
	}

	public void updateParksHelper(List<String> parkIDs) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new FavoritePark(getUser(), parkIDs));
		} finally {
			pm.close();
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