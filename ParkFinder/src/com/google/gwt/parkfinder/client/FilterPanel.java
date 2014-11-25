package com.google.gwt.parkfinder.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.parkfinder.filter.DistanceFilter;
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

	private List<ParkFilter> filters = new LinkedList<ParkFilter>();
	private ParkFinder parkFinder;
	private VerticalPanel neighbourhoodPanel = new VerticalPanel();
	private Tree neighbourhoodTree = new Tree();
	private TreeItem neighbourhoods = new TreeItem();
	private boolean allNeighbourhoodsBool = true;
	private List<String> checkedNeighbourhoodStrings = new LinkedList<String>();


	private ScrollPanel parkDisplay = new ScrollPanel();

	private CellList parkCellList;

	public FilterPanel(ParkFinder pf) {
		parkFinder = pf;

		ParkFilter washroomFilter = new WashroomFilter();
		FilterCheckBox washroomCheckBox = new FilterCheckBox("Washrooms", this, washroomFilter);
		this.add(washroomCheckBox);

		ParkFilter playgroundFilter = new PlaygroundFilter();
		FilterCheckBox playgroundCheckBox = new FilterCheckBox("Playgrounds", this, playgroundFilter);
		this.add(playgroundCheckBox);

		
//		ParkFilter walkingDistanceFilter = new DistanceFilter((float)2.5, parkFinder.getUserLat(), parkFinder.getUserLon());
//		FilterCheckBox walkingDistanceCheckBox = new FilterCheckBox("Walking Distance", this, walkingDistanceFilter);
//		this.add(walkingDistanceCheckBox);

		this.add(neighbourhoodPanel);
		neighbourhoodPanel.add(neighbourhoodTree);
		neighbourhoodTree.addItem(neighbourhoods);
		neighbourhoods.setText("Neighbourhoods");

		final CheckBox allNeighbourhoodsCheckBox = new CheckBox("All");
		allNeighbourhoodsCheckBox.setValue(true);
		allNeighbourhoodsCheckBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (allNeighbourhoodsCheckBox.getValue()) {
					allNeighbourhoodsCheckBox.setValue(true);
					allNeighbourhoodsBool = true;
					for (ParkFilter f: filters){
						if (f.getClass() == NeighbourhoodFilter.class){
							filters.remove(f);
							break;
						}
					}
				} else {
					allNeighbourhoodsCheckBox.setValue(false);
					ParkFilter neighbourhoodFilter = new NeighbourhoodFilter(checkedNeighbourhoodStrings);
					filters.add(neighbourhoodFilter);
					allNeighbourhoodsBool = false;
				}
				refresh();
			}
		});

		neighbourhoods.addItem(allNeighbourhoodsCheckBox);

		List<String> neighbourhoodStrings = Arrays.asList("Downtown","Arbutus Ridge","Dunbar-Southlands",
				"Fairview","Grandview-Woodland","Hastings-Sunrise","Kensington-Cedar Cottage",
				"Kerrisdale","Killarney","Kitsilano","Marpole","Mount Pleasant","Oakridge","Renfrew-Collingwood",
				"Riley-Little Mountain","Shaughnessy","South Cambie","Strathcona","Sunset","Victoria-Fraserview",
				"West End","West Point Grey");
		for (String nbh : neighbourhoodStrings) {
			CheckBox check = new NeighbourhoodCheckBox(nbh);
			neighbourhoods.addItem(check);
		}

		this.add(neighbourhoodPanel);

		ScrollPanel nbhFilterTab = new ScrollPanel();
		nbhFilterTab.setHeight("700px");
		this.add(parkDisplay);
		//		refresh();
	}


	public void refresh() {
		List<Park> freshParks = filter(parkFinder.getParks());
		parkFinder.clearMap();
		parkDisplay.clear();
		parkCellList = parkFinder.parkCellList(freshParks);
		parkDisplay.add(parkCellList);		
		parkFinder.newMapMarker(freshParks);
	}

	public List<Park> filter(List<Park> parks) {
		for (ParkFilter filter : filters) {
			parks = filter.filter(parks);
		}
		return parks;	
	}

	private class FilterCheckBox extends CheckBox {
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

	private class NeighbourhoodCheckBox extends CheckBox {
		private NeighbourhoodCheckBox(final String text){
			super(text);
			final CheckBox fcb = this;
			this.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					for (ParkFilter f: filters){
						if (f.getClass() == NeighbourhoodFilter.class){
							filters.remove(f);
						}
					}

					if (fcb.getValue()) {
						fcb.setValue(true);
						checkedNeighbourhoodStrings.add(text);

					} else {
						fcb.setValue(false);
						for (String str: checkedNeighbourhoodStrings) {
							if (str.equals(text))
								checkedNeighbourhoodStrings.remove(str);
						}

					}
					if (!allNeighbourhoodsBool) {
						ParkFilter neighbourhoodFilter = new NeighbourhoodFilter(checkedNeighbourhoodStrings);
						filters.add(neighbourhoodFilter);
						refresh();
					}
				}
			});
		}
	}
}
