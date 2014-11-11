package com.google.gwt.parkfinder.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.parkfinder.client.LoginInfo;
import com.google.gwt.parkfinder.client.NotLoggedInException;
import com.google.gwt.parkfinder.client.LoginService;
import com.google.gwt.parkfinder.client.LoginServiceAsync;
import com.google.gwt.parkfinder.client.ParkService;
import com.google.gwt.parkfinder.client.ParkServiceAsync;
import com.google.gwt.parkfinder.client.FavoriteParkService;
import com.google.gwt.parkfinder.client.FavoriteParkServiceAsync;
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
	private HorizontalPanel mapPanel = new HorizontalPanel();
	public MapWidget map; // public final LatLng mapCenter = LatLng.newInstance(49.240902, -123.155935);

	private TabPanel tabPanel = new TabPanel();
	private VerticalPanel searchTabPanel = new VerticalPanel();
	private VerticalPanel favouritesTabPanel = new VerticalPanel();
	
	private VerticalPanel adminPanel = new VerticalPanel();
	private Button adminButton = new Button();
	private DialogBox adminBox = new DialogBox();
	HorizontalPanel buttonPanel = new HorizontalPanel();
	
	private VerticalPanel loginPanel = new VerticalPanel();
	private LoginInfo loginInfo = null;
	private Label loginLabel = new Label("Please sign in to your Google Account to access the ParkFinder application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	HorizontalPanel searchNamePanel = new HorizontalPanel();
	final TextBox nameField = new TextBox();
	Button searchName = new Button("Search");

	private final ParkServiceAsync parkService = GWT.create(ParkService.class);
	private final FavoriteParkServiceAsync favoriteParkService = GWT.create(FavoriteParkService.class);

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
		// manually enter names of admins!!
		/** temporarily disable the admin-only feature
		if ((loginInfo.getNickname().equals("grrraham"))||
				(loginInfo.getNickname().equals("acie.liang"))||
				(loginInfo.getNickname().equals("shineoutloudlol"))||
				(loginInfo.getNickname().equals("joshparkes24"))){
		RootPanel.get("signInOut").add(adminButton);
		}
		*/
		RootPanel.get("signInOut").add(adminButton);
		
		RootPanel.get("mapPanel").add(mapPanel);
		RootPanel.get("searchContainer").add(tabPanel);
//		String ASDF = loginInfo.getNickname();
//		Button debugPanel = new Button(ASDF);
//		RootPanel.get("signInOut").add(debugPanel);
	}
	
	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("signInOut").add(loginPanel);
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

	private void initAdmin() {
		adminButton = new Button("ADMIN", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				adminBox.setText("Administrator Panel");
				Button parseButton = new Button("Parse Data", new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								loadParks();
							}
						});

				Button displayButton = new Button("Show Database", new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								displayParks();
							}
						});

				buttonPanel.add(parseButton);
				buttonPanel.add(displayButton);
				adminPanel.add(buttonPanel);
				adminBox.add(adminPanel);
				adminBox.center();
				adminBox.setAutoHideEnabled(true);
				adminBox.show();
			}
		});
	}

	private void loadParks() {
		parkService.storeParkList(new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(Throwable error) {
				Label parseFailed = new Label("Error: Failed to Parse Data"); 
				adminPanel.add(parseFailed);
			}

			@Override
			public void onSuccess(Void ignore) {
				Label parseSuccess = new Label("Data Successfully Parsed into Database");
				adminPanel.add(parseSuccess);
			}
		});
	}

	private void displayParks() {
		parkService.getParkList(new AsyncCallback<List<Park>>() {

			@Override
			public void onFailure(Throwable error) {
				Label displayFailed = new Label("Error: Failed to Display Database");
				adminPanel.add(displayFailed);
			}

			@Override
			public void onSuccess(List<Park> parks) {
				int listSize = parks.size();
				Label databaseSize = new Label("There are a total of " + listSize + " entries in the database.");
				
				int sampleNumber = 10;
				
				Grid dataGrid = parkGrid(parks, sampleNumber);
				
				adminPanel.add(databaseSize);
				adminPanel.add(dataGrid);
			}
		});
	}
	
	private Grid parkGrid(List<Park> parks, int length) {
		List<Park> output = new ArrayList<Park>();
		int i;
		for (i = 0; i < length; i++) {
			output.add(parks.get(i));
		}
		return parkGrid(output);
	}
	
	private Grid parkGrid(List<Park> parks) {
		int length = parks.size();
		if (length == 0) return null;
		Grid dataGrid = new Grid(length + 1, 2);
		
		dataGrid.setText(0, 0, "Name");
		dataGrid.setText(0, 1, "Address");
		
		for(int i = 0; i < length; i++){
			String parkName = parks.get(i).getName();
			String parkAddress = parks.get(i).getStreetNumber() + " " + parks.get(i).getStreetName();
			dataGrid.setText(i+1, 0, parkName);
			dataGrid.setText(i+1, 1, parkAddress);
		}
		return dataGrid;
	}

	private void initTabs() {
		tabPanel.setWidth("100%");
		tabPanel.add(searchTabPanel, "Search");
		tabPanel.add(favouritesTabPanel, "Favourites");
		tabPanel.selectTab(0);
	}

	private void initTabPanels() {
		loadSearchTabContent();
		loadFavoritesTabContent();
	}
				
	private void loadSearchTabContent() {
		searchByName();
		Button testButton = new Button("Arbutus Ridge Park Page Preview", new ClickHandler() {

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
		searchTabPanel.add(testButton);
	}
	
	private void searchByName() {
		Label searchNameLabel = new Label("Search By Name:");
		searchTabPanel.add(searchNameLabel);
		searchNamePanel.add(nameField);
		searchNamePanel.add(searchName);
		searchTabPanel.add(searchNamePanel);

		nameField.setFocus(true);

		searchName.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				filterByName();
			}
		});
	}
		
	private void filterByName() {
		String symbol = nameField.getText();
		nameField.setFocus(true);

		/** look up regular expression
		if (!symbol.matches("^[a-z]{1,10}$")) {
			Window.alert("'" + symbol + "' is not a valid park name.");
			nameField.selectAll();
			return;
		}
		*/
		
		nameField.setText("");

		parkService.searchName(symbol, new AsyncCallback<List<Park>>() {

			@Override
			public void onFailure(Throwable error) {
				System.out.println("Faild to search for name");
				Label searchFailed = new Label("Error: Failed to Search Name");
				searchTabPanel.add(searchFailed);
			}

			@Override
			public void onSuccess(List<Park> parks) {
				if (parks.isEmpty()) {
					Label searchEmpty = new Label("Name does not match with any park.");
					searchTabPanel.add(searchEmpty);
				} else {
					Grid dataGrid = parkGrid(parks);
					searchTabPanel.add(dataGrid);
				}
			}
		});
	}
	
	private void loadFavoritesTabContent() {
		favoriteParkService.getParks(new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable error) {
				Label getParksFailed = new Label("Error: Failed to Get Favorite Parks");
				favouritesTabPanel.add(getParksFailed);
			}

			@Override
			public void onSuccess(final String[] favorites) {
				if (favorites.length == 0) {
					Label noFavoritePark = new Label("You do not have any favorite park.");
					favouritesTabPanel.add(noFavoritePark);
				} else {
					final List<Park> favoriteParks = new ArrayList<Park>();
					parkService.getParkList(new AsyncCallback<List<Park>>() {

						@Override
						public void onFailure(Throwable caught) {
							Label getParksInfoFailed = new Label("Error: Failed to Get Favorite Parks");
							favouritesTabPanel.add(getParksInfoFailed);
						}

						@Override
						public void onSuccess(List<Park> parks) {
							for (String id : favorites) {
								for (Park park : parks) {
									if (id == park.getParkID())
										favoriteParks.add(park);
								}
							}
							Grid dataGrid = parkGrid(favoriteParks);
							favouritesTabPanel.add(dataGrid);
						}
					});
				}
			}
		});
	}
	
	private void buildParkPage(final Park park, final Panel panel) {
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
		
		Button favoriteButton = new Button("Add to favorite", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {	
				String parkID = park.getParkID();
				favoriteParkService.addPark(parkID, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Label addFavoritesFailed = new Label("Error: failed to add to Favorites");
						panel.add(addFavoritesFailed);
					}

					@Override
					public void onSuccess(Void result) {
						Label addFavoritesSuccess = new Label("Successfully added to Favorites");
						panel.add(addFavoritesSuccess);
					}
				});
			}
		});
		panel.add(favoriteButton);
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
}
