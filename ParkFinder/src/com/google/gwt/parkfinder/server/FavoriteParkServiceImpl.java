package com.google.gwt.parkfinder.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.parkfinder.client.Facility;
import com.google.gwt.parkfinder.client.Washroom;

public class FavoriteParkServiceImpl {
	
	private static final Logger LOG = Logger.getLogger(FavoriteParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
		      JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public void addPark(String Name, String StreetNumber, String StreetName, String GoogleMapDest, String NeighbourhoodName, 
			String NeighbourhoodURL, LinkedList <Facility> Facility, LinkedList <Washroom> Washroom) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new Park(Name, StreetNumber, StreetName, GoogleMapDest, NeighbourhoodName, 
					NeighbourhoodURL, Facility, Washroom));
		} finally {
			pm.close();
		}
	}
	
	public String[] getParks() {
		PersistenceManager pm = getPersistenceManager();
		List<String> symbols = new ArrayList<String>();
		 try {
		      Query q = pm.newQuery(Park.class, "user == u");
		      q.declareParameters("com.google.appengine.api.users.User u");
		      q.setOrdering("createDate");
		      List<Park> parks = (List<Park>) q.execute(getUser());
		      for (Park park : parks) {
		        symbols.add(park.getName());
		      }
		    } finally {
		      pm.close();
		    }
		    return (String[]) symbols.toArray(new String[0]);
		  }
	
	private User getUser() {
		 UserService userService = UserServiceFactory.getUserService();
		 return userService.getCurrentUser();
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}
