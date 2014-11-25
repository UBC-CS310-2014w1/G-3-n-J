package com.google.gwt.parkfinder.client;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
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
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FocusWidget;
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
import com.google.gwt.view.client.Range;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;

/**
 * Entry point classes define onModuleLoad()
 */
public class ParkFinder implements EntryPoint {
	private HorizontalPanel mapPanel = new HorizontalPanel();
	private double userLat = 0;
	private double userLng = 0;
	private double MAP_HEIGHT = Window.getClientHeight() - 100;
	public MapWidget map;

	private TabPanel tabPanel = new TabPanel();
	private VerticalPanel searchTabPanel = new VerticalPanel();
	private ScrollPanel searchTabScrollPanel = new ScrollPanel();
	private VerticalPanel favouritesTabPanel = new VerticalPanel();
	private ScrollPanel favouritesScrollPanel = new ScrollPanel();
	private String TAB_PANEL_HEIGHT = Double.toString(MAP_HEIGHT - 45) + "px";

	private VerticalPanel loginPanel = new VerticalPanel();
	private LoginInfo loginInfo = null;
	private Label loginLabel = new Label("Please sign in to your Google Account to access the ParkFinder application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");

	private FilterPanel filterPanel = new FilterPanel(this); 
	private ScrollPanel filterScrollPanel = new ScrollPanel();

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
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
			}

			@Override
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
		initTabs();
		retrieveFavoriteParkInformation();
		retrieveParkInformation();

		signOutLink.setHref(loginInfo.getLogoutUrl());

		// Update Datastore when signing out
		signOutLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				updateDatastore();
			}
		});

		// Update Datastore when the page closes or refreshes
		HandlerRegistration registration = Window.addWindowClosingHandler(new ClosingHandler() {

			@Override
			public void onWindowClosing(Window.ClosingEvent event) {
				updateDatastore();
			}
		});

		getUserLocation();

		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				buildMapUi();
			}
		});



		// Sequence Map (set up this way to avoid synchronization problem)
		// retrieveParkInformation() -> loadAdminBarContent()
		//							 -> loadSearchTabContent()
		// retrieveFavoriteParkInformation() -> loadFavoriteTabContent()
		//
		//		initTabs();
		//		retrieveParkInformation();
		//		retrieveFavoriteParkInformation();
		//		
		RootPanel.get("mapPanel").add(mapPanel);
		RootPanel.get("searchContainer").add(tabPanel);

		/**
		// Update Datastore every 30 seconds
		Timer t = new Timer() {
			public void run() {
				updateDatastore();
			}
		};

		t.schedule((int) (0.5*1000*60));
		 */
	}

	private void updateDatastore() {
		List<String> newFavoriteList = (List<String>) favoriteParkList;
		favoriteParkService.updateParks(newFavoriteList, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				messageHandler(1);
			}

			@Override
			public void onSuccess(Void result) {
				// NULL
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

	private void buildMapUi() {
		LatLng defaultCenter = LatLng.newInstance(49.258105,-123.142395);
		map = new MapWidget(defaultCenter, 12);

		map.setSize("100%", "100%");
		map.addControl(new LargeMapControl());

		final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
		dock.addNorth(map, MAP_HEIGHT);

		RootPanel.get("mapPanel").add(dock);
	}

	private void initTabs() {
		tabPanel.setWidth("100%");

		tabPanel.add(searchTabScrollPanel, "Search");
		searchTabScrollPanel.setHeight(TAB_PANEL_HEIGHT);
		searchTabScrollPanel.add(searchTabPanel);

		tabPanel.add(favouritesScrollPanel, "Favourites");
		favouritesScrollPanel.setHeight(TAB_PANEL_HEIGHT);
		favouritesScrollPanel.add(favouritesTabPanel);

		tabPanel.add(filterScrollPanel, "Filter");
		filterScrollPanel.setHeight(TAB_PANEL_HEIGHT);
		filterScrollPanel.add(filterPanel);
		tabPanel.selectTab(0);
	}

	private void retrieveParkInformation() {
		parkService.getParkList(new AsyncCallback<List<Park>>() {
			@Override
			public void onFailure(Throwable caught) {
				messageHandler(2);
			}

			@Override
			public void onSuccess(List<Park> parks) {
				parkList.clear();
				for (Park park : parks)
					parkList.add(park);

				loadAdminBarContent();
				loadSearchTabContent();
			}
		});
	}

	private void retrieveFavoriteParkInformation() {
		favoriteParkService.getParks(new AsyncCallback<String[]>() {
			@Override
			public void onFailure(Throwable caught) {
				messageHandler(3);
			}

			@Override
			public void onSuccess(final String[] favorites) {
				favoriteParkList.clear();
				for (String id : favorites)
					favoriteParkList.add(id);

				loadFavoritesTabContent();
			}
		});
	}

	private void loadAdminBarContent() {
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

							int widgetCount = adminPanel.getWidgetCount();
							if (widgetCount > 1) {
								for (int i = widgetCount - 1; i >= 1; i--)
									adminPanel.remove(i);
							}

							parkService.storeParkList(new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable error) {
									messageHandler(4);
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

							int widgetCount = adminPanel.getWidgetCount();
							if (widgetCount > 1) {
								for (int i = widgetCount - 1; i >= 1; i--)
									adminPanel.remove(i);
							}

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

		favouritesTabPanel.clear();
		loadFavoritesTabContent();

		RootPanel.get("signInOut").add(signOutLink);
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
				clearOldSearch(searchTabPanel);

				final String symbol = field.getText();
				field.setFocus(true);
				field.setText("");
				search(symbol);
			}
		});

		field.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					clearOldSearch(searchTabPanel);

					final String symbol = field.getText();
					field.setFocus(true);
					field.setText("");
					search(symbol);
				}
			}
		});
	}

	private void search(String symbol) {
		int length = symbol.length();
		int i = 0;
		if (symbol.charAt(0) >= '0' && symbol.charAt(0) <= '9') {
			while (symbol.charAt(i) != ' ' && i + 1 < length) {
				i++;
			}
			if (i + 1 >= length) {
				messageHandler(5);
			} else {
				searchByAddress(symbol, i, length);
			}
		}
		else {
			searchByName(symbol);
		}
	}

	private void clearOldSearch(Panel p) {
		map.getInfoWindow().close();

		if (p.equals(searchTabPanel)) {
			while (searchTabPanel.getWidgetCount() > 2) {
				searchTabPanel.remove(searchTabPanel.getWidgetCount() -1);
			}
		}

		map.clearOverlays();
	}


	private void searchByName(String symbol) {
		List<Park> nameMatched = new ArrayList<Park>();;

		for (Park park : parkList) {
			if (park.getName().toLowerCase().contains(symbol.toLowerCase())) {
				nameMatched.add(park);
			}
		}

		if (nameMatched.isEmpty()) {
			Label noMatchingPark = new Label("There are no park with that name.");
			searchTabPanel.add(noMatchingPark);
		} else {
			newMapMarker(nameMatched);
			searchTabPanel.add(parkCellList(nameMatched));
		}
	}

	private void searchByAddress(String symbol, int i, int length) {
		final String house = symbol.substring(0, i);
		final String street = symbol.substring(i + 1, length);

		List<Park> addressMatched = new ArrayList<Park>();
		for (Park park : parkList) {
			if (park.getStreetName().toLowerCase().contains(street.toLowerCase())) {
				addressMatched.add(park);
			}
		}

		int j = 0;
		List<Park> singleMatch = new ArrayList<Park>();
		while (singleMatch.isEmpty() && j < addressMatched.size()) {
			if (house.equals(addressMatched.get(j).getStreetNumber())) {
				singleMatch.add(addressMatched.get(j));
				searchTabPanel.add(parkCellList(singleMatch));
			} else
				j++;
		}

		if (singleMatch.isEmpty()) 
			findNearbyParks(symbol);

	}

	private void findNearbyParks(String address) {
		final TreeMap<Double, Park> nearbyParks = new TreeMap<Double, Park>();

		Geocoder geocoder = new Geocoder();
		geocoder.getLatLng(address, new LatLngCallback() {

			@Override
			public void onFailure() {
				Label error = new Label("Invalid address. Please try again.");
				searchTabPanel.add(error);
			}

			@Override
			public void onSuccess(final LatLng point) {

				for (Park park : parkList) {
					LatLng loc = LatLng.newInstance(park.getLat(), park.getLon());
					double dist = loc.distanceFrom(point);
					if (nearbyParks.size() > 10) {
						// Greater than 10 closest parks
						if (dist < nearbyParks.lastKey()) {
							nearbyParks.remove(nearbyParks.lastKey());
							nearbyParks.put(dist, park);
						}
					} else {
						// if less than 10 parks in map
						nearbyParks.put(dist, park);
					}
				}

				Collection<Park> parks = nearbyParks.values();
				ArrayList<Park> closestParks = new ArrayList<Park>();
				for (Park p : parks) {
					closestParks.add(p);
				}

				Label info = new Label("There is no park in Vancouver with that address. Here are the closest parks to the given address.");
				newMapMarker(closestParks);
				searchTabPanel.add(info);
				searchTabPanel.add(parkCellList(closestParks));
			}
		});
	}




	private void loadFavoritesTabContent() {
		if (favouritesTabPanel.getWidgetCount() > 0) {
			// Clears previous search results
			favouritesTabPanel.remove(0);
			map.clearOverlays();
		}

		if (favoriteParkList.size() == 0) {
			Label noFavoritePark = new Label("You do not have any favorite park.");
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
		if (!park.getParkImgUrl().equals("No available image.")) {
			Image img = new Image();
			img.setUrlAndVisibleRect(park.getParkImgUrl(), 0, 0, 333, 250);
			allInfo.add(img);
		}

		Label address = new Label("Address: " + park.getStreetNumber() + " " + park.getStreetName());
		allInfo.add(address);

		// Gives user directions to the park if location services enabled.
		if (userLat != 0 && userLng != 0) {
			Anchor directions = new Anchor("Get Directions to " + park.getName());
			directions.setTarget("_blank");
			directions.setHref("https://maps.google.com?saddr=" + Double.toString(userLat) + ","
					+ Double.toString(userLng) + "&daddr=" + Float.toString(park.getLat()) + "," +
					Float.toString(park.getLon()));
			allInfo.add(directions);
		}

		Label neighbourhood = new Label("Neighbourhood: " + park.getNeighbourhoodName());
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
		final Button favoriteButton = new Button("Add to Favorites");
		final Button removeButton = new Button("Remove from Favorites");

		favoriteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				String parkID = park.getParkID();

				if (favButtonPanel.getWidgetCount() > 2) {
					favButtonPanel.remove(2);
				}

				favoriteParkList.add(parkID);
				favouritesTabPanel.clear();
				loadFavoritesTabContent();
				Label addFavoritesSuccess = new Label(park.getName() + " is saved to Favorites.");
				favButtonPanel.add(addFavoritesSuccess);

				((FocusWidget) event.getSource()).setEnabled(false);
				removeButton.setEnabled(true);
			}
		});

		removeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				String parkID = park.getParkID();

				if (favButtonPanel.getWidgetCount() > 2) {
					favButtonPanel.remove(2);
					map.clearOverlays();
				}

				favoriteParkList.remove(parkID);
				favouritesTabPanel.clear();
				loadFavoritesTabContent();
				Label removeFavoritesSuccess = new Label(park.getName() + " is removed from Favorites.");
				favButtonPanel.add(removeFavoritesSuccess);

				((FocusWidget) event.getSource()).setEnabled(false);
				favoriteButton.setEnabled(true);
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
		} else {
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

	public List<Park> getParks() {
		return parkList;
	}


	CellList<String> parkCellList(final List<Park> parks) {
		final Cell<String> buttonCell = new ClickableTextCell() {

			@Override
			public void onBrowserEvent(Context context, Element parent, final String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
				super.onBrowserEvent(context, parent, value, event, valueUpdater);

				if (MOUSEOVER.equals(event.getType())) {
					// TODO Change color or highlight the text & background when mouse-over. 
					// setStyleName("highlight"); // create "highlight" in CSS
				}


				if (CLICK.equals(event.getType())) {
					final DialogBox message = new DialogBox();
					final ScrollPanel msgPanel = new ScrollPanel();
					msgPanel.setSize("350px", "375px");

					message.add(msgPanel);

					for (Park park : parks) {
						if (value.equals(park.getName())) {
							message.setText(park.getName());
							buildParkPage(park, msgPanel);
							showParkMarkerPopup(park);
						}
					}

					message.setAutoHideEnabled(true);
					message.setPopupPosition(Window.getClientWidth()/2 - 175, Window.getClientHeight()/2 - 190);
					message.show();
				}
			}
		};
		CellList<String> cellList = new CellList<String>(buttonCell);
		cellList.setVisibleRange(new Range(0, parks.size()));

		List<String> parkNames = new ArrayList<String>();

		for (Park park : parks) {
			parkNames.add(park.getName());
		}

		if (!parkNames.isEmpty()) {
			cellList.setRowData(0, parkNames);
		}

		return cellList;

	}


	private void messageHandler(int code) {
		final DialogBox message = new DialogBox();

		switch (code) {
		case 1:
			message.setText("Failed to update favorite park list. Check updateDatastore()");
			break;
		case 2:
			message.setText("Failed to get park list. Check retrieveParkInformation()");
			break;
		case 3:
			message.setText("Failed to get favorite park list. Check retrieveFavoriteParkInformation()");
			break;
		case 4:
			message.setText("Failed to store park list. Check loadAdminBarContent()");
			break;
		default:
			message.setText("unknown error");
		}

		message.setAutoHideEnabled(true);
		message.setPopupPosition(300, 150);
		message.show();
	}

	private void getUserLocation() {
		Geolocation userLoc = Geolocation.getIfSupported();
		userLoc.getCurrentPosition(new Callback<Position, PositionError>() {

			@Override
			public void onFailure(PositionError reason) {
				// Do nothing
			}

			@Override
			public void onSuccess(Position result) {
				// TODO Auto-generated method stub
				Coordinates loc = result.getCoordinates();
				userLat = loc.getLatitude();
				userLng = loc.getLongitude();
			}

		});
	}

	private void newMapMarker(final Park park) {
		final LatLng markerLocation = LatLng.newInstance(park.getLat(), park.getLon());
		Marker marker = new Marker(markerLocation);
		marker.addMarkerClickHandler(new MarkerClickHandler() {
			@Override
			public void onClick(MarkerClickEvent event) {
				showParkMarkerPopup(park);
			}
		});
		map.addOverlay(marker);
	}

	public void newMapMarker(List<Park> parks) {
		for (Park park : parks) {
			newMapMarker(park);
		}
	}

	public void clearMap() {
		map.clearOverlays();
	}

	public float getUserLat() {
		return (float) userLat;
	}

	public float getUserLon() {
		return (float) userLng;
	}

	private void showParkMarkerPopup(Park park) {
		LatLng markerLocation = LatLng.newInstance(park.getLat(), park.getLon());

		if (userLat != 0 && userLng != 0) {
			VerticalPanel parkNameDist = new VerticalPanel();

			LatLng parkLoc = LatLng.newInstance(park.getLat(), park.getLon());
			LatLng userLoc = LatLng.newInstance(userLat, userLng);
			double distance = parkLoc.distanceFrom(userLoc);
			// Conversion to kilometers
			distance = distance / 1000;
			Label nameLabel = new Label(park.getName());
			Label distLabel = new Label("Approximately " + Integer.toString((int)distance) + "km away from you.");

			parkNameDist.add(nameLabel);
			parkNameDist.add(distLabel);

			map.getInfoWindow().open(markerLocation, new InfoWindowContent(parkNameDist));
		} else {
			map.getInfoWindow().open(markerLocation, new InfoWindowContent(park.getName()));
		}
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}

}


