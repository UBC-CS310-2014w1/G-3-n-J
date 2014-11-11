
package com.google.gwt.parkfinder.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.parkfinder.server.Park;

public interface ParkServiceAsync {
	
	// Store the parsed list of parks
	public void storeParkList(AsyncCallback<Void> async);
	
	// Return the stored list of parks
	public void getParkList(AsyncCallback<List<Park>> async);

	// Return a single park by inputing the ParkID
	// Used for query
	public void getParkInfo(String id, AsyncCallback<Park> async);
}
