package com.google.gwt.parkfinder.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.parkfinder.server.ParkParser;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define onModuleLoad()
 */
public class ParkFinder implements EntryPoint {

	VerticalPanel mainPanel = new VerticalPanel();
	TextBox errorMessage = new TextBox();
	Button startParseButton = new Button("Start Parsing");

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		mainPanel.add(errorMessage);
		mainPanel.add(startParseButton);
		errorMessage.setText("No errors so far");

		RootPanel.get("parser").add(mainPanel);

		startParseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				try {
					ParkParser.parse();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					errorMessage.setText("Parser error");
					e.printStackTrace();
				}
			}
		});

	}
}