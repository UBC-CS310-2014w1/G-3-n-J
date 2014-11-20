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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FilterPanel extends VerticalPanel {

	private List<ParkFilter> filters = new ArrayList<ParkFilter>();
	private ParkFinder parkFinder;
	//private CheckBox washroomCheckBox = new CheckBox("Washrooms");
	//private CheckBox playgroundCheckBox = new CheckBox("Playgrounds");
	//TODO try to remove neighbourhoodPanel
	private VerticalPanel neighbourhoodPanel = new VerticalPanel();
	private Tree neighbourhoodTree = new Tree();

	private ScrollPanel parkDisplay = new ScrollPanel();

	private Grid parkGrid;
	
	private class FilterCheckBox extends CheckBox{

		public FilterCheckBox (String text, FilterPanel panel, final ParkFilter filter) {
			super(text);
			this.setValue(false);
			final CheckBox fcb = this;
			this.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (fcb.getValue()) {
						fcb.setValue(true);
						filters.add(filter);
						refresh();
					} else {
						fcb.setValue(false);
						for (ParkFilter f: filters){
							if (f.getClass() == filter.getClass()){
								filters.remove(f);
								refresh();
								break;
							}
						}
					}
				}
			});
		}
	}

	public FilterPanel(ParkFinder pf) {
		parkFinder = pf;
		
		ParkFilter washroomFilter = new WashroomFilter();
		FilterCheckBox washroomCheckBox = new FilterCheckBox("Washrooms", this, washroomFilter);
		this.add(washroomCheckBox);
		
		ParkFilter playgroundFilter = new PlaygroundFilter();
		FilterCheckBox playgroundCheckBox = new FilterCheckBox("Playgrounds", this, playgroundFilter);
//
//		playgroundCheckBox.setValue(false);
//		playgroundCheckBox.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				if (playgroundCheckBox.getValue()) {
//					playgroundCheckBox.setValue(true);
//					filters.add(new PlaygroundFilter());
//					refresh();
//				} else {
//					playgroundCheckBox.setValue(false);
//					for (ParkFilter f: filters){
//						if (f.getClass() == PlaygroundFilter.class){
//							filters.remove(f);
//							refresh();
//							break;
//						}
//					}
//				}
//			}
//		});
		this.add(playgroundCheckBox);



			TreeItem neighbourhoods = new TreeItem();
			neighbourhoods.setText("Neighbourhoods");
			
			//ArrayList<String> neighbourhoodNames = getNeighbourhoodNames();
//			
//			for (String nbh : neighbourhoodNames) {
//				CheckBox check = new CheckBox(nbh);
//				neighbourhoods.addItem(check);
//			}
			

						
			
			neighbourhoodTree.addItem(neighbourhoods);
			neighbourhoodPanel.add(neighbourhoodTree);
			//TODO See about remove neighbourhoodPanel
			
			Button searchNeighbourhoodBtn = new Button("Search", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (neighbourhoodPanel.getWidgetCount() > 2) {
						neighbourhoodPanel.remove(2);
						map.clearOverlays();
					}
					
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
					newMapMarker(filteredList);
					CellList<String> filtered = parkCellList(filteredList);
					vPanel.add(filtered);
					}
					
				}
				
			});
			
			vPanel.add(searchNeighbourhoodBtn);
			nbhFilterTab.add(vPanel);
			
//			tabPanel.add(nbhFilterTab, "Neighbourhood Filter");
			
		


//		neighbourhoodTree.addItem(neighbourhoods);
		neighbourhoodPanel.add(neighbourhoodTree);
		this.add(neighbourhoodPanel);

		

		ScrollPanel nbhFilterTab = new ScrollPanel();
		nbhFilterTab.setHeight("700px");
		this.add(parkDisplay);
		//refresh();
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

	private void refresh() {
		parkDisplay.clear();
		parkGrid = parkFinder.displayParkList();
//		Label numParks = new Label("You do not have any favorite park.");
//		TextBox diagno = new TextBox();
//		String text = Integer.toString(filters.size());
//		diagno.setText(text);
//		parkDisplay.add(diagno);
		parkDisplay.add(parkGrid);
	}

	public List<Park> filter(List<Park> parks) {
		for (ParkFilter filter : filters) {
			parks = filter.filter(parks);
		}
		return parks;	
	}

	public List<ParkFilter> getFilters() {
		return filters;
	}

	public void add(ParkFilter filter) {
		filters.add(filter);
	}
	
	

}
