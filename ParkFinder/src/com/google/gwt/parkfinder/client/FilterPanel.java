package com.google.gwt.parkfinder.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.parkfinder.filter.ParkFilter;
import com.google.gwt.parkfinder.filter.WashroomFilter;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FilterPanel extends VerticalPanel {
	
	private List<ParkFilter> filters = new ArrayList<ParkFilter>();
	private ParkFinder parkFinder;
	private CheckBox washroomCheckBox = new CheckBox("Washrooms");

	
	public FilterPanel(ParkFinder pf) {
		parkFinder = pf;
		
		washroomCheckBox.setValue(false);
		washroomCheckBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (washroomCheckBox.getValue()) {
//					washroomCheckBox.setValue(false);
					for (ParkFilter f: filters){
						if (f.getClass() == WashroomFilter.class){
							filters.remove(f);
						}
					}
				} else {
//					washroomCheckBox.setValue(true);
					filters.add(new WashroomFilter());
				}
//				pf.refreshParks();
			}
		});
		this.add(washroomCheckBox);
	}
////  TODO DISTANCE FILTER
//		HorizontalPanel distancePanel = new HorizontalPanel();
//		TextBox distanceTextField = new TextBox();
//		Button distanceButton = new Button("Distance", new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				//map.getlatlong
//				// new DistanceFilter based on distance input and map coords
//			}
//		});
//		distancePanel.add(distanceTextField);
//		distancePanel.add(distanceButton);
//		filterPanel.add(distancePanel);
//		
//		// TODO NEIGHBOURHOOD FILTER
//		Button neighbourhoodButton = new Button("Neighbourhood", new ClickHandler() {
//			public void onClick(ClickEvent event) {
//			//	filterPanel.remove(neighbourhoodButton);
//			//	filterPanel.add(neighbourhoodPanel);	
//
//				
//			}
//		});
//		VerticalPanel neighbourhoodPanel = new VerticalPanel();
//		// checkboxes go in neighbourhoodPanel
//		
//		filterPanel.add(neighbourhoodButton);
//	}
//
//	private void refreshParks() {
//		List<Park> displayParks = new ArrayList<Park>();
//		// somehow use parkService.getParkList() to initialize displayParks
//		for (ParkFilter filter: filters) {
//			filter.filter(displayParks);
//		}
//	}
//	
	
	public List<ParkFilter> getFilters() {
		return filters;
	}
	
}
