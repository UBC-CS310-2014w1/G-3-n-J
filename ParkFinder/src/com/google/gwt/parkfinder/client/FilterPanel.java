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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FilterPanel extends VerticalPanel {

	private List<ParkFilter> filters = new ArrayList<ParkFilter>();
	private ParkFinder parkFinder;
	//private CheckBox washroomCheckBox = new CheckBox("Washrooms");
	//private CheckBox playgroundCheckBox = new CheckBox("Playgrounds");

	private VerticalPanel neighbourhoodPanel = new VerticalPanel();
	private Tree neighbourhoodTree = new Tree();

	private VerticalPanel parkDisplay = new VerticalPanel();
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
//
//		washroomCheckBox.setValue(false);
//		washroomCheckBox.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				if (washroomCheckBox.getValue()) {
//					washroomCheckBox.setValue(true);
//					filters.add(new WashroomFilter());
//					refresh();
//				} else {
//					washroomCheckBox.setValue(false);
//					for (ParkFilter f: filters){
//						if (f.getClass() == WashroomFilter.class){
//							filters.remove(f);
//							refresh();
//							break;
//						}
//					}
//				}
//			}
//		});
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
		neighbourhoods.setText("Neighbourhoods:");
		CheckBox allNeighbourhoods = new CheckBox("All");
		neighbourhoods.addItem(allNeighbourhoods);
		//ParkFilter downtownNeighbourhoodFilter = new NeighbourhoodFilter("Downtown");
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
		this.add(neighbourhoodPanel);

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
