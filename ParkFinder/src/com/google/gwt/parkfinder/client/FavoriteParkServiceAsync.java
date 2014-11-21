package com.google.gwt.parkfinder.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FavoriteParkServiceAsync {
	
	public void getParks(AsyncCallback<String[]> async);
	
	public void updateParks(List<String> ids, AsyncCallback<Void> async);
}