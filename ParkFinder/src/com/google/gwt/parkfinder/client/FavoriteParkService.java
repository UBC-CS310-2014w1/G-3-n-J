package com.google.gwt.parkfinder.client;

import java.util.List;

import com.google.gwt.parkfinder.client.NotLoggedInException;
import com.google.gwt.parkfinder.server.Park;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Park")
public interface FavoriteParkService extends RemoteService {
	public String[] getParks() throws NotLoggedInException;
	  public void addPark(String id) throws NotLoggedInException;
	  public void removePark(String id) throws NotLoggedInException;
}
