package com.google.gwt.parkfinder.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FavoriteParkServiceAsync {
	// Load the favorite list
	public void getParks(AsyncCallback<String[]> async);

	public void addPark(String id, AsyncCallback<Void> async);

	public void removePark(String id, AsyncCallback<Void> async);

}
