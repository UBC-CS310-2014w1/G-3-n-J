package com.google.gwt.parkfinder.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.parkfinder.server.Park;

public interface ParkServiceAsync {
	public void getParkList(AsyncCallback<List<Park>> async);
	public void getParkInfo(String id, AsyncCallback<Park> async);
}