package com.google.gwt.parkfinder.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FavoriteParkServiceAsync {
	// Load the favorite list
	public void getParks(AsyncCallback<String[]> async);

	// Add a park by id
	public void addPark(String id, AsyncCallback<Void> async);

	// Remove a park by id
	public void removePark(String id, AsyncCallback<Void> async);
}
