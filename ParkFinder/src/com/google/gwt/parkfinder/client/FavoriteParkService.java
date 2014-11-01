package com.google.gwt.parkfinder.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Park")
public interface FavoriteParkService extends RemoteService {
	// Load the favorite list
	public String[] getParks() throws NotLoggedInException;

	public void addPark(String id) throws NotLoggedInException;

	public void removePark(String id) throws NotLoggedInException;
}
