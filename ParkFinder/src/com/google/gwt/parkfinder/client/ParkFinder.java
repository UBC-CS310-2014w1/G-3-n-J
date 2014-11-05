package com.google.gwt.parkfinder.client;

import java.util.List;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.parkfinder.client.LoginInfo;
import com.google.gwt.parkfinder.client.LoginService;
import com.google.gwt.parkfinder.client.LoginServiceAsync;
import com.google.gwt.parkfinder.client.NotLoggedInException;
import com.google.gwt.parkfinder.server.Park;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.dom.client.Style.Unit;

/**
 * Entry point classes define onModuleLoad()
 */
public class ParkFinder implements EntryPoint {
	public MapWidget map;
	
	private HorizontalPanel mapPanel = new HorizontalPanel();
	private TabPanel tabPanel = new TabPanel();
	private VerticalPanel searchTabPanel = new VerticalPanel();
	private VerticalPanel favouritesTabPanel = new VerticalPanel();
	private DialogBox adminBox = new DialogBox();
	private Button adminButton = new Button();
	
	private int sampleNumber = 16;
	private Grid dataGrid = new Grid(sampleNumber + 1, 3);

	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google Account to access the ParkFinder application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private LoginInfo loginInfo = null;

	private final ParkServiceAsync parkService = GWT.create(ParkService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
						handleError(error);
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						if (loginInfo.isLoggedIn()) {
							loadParkFinder();
						} else {
							loadLogin();
						}
					}
				});
	}

	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("signInOut").add(loginPanel);
	}

	private void loadParkFinder() {

		signOutLink.setHref(loginInfo.getLogoutUrl());

		/*
		 * Asynchronously loads the Maps API.
		 * 
		 * The first parameter should be a valid Maps API Key to deploy this
		 * application on a public server, but a blank key will work for an
		 * application served from local host.
		 */
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				buildMapUi();
			}
		});

		initAdmin();
		initTabPanels();
		initTabs();

		RootPanel.get("signInOut").add(signOutLink);
		// Adding admin button to signOutLink div temporarily
		RootPanel.get("signInOut").add(adminButton);
		RootPanel.get("mapPanel").add(mapPanel);
		RootPanel.get("searchContainer").add(tabPanel);

	}

	private void initAdmin() {
		adminButton = new Button("Admin", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				adminBox.setText("Admin Panel");

				Button startParseButton = new Button("Parse data", new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								loadParks();
							}
						});

				adminBox.add(startParseButton);
				adminBox.center();
				adminBox.setAutoHideEnabled(true);
				adminBox.show();
			}
		});
	}

	private void loadParks() {
		parkService.storeParkList(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				System.out.println("Failed to store data.");
			}

			public void onSuccess(Void ignore) {
				System.out.println("storeParkList() ran successfully");
				displayParks();
			}
		});
	}

	private void displayParks() {
		parkService.getParkList(new AsyncCallback<List<Park>>() {

			@Override
			public void onFailure(Throwable error) {
				System.out.println("Failed to get data.");
			}

			@Override
			public void onSuccess(List<Park> parks) {
				System.out.println("getParkList() ran successfully");

				adminBox.setText("Successfully parsed data into database");
				dataGrid.setText(0, 0, "ID");
				dataGrid.setText(0, 1, "Name");
				dataGrid.setText(0, 2, "Address");
				
				int i = 0;
				
				while (parks.get(i) != null && i < sampleNumber) {
					String parkID = parks.get(i).getParkID();
					String parkName = parks.get(i).getName();
					String parkAddress = parks.get(i).getStreetNumber() + " " + parks.get(i).getStreetName();
					dataGrid.setText(i+1, 0, parkID);
					dataGrid.setText(i+1, 1, parkName);
					dataGrid.setText(i+1, 2, parkAddress);
					i++;
				}
				adminBox.setWidget(dataGrid);
			}
		});
	}

	private void buildMapUi() {
		LatLng mapCenter = LatLng.newInstance(49.240902, -123.155935);

		map = new MapWidget(mapCenter, 12);
		map.setSize("100%", "100%");

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

	private void initTabPanels() {
		loadSearchTabContent();
		favouritesTabPanel.add(new Button("Favourites content here"));
	}
	
	private void loadSearchTabContent() {
		Button searchButton = new Button("Arbutus Ridge Park Page Preview",
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						final DialogBox message = new DialogBox();
						final VerticalPanel msgPanel = new VerticalPanel();
						message.add(msgPanel);
						
						parkService.getParkList(new AsyncCallback<List<Park>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								System.out.println("Cannot get park list");
							}

							@Override
							public void onSuccess(List<Park> parks) {
								Park samplePark = parks.get(0);
								message.setText(samplePark.getName());
								buildParkPage(samplePark, msgPanel);
							}
							
						});
						
						message.setAutoHideEnabled(true);
						message.setPopupPosition(300, 150);
						message.show();
					}
				});
		searchTabPanel.add(searchButton);
	}
	
	private void buildParkPage(Park park, Panel panel) {
		Image img = new Image();
		img.setUrlAndVisibleRect("http://www.google.com/images/logo.gif", 0, 0, 276, 110);
		
		TextBox address = new TextBox();
		address.setText("Address: " + park.getStreetNumber() + " " + park.getStreetName());
		address.setWidth("250px");
		
		TextBox nb = new TextBox();
		nb.setText("Neighbourhood: " + park.getNeighbourhoodName());
		nb.setWidth("250px");
		
		panel.add(img);
		panel.add(nb);
		panel.add(address);
		panel.add(new Button("Add to Favourites"));
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
}
