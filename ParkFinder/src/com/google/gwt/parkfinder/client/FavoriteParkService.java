package com.google.gwt.parkfinder.client;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("FavoritePark")
public interface FavoriteParkService extends RemoteService {
	// Load the favorite list
	public String[] getParks() throws NotLoggedInException;

	// Add a park by id
	public void addPark(String id) throws IOException, NotLoggedInException;

	// Remove a park by id
	public void removePark(String id) throws NotLoggedInException;
}
