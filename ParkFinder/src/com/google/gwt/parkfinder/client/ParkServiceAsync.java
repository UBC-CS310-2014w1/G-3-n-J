package com.google.gwt.parkfinder.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.parkfinder.client.Park;

public interface ParkServiceAsync {
	void getParkList(AsyncCallback<List<Park>> callback);
	void getPark(String id, AsyncCallback<Park> callback);
}
