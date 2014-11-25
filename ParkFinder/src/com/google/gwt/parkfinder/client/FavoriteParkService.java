package com.google.gwt.parkfinder.client;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("FavoritePark")
public interface FavoriteParkService extends RemoteService {
	
	public String[] getParks();
	
	public void updateParks(List<String> ids);
}