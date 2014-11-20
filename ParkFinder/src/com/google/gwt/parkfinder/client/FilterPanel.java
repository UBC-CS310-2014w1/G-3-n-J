package com.google.gwt.parkfinder.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.parkfinder.filter.NeighbourhoodFilter;
import com.google.gwt.parkfinder.filter.ParkFilter;
import com.google.gwt.parkfinder.filter.PlaygroundFilter;
import com.google.gwt.parkfinder.filter.WashroomFilter;
import com.google.gwt.parkfinder.server.Park;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FilterPanel extends VerticalPanel {

	private List<ParkFilter> filters = new ArrayList<ParkFilter>();
	private ParkFinder parkFinder;
	private CheckBox washroomCheckBox = new CheckBox("Washrooms");
	private CheckBox playgroundCheckBox = new CheckBox("Playgrounds");

	private VerticalPanel neighbourhoodPanel = new VerticalPanel();
	private Tree neighbourhoodTree = new Tree();

	private VerticalPanel parkDisplay = new VerticalPanel();
	private Grid parkGrid;

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

		playgroundCheckBox.setValue(false);
		playgroundCheckBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (playgroundCheckBox.getValue()) {
					//					washroomCheckBox.setValue(false);
					for (ParkFilter f: filters){
						if (f.getClass() == PlaygroundFilter.class){
							filters.remove(f);
						}
					}
				} else {
					//					washroomCheckBox.setValue(true);
					filters.add(new PlaygroundFilter());
				}
				//				pf.refreshParks();
			}
		});
		this.add(playgroundCheckBox);






		TreeItem neighbourhoods = new TreeItem();
		neighbourhoods.setText("Neighbourhoods:");

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
		neighbourhoodPanel.add(neighbourhoodTree);

		Button searchNeighbourhoodBtn = new Button("Search", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (neighbourhoodPanel.getWidgetCount() > 2)
					neighbourhoodPanel.remove(2);

				TreeItem neighbourhoodList = neighbourhoodTree.getItem(0);
				int numNeighbourhoods = neighbourhoodList.getChildCount();
				List<String> chosenNBH = new LinkedList<String>();
				for (int i = 0; i < numNeighbourhoods; i++) {
					CheckBox box = (CheckBox) neighbourhoodList.getChild(i).getWidget();
					if (box.getValue())
						chosenNBH.add(neighbourhoodList.getChild(i).getText());
				}

				NeighbourhoodFilter nbhFilter = new NeighbourhoodFilter(chosenNBH);
				//				if (parkList != null) {
				//					List<Park> filteredList = nbhFilter.filter(parkList);
				//					CellList<String> filtered = parkCellList(filteredList);
				//					neighbourhoodPanel.add(filtered);
				//				}

				display();

			}

		});

		neighbourhoodPanel.add(searchNeighbourhoodBtn);
		this.add(neighbourhoodPanel);

		this.add(parkDisplay);
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

	private void display() {
		parkDisplay.clear();
		parkGrid = parkFinder.displayParkList();
		parkDisplay.add(parkGrid);
	}

	public List<Park> filter(List<Park> parks) {
		for (ParkFilter filter : filters) {
			filter.filter(parks);
		}
		return parks;	
	}

	public List<ParkFilter> getFilters() {
		return filters;
	}

}
