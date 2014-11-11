package com.google.gwt.parkfinder.client;

import java.io.IOException;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.parkfinder.server.Park;

@RemoteServiceRelativePath("Park")
public interface ParkService extends RemoteService {
	
	// Store the parsed list of parks
	public void storeParkList() throws IOException, NotLoggedInException;
	
	// Return the stored list of parks
	public List<Park> getParkList() throws NotLoggedInException;

	// Return a single park by inputing the ParkID
	// Used for query
	public Park getParkInfo(String id) throws NotLoggedInException;
}

