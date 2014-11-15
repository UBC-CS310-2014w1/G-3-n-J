package com.google.gwt.parkfinder.client;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
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
import com.google.gwt.parkfinder.filter.NeighbourhoodFilter;
import com.google.gwt.parkfinder.server.Park;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;

/**
 * Entry point classes define onModuleLoad()
 */
public class ParkFinder implements EntryPoint {
	private HorizontalPanel mapPanel = new HorizontalPanel();
	public MapWidget map;

	private TabPanel tabPanel = new TabPanel();
	private VerticalPanel searchTabPanel = new VerticalPanel();
	private VerticalPanel favouritesTabPanel = new VerticalPanel();
	
	private VerticalPanel loginPanel = new VerticalPanel();
	private LoginInfo loginInfo = null;
	private Label loginLabel = new Label("Please sign in to your Google Account to access the ParkFinder application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");

	private List<Park> parkList = new ArrayList<Park>();
	private List<String> favoriteParkList = new ArrayList<String>();

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

		retrieveParkInformation();
		initAdminBar();
		initTabPanels();
		initTabs();

		RootPanel.get("mapPanel").add(mapPanel);
		RootPanel.get("searchContainer").add(tabPanel);
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
		map.getInfoWindow().open(map.getCenter(), new InfoWindowContent("Ravine Park"));

		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(map, 700);

		// Add the map to the HTML host page
		RootPanel.get("mapPanel").add(dock);
	}

	private void initAdminBar() {

		if ((loginInfo.getNickname().equals("grrraham"))||
				(loginInfo.getNickname().equals("acie.liang"))||
				(loginInfo.getNickname().equals("shineoutloudlol"))||
				(loginInfo.getNickname().equals("joshparkes24"))) {
				

			Button adminButton = new Button("ADMIN", new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					DialogBox adminBox = new DialogBox();
					adminBox.setText("Administrator Panel");
					final VerticalPanel adminPanel = new VerticalPanel();
					
					Button parseButton = new Button("Parse Data", new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							parkService.storeParkList(new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable error) {
									Label parseFailed = new Label("Error: Failed to Parse Data");
									adminPanel.add(parseFailed);
								}

								@Override
								public void onSuccess(Void ignore) {
									Label parseSuccess = new Label("Successfully Parsed Data into Database");
									adminPanel.add(parseSuccess);
								}
							});
						}
					});

					
					Button displayButton = new Button("Show Database", new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							Label databaseSize = new Label("There are " + parkList.size() + " park entries in the database.");
							Grid dataGrid = parkGrid(parkList, 10);
							adminPanel.add(databaseSize);
							adminPanel.add(dataGrid);
						}
					});

					HorizontalPanel buttonPanel = new HorizontalPanel();
					buttonPanel.add(parseButton);
					buttonPanel.add(displayButton);

					adminPanel.add(buttonPanel); 
					adminBox.add(adminPanel);
					adminBox.center();
					adminBox.setAutoHideEnabled(true);
					adminBox.show();
				}
			});
			RootPanel.get("signInOut").add(adminButton);
		}

		RootPanel.get("signInOut").add(signOutLink);
	}
	
	private void initTabs() {
		tabPanel.setWidth("100%");
		tabPanel.add(searchTabPanel, "Search");
		tabPanel.add(favouritesTabPanel, "Favourites");
		tabPanel.selectTab(0);
		
		// TODO: Remove neighbourhood filtering tab once implemented into search tab.
		testNeighbourhoodFilterTab();
	}

	private void initTabPanels() {
		loadSearchTabContent();
		loadFavoritesTabContent();
	}

	private void testNeighbourhoodFilterTab() {
		// Created to temporarily test neighbourhood filtering
		// TODO: Sort results alphabetically, add scrollable list, implement neighbourhood filtering into search tab somehow.
		final VerticalPanel vPanel = new VerticalPanel(); 
		tabPanel.add(vPanel, "Neighbourhood Filter");
		
		final Tree neighbourhoodTree = new Tree();
		
		TreeItem neighbourhoods = new TreeItem();
		neighbourhoods.setText("Select your preferred heighbourhoods:");
		
		// Check box for all 
		CheckBox downtown = new CheckBox("Downtown");
		neighbourhoods.addItem(downtown);
		CheckBox arbutusRidge = new CheckBox("Arbutus Ridge");
		neighbourhoods.addItem(arbutusRidge);
		CheckBox dunbar = new CheckBox("Dunbar-Southlands");
		neighbourhoods.addItem(dunbar);
		CheckBox fairview = new CheckBox("Fairview");
		neighbourhoods.addItem(fairview);
		CheckBox grandview = new CheckBox("Grandview-Woodland");
		neighbourhoods.addItem(grandview);
		CheckBox hastings = new CheckBox("Hastings-Sunrise");
		neighbourhoods.addItem(hastings);
		CheckBox kensignton = new CheckBox("Kensington-Cedar Cottage");
		neighbourhoods.addItem(kensignton);
		CheckBox kerrisdale = new CheckBox("Kerrisdale");
		neighbourhoods.addItem(kerrisdale);
		CheckBox killanary = new CheckBox("Killarney");
		neighbourhoods.addItem(killanary);
		CheckBox kits = new CheckBox("Kitsilano");
		neighbourhoods.addItem(kits);
		CheckBox marpole = new CheckBox("Marpole");
		neighbourhoods.addItem(marpole);
		CheckBox mp = new CheckBox("Mount Pleasant");
		neighbourhoods.addItem(mp);
		CheckBox oak = new CheckBox("Oakridge");
		neighbourhoods.addItem(oak);
		CheckBox renfrew = new CheckBox("Renfrew-Collingwood");
		neighbourhoods.addItem(renfrew);
		CheckBox riley = new CheckBox("Riley-Little Mountain");
		neighbourhoods.addItem(riley);
		CheckBox shaughnessy = new CheckBox("Shaughnessy");
		neighbourhoods.addItem(shaughnessy);
		CheckBox sc = new CheckBox("South Cambie");
		neighbourhoods.addItem(sc);
		CheckBox strathcona = new CheckBox("Strathcona");
		neighbourhoods.addItem(strathcona);
		CheckBox sunset = new CheckBox("Sunset");
		neighbourhoods.addItem(sunset);
		CheckBox victoria = new CheckBox("Victoria-Fraserview");
		neighbourhoods.addItem(victoria);
		CheckBox we = new CheckBox("West End");
		neighbourhoods.addItem(we);
		CheckBox wpg = new CheckBox("West Point Grey");
		neighbourhoods.addItem(wpg);
		
		
		neighbourhoodTree.addItem(neighbourhoods);
		vPanel.add(neighbourhoodTree);
		
		Button searchNeighbourhoodBtn = new Button("Search", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (vPanel.getWidgetCount() > 2)
					vPanel.remove(2);
				
				TreeItem neighbourhoodList = neighbourhoodTree.getItem(0);
				int numNeighbourhoods = neighbourhoodList.getChildCount();
				List<String> chosenNBH = new LinkedList<String>();
				for (int i = 0; i < numNeighbourhoods; i++) {
					CheckBox box = (CheckBox) neighbourhoodList.getChild(i).getWidget();
					if (box.getValue())
						chosenNBH.add(neighbourhoodList.getChild(i).getText());
				}
				
				NeighbourhoodFilter nbhFilter = new NeighbourhoodFilter(chosenNBH);
				if (parkList != null) {
				List<Park> filteredList = nbhFilter.filter(parkList);
				CellList<String> filtered = parkCellList(filteredList);
				vPanel.add(filtered);
				}
				
			}
			
		});
		
		vPanel.add(searchNeighbourhoodBtn);
		
		
	}

	private void loadSearchTabContent() {
		Label searchLabel = new Label("Search by Name or Address");
		HorizontalPanel searchPanel = new HorizontalPanel();
		searchTabPanel.add(searchLabel);
		final TextBox field = new TextBox();
		Button search = new Button("SEARCH");
		searchPanel.add(field);
		searchPanel.add(search);
		searchTabPanel.add(searchPanel);

		field.setFocus(true);

		search.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				final String symbol = field.getText();
				field.setFocus(true);
				field.setText("");


				int length = symbol.length();
				int i = 0;

				if (symbol.charAt(0) >= '0' && symbol.charAt(0) <= '9') {


					while (symbol.charAt(i) != ' ' && i + 1 < length) {
						i++;
					}
					if (i + 1 >= length) {
						Label invalidAddress = new Label("Invalid park address");
						searchTabPanel.add(invalidAddress);
					} else {
						searchByAddress(symbol, i, length);
					}
				}
				else {
					searchByName(symbol);
				}
			}
		});
	}

	private void searchByName(String symbol) {
		List<Park> nameMatched = new ArrayList<Park>();
		for (Park park : parkList) {
			if (park.getName().toLowerCase().contains(symbol.toLowerCase())) {
				nameMatched.add(park);
			}
		}
		
		if (searchTabPanel.getWidgetCount() > 2) {
			// Clears previous search results
			searchTabPanel.remove(2);
		}
		
		if (nameMatched.isEmpty()) {
			Label noMatchingPark = new Label("There are no park with that name.");
			searchTabPanel.add(noMatchingPark);
		} else {
			searchTabPanel.add(parkCellList(nameMatched));
		}
	}

	private void searchByAddress(String symbol, int i, int length) {
		final String house = symbol.substring(0, i);
		final String street = symbol.substring(i + 1, length);

		List<Park> addressMatched = new ArrayList<Park>();
		for (Park park : parkList) {
			if (park.getStreetName().toLowerCase()
					.contains(street.toLowerCase())) {
				addressMatched.add(park);
			}
		}
		
		if (searchTabPanel.getWidgetCount() > 2) {
			// Clears previous search results
			searchTabPanel.remove(2);
		}
		
		if (addressMatched.isEmpty()) {
			Label noMatchingPark = new Label("There are no park with that address.");
			searchTabPanel.add(noMatchingPark);
		} else {
			int j = 0;
			List<Park> singleMatch = new ArrayList<Park>();
			while (singleMatch.isEmpty() && j < addressMatched.size()) {
				if (house.equals(addressMatched.get(j).getStreetNumber())) {
					singleMatch.add(addressMatched.get(j));
					searchTabPanel.add(parkCellList(singleMatch));
				} else
					j++;
			}
			if (singleMatch.isEmpty()) {
				Label zeroExactMatch = new Label("No park has the given address. Did you mean:");
				searchTabPanel.add(zeroExactMatch);
				searchTabPanel.add(parkCellList(addressMatched));
			}
		}
	}

	private void loadFavoritesTabContent() {
		retrieveFavoriteParkInformation();
		if (favoriteParkList.size() == 0) {
			Label noFavoritePark = new Label("You do not have any favorite park.");
			System.out.println("this should come out second");
			favouritesTabPanel.add(noFavoritePark);
		} else {
			final List<Park> favoriteParks = new ArrayList<Park>();
			for (String id : favoriteParkList) {
				for (Park park : parkList) {
					if (park.getParkID().equals(id)) {
						favoriteParks.add(park);
					}
				}
			}
			favouritesTabPanel.add(parkCellList(favoriteParks));
		}
	}

	private void buildParkPage(final Park park, final Panel panel) {
		VerticalPanel allInfo = new VerticalPanel();
		final VerticalPanel favButtonPanel = new VerticalPanel();

		// Display name, picture, address, and neighbourhood
		Image img = new Image();
		img.setUrlAndVisibleRect(park.getParkImgUrl(), 0, 0, 333, 250);
		Label address = new Label("Address: " + park.getStreetNumber() + " " + park.getStreetName());
		Label neighbourhood = new Label("Neighbourhood: " + park.getNeighbourhoodName());

		allInfo.add(img);
		allInfo.add(address);
		allInfo.add(neighbourhood);

		// Display facilities.
		
		String parkFacilities = park.getFacility();
		if (!parkFacilities.equalsIgnoreCase("No available facilities.")) {
			Tree facilityTree = new Tree();
			final TreeItem facilitiesRoot = new TreeItem();
			facilitiesRoot.setText("Available Facilities");
			
			List<String> listOfFacilities = Arrays.asList(parkFacilities.split(","));
			for (String facility : listOfFacilities) {
				Label facilityLabel = new Label(facility);
				facilitiesRoot.addItem(facilityLabel);
			}
			facilityTree.addItem(facilitiesRoot);
			allInfo.add(facilityTree);
			
			facilityTree.addMouseDownHandler(new MouseDownHandler() {
				@Override
				public void onMouseDown(MouseDownEvent event) {
					if (facilitiesRoot.getState() == true)
						facilitiesRoot.setState(false);
					else facilitiesRoot.setState(true);
				}
				
			});
		}
		
		// Displays washrooms
		String parkWashrooms = park.getWashroom();
		if (!parkWashrooms.equalsIgnoreCase("No available washrooms.")) {
			Tree washroomTree = new Tree();
			final TreeItem washroomsRoot = new TreeItem();
			washroomsRoot.setText("Washrooms available at:");
			
			List<String> listOfWashrooms = Arrays.asList(parkWashrooms.split("LOCATION:"));
			for (String washroom : listOfWashrooms) {
				if (washroom.length() > 0) {
					String washroomText = washroom.substring(0, washroom.length() - 2);
					Label washroomLabel = new Label(washroomText);
					washroomsRoot.addItem(washroomLabel);
				}
			}
			washroomTree.addItem(washroomsRoot);
			allInfo.add(washroomTree);
			
			washroomTree.addMouseDownHandler(new MouseDownHandler() {
				@Override
				public void onMouseDown(MouseDownEvent event) {
					if (washroomsRoot.getState() == true)
						washroomsRoot.setState(false);
					else washroomsRoot.setState(true);
				}
				
			});
		}

		// Favourites button
		Button favoriteButton = new Button("Add to Favorites", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						String parkID = park.getParkID();
						favoriteParkService.addPark(parkID, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										Label addFavoritesFailed = new Label("Error: Failed to Add " + park.getName() + " to Favorites");
										favButtonPanel.add(addFavoritesFailed);
									}

									@Override
									public void onSuccess(Void result) {
										Label addFavoritesSuccess = new Label(park.getName() + " is saved to Favorites.");
										loadFavoritesTabContent();
										favButtonPanel.add(addFavoritesSuccess);

									}
								});
					}
				});

		Button removeButton = new Button("Remove from Favorites", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						String parkID = park.getParkID();
						favoriteParkService.removePark(parkID, new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										Label removeFavoritesFailed = new Label("Error: Failed to Remove " + park.getName() + " to Favorites");
										panel.add(removeFavoritesFailed);
									}

									@Override
									public void onSuccess(Void result) {
										Label removeFavoritesSuccess = new Label(park.getName() + " is removed from Favorites.");
										loadFavoritesTabContent();
										panel.add(removeFavoritesSuccess);
									}
								});
					}
				});
		
		int i = 0;
		boolean enableAdd = true;
		while (enableAdd && i < favoriteParkList.size()) {
			if (park.getParkID().equals(favoriteParkList.get(i))) {
				enableAdd = false;
			}
			i++;
		}
		if (enableAdd) {
			favoriteButton.setEnabled(true);
			removeButton.setEnabled(false);			
		}else {
			favoriteButton.setEnabled(false);
			removeButton.setEnabled(true);
		}
	

		favButtonPanel.add(favoriteButton);
		favButtonPanel.add(removeButton);
		allInfo.add(favButtonPanel);
		
		panel.add(allInfo);
	}

	private Grid parkGrid(List<Park> parks, int length) {
		if (length == 0) return null;
		Grid dataGrid = new Grid(length + 1, 2);
		dataGrid.setText(0, 0, "Name");
		dataGrid.setText(0, 1, "Address");
		
		for (int i = 0; i < length; i++) {
			String parkName = parks.get(i).getName();
			String parkAddress = parks.get(i).getStreetNumber() + " " + parks.get(i).getStreetName();
			dataGrid.setText(i + 1, 0, parkName);
			dataGrid.setText(i + 1, 1, parkAddress);
		}
		
		return dataGrid;
	}

	private CellList<String> parkCellList(final List<Park> parks) {
		Cell<String> buttonCell = new ClickableTextCell() {

			@Override
			public void onBrowserEvent(Context context, Element parent, final String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
				super.onBrowserEvent(context, parent, value, event, valueUpdater);

// 				Commented out, because exception was being thrown at this line.
//				if (MOUSEOVER.equals(event.getType())) {
//					// TODO: change color when mouseover
//				}


				if (CLICK.equals(event.getType())) {
					final DialogBox message = new DialogBox();
					final ScrollPanel msgPanel = new ScrollPanel();
					msgPanel.setSize("350px", "375px");
					
					message.add(msgPanel);
					
					for (Park park : parks) {
						if (value.equals(park.getName())) {
							message.setText(park.getName());
							buildParkPage(park, msgPanel);
							newMapMarker(park);
						}
					}
					
					message.setAutoHideEnabled(true);
					message.setPopupPosition(300, 150);
					message.show();
				}
			}
		};
		CellList<String> cellList = new CellList<String>(buttonCell);
		cellList.setRowCount(parks.size(), true);
		List<String> parkNames = new ArrayList<String>();
		
		for (Park park : parks) {
			parkNames.add(park.getName());
		}
		
		cellList.setRowData(0, parkNames);
		return cellList;
	}

	private synchronized void retrieveParkInformation() {
		final DialogBox message = new DialogBox();
		final VerticalPanel msgPanel = new VerticalPanel();
		message.add(msgPanel);
		
		parkService.getParkList(new AsyncCallback<List<Park>>() {

			@Override
			public void onFailure(Throwable caught) {
				message.setText("Error: Failed to Retrieve Parks Information.");
				message.setAutoHideEnabled(true);
				message.setPopupPosition(300, 150);
				message.show();
			}

			@Override
			public void onSuccess(List<Park> parks) {
				parkList.clear();
				for (Park park : parks)
					parkList.add(park);
			}
		});
	}
	
	private synchronized void retrieveFavoriteParkInformation() {
		final DialogBox message = new DialogBox();
		final VerticalPanel msgPanel = new VerticalPanel();
		message.add(msgPanel);
		
		favoriteParkService.getParks(new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				message.setText("Error: Failed to Retrieve Favorite Parks Information.");
				message.setAutoHideEnabled(true);
				message.setPopupPosition(300, 150);
				message.show();
			}

			@Override
			public void onSuccess(final String[] favorites) {
				favoriteParkList.clear();
				for (String id : favorites) {
					favoriteParkList.add(id);
				}
				System.out.println("this should come out first");
			}
		});
	}

	private void newMapMarker(final Park park) {
		String latLonString = park.getGoogleMapDest();
		List<String> latLon = Arrays.asList(latLonString.split(","));

		final LatLng markerLocation = LatLng.newInstance(Double.parseDouble(latLon.get(0)), Double.parseDouble(latLon.get(1)));
		Marker marker = new Marker(markerLocation);
		marker.addMarkerClickHandler(new MarkerClickHandler() {
			@Override
			public void onClick(MarkerClickEvent event) {
				// TODO Do something when marker is clicked. Perhaps display park name.
				map.getInfoWindow().open(markerLocation,new InfoWindowContent(park.getName()));
			}
		});
		map.addOverlay(marker);
	}
	
	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
}
