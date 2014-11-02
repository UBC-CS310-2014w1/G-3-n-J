package com.google.gwt.parkfinder.client;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.parkfinder.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.dom.client.Style.Unit;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParkFinder implements EntryPoint {


	HorizontalPanel mapPanel = new HorizontalPanel();
	TabPanel tabPanel = new TabPanel();
	VerticalPanel favouritesTabPanel = new VerticalPanel();
	VerticalPanel searchTabPanel = new VerticalPanel();


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		/*
		 * Asynchronously loads the Maps API.
		 *
		 * The first parameter should be a valid Maps API Key to deploy this
		 * application on a public server, but a blank key will work for an
		 * application served from localhost.
		 */
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				buildMapUi();
			}
		});
		
		
		
		initPanels();
		initTabs();
		
		RootPanel.get("mapPanel").add(mapPanel);
		RootPanel.get("searchContainer").add(tabPanel);
		
		
	}
	

	private void buildMapUi() {
		LatLng mapCenter = LatLng.newInstance(49.240902, -123.155935);
		
		MapWidget map = new MapWidget(mapCenter, 2);
		
		map.setSize("100%", "100%");
		map.setZoomLevel(12);
		
		// Add some controls for the zoom level
		map.addControl(new LargeMapControl());

		// Add a marker
		map.addOverlay(new Marker(mapCenter));

		// Add an info window to highlight a point of interest
		map.getInfoWindow().open(map.getCenter(),
				new InfoWindowContent("Ravine Park"));

		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(map, 500);

		// Add the map to the HTML host page
		RootPanel.get("mapPanel").add(dock);
	}
	
	
	private void initTabs() {
		tabPanel.setWidth("100%");
		tabPanel.add(searchTabPanel, "Search");
		tabPanel.add(favouritesTabPanel, "Favourites");
		tabPanel.selectTab(0);
		
	}
	
	private void initPanels() {
		Button searchButton = new Button("Potential park page display", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				DialogBox message = new DialogBox();
				message.add(new Button("Park info content"));
				message.setText("Park Name");
				message.setAutoHideEnabled(true);
				message.center();
				message.show();
			}
			
		});
		searchTabPanel.add(searchButton);
		
		favouritesTabPanel.add(new Button("Favourites content here"));
	}
}
