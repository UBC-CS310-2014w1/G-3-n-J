package com.google.gwt.parkfinder.client;

import java.io.IOException;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.parkfinder.server.Park;

@RemoteServiceRelativePath("Park")
public interface ParkService extends RemoteService {
	// Load entire park list
	public List<Park> getParkList() throws IOException, NotLoggedInException;

	// Return a single park by inputing the ParkID
	// Used for query
	public Park getParkInfo(String id) throws NotLoggedInException;
	
	// Return a list of parks by inputing the Name
	// Used for query
	public List<Park> searchName(String name) throws NotLoggedInException;
}