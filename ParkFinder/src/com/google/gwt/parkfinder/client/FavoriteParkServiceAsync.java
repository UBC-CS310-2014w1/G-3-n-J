package com.google.gwt.parkfinder.client;

import java.util.List;

import com.google.gwt.parkfinder.server.Park;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FavoriteParkServiceAsync {
	public void getParks(AsyncCallback<String[]> async);
	  public void addPark(String id, AsyncCallback<Void> async);
	  public void removePark(String id, AsyncCallback<Void> async);

}
