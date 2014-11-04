package com.google.gwt.parkfinder.client;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.dom.client.Style.Unit;


/**
 * Entry point classes define onModuleLoad()
 */
public class ParkFinder implements EntryPoint {
	public MapWidget map;
//	public final LatLng mapCenter = LatLng.newInstance(49.240902, -123.155935);
	
	private HorizontalPanel mapPanel = new HorizontalPanel();
	private TabPanel tabPanel = new TabPanel();
	private VerticalPanel favouritesTabPanel = new VerticalPanel();
	private VerticalPanel searchTabPanel = new VerticalPanel();
	private Button adminButton;

	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access the ParkFinder application.");
	private Anchor signInLink = new Anchor("Sign In"); 
	private Anchor signOutLink = new Anchor("Sign Out");
	
	private final ParkServiceAsync parkService = GWT.create(ParkService.class);
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if(loginInfo.isLoggedIn()) {
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
		 * application served from localhost.
		 */
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				buildMapUi();
			}
		});


		initAdmin();
		initPanels();
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
				DialogBox adminBox = new DialogBox();
//				Grid parsedDataGridDisplay = new Grid(2, 10);
				
				adminBox.setText("Admin Panel");
				
				Button startParseButton = new Button("Parse data", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// TODO: What happens when this button is clicked?
						
					}
				});
				
				adminBox.add(startParseButton);
//				adminBox.add(parsedDataGridDisplay);
				adminBox.center();
				adminBox.setAutoHideEnabled(true);
				adminBox.show();
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

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
	
	private void loadParks() {
		parkService.storeParkList(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
			}

			public void onSuccess(Void ignore) {
				// display parks
				// call parkService.getParkList() to return list of parks
			}
		});
	}
}
