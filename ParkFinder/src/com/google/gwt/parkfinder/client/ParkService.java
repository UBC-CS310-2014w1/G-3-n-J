package com.google.gwt.parkfinder.client;

import java.io.IOException;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.parkfinder.client.Park;

@RemoteServiceRelativePath("Park")
public interface ParkService extends RemoteService {
	List<Park> getParkList() throws IOException;
	Park getPark(String ParkID);
}
