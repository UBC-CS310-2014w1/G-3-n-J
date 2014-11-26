package com.google.gwt.parkfinder.client;


import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.parkfinder.filter.BaseballFilter;
import com.google.gwt.parkfinder.filter.BasketballFilter;
import com.google.gwt.parkfinder.filter.BeachFilter;
import com.google.gwt.parkfinder.filter.CommunityFilter;
import com.google.gwt.parkfinder.filter.DistanceFilter;
import com.google.gwt.parkfinder.filter.DogFilter;
import com.google.gwt.parkfinder.filter.FoodFilter;
import com.google.gwt.parkfinder.filter.NeighbourhoodFilter;
import com.google.gwt.parkfinder.filter.ParkFilter;
import com.google.gwt.parkfinder.filter.PicnicFilter;
import com.google.gwt.parkfinder.filter.PlaygroundFilter;
import com.google.gwt.parkfinder.filter.SkateboardFilter;
import com.google.gwt.parkfinder.filter.SoccerFilter;
import com.google.gwt.parkfinder.filter.SprayParkFilter;
import com.google.gwt.parkfinder.filter.TennisFilter;
import com.google.gwt.parkfinder.filter.TrailFilter;
import com.google.gwt.parkfinder.filter.WashroomFilter;
import com.google.gwt.parkfinder.server.Park;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FilterPanel extends VerticalPanel {

	private List<ParkFilter> filters = new LinkedList<ParkFilter>();
	private ParkFinder parkFinder;
	private VerticalPanel neighbourhoodPanel = new VerticalPanel();
	private Tree neighbourhoodTree = new Tree();
	private TreeItem neighbourhoods = new TreeItem();
	private CheckBox allNeighbourhoodsCheckBox = new CheckBox("All");
	private boolean allNeighbourhoodsBool = true;
	private HashMap<String, String> checkedNeighbourhoodStrings = new HashMap<String, String>();
	private List<NeighbourhoodCheckBox> checkedNeighbourhoodBoxes = new LinkedList<NeighbourhoodCheckBox>();


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
		
		ParkFilter tennisFilter = new TennisFilter();
		FilterCheckBox tennisCheckBox = new FilterCheckBox("Tennis Courts", this, tennisFilter);
		this.add(tennisCheckBox);
		
		ParkFilter baseballFilter = new BaseballFilter();
		FilterCheckBox baseballCheckBox = new FilterCheckBox("Baseball/Softball Diamonds", this, baseballFilter);
		this.add(baseballCheckBox);
		
		ParkFilter basketballFilter = new BasketballFilter();
		FilterCheckBox basketballCheckBox = new FilterCheckBox("Basketball Courts", this, basketballFilter);
		this.add(basketballCheckBox);
		
		ParkFilter beachFilter = new BeachFilter();
		FilterCheckBox beachCheckBox = new FilterCheckBox("Beaches", this, beachFilter);
		this.add(beachCheckBox);
		
		ParkFilter dogFilter = new DogFilter();
		FilterCheckBox dogCheckBox = new FilterCheckBox("Off Leash Dog Areas", this, dogFilter);
		this.add(dogCheckBox);
		
		ParkFilter skateFilter = new SkateboardFilter();
		FilterCheckBox skateCheckBox = new FilterCheckBox("Skateboard Parks", this, skateFilter);
		this.add(skateCheckBox);

		ParkFilter soccerFilter = new SoccerFilter();
		FilterCheckBox soccerCheckBox = new FilterCheckBox("Soccer Fields", this, soccerFilter);
		this.add(soccerCheckBox);

		ParkFilter sprayFilter = new SprayParkFilter();
		FilterCheckBox sprayCheckBox = new FilterCheckBox("Water/Spray Parks", this, sprayFilter);
		this.add(sprayCheckBox);
		
		ParkFilter trailFilter = new TrailFilter();
		FilterCheckBox trailCheckBox = new FilterCheckBox("Jogging Trails", this, trailFilter);
		this.add(trailCheckBox);
		
		ParkFilter foodFilter = new FoodFilter();
		FilterCheckBox foodCheckBox = new FilterCheckBox("Food Available", this, foodFilter);
		this.add(foodCheckBox);

		ParkFilter picnicFilter = new PicnicFilter();
		FilterCheckBox picnicCheckBox = new FilterCheckBox("Picnic Sites", this, picnicFilter);
		this.add(picnicCheckBox);
		
		ParkFilter communityFilter = new CommunityFilter();
		FilterCheckBox communityCheckBox = new FilterCheckBox("Community Centre", this, communityFilter);
		this.add(communityCheckBox);

//		ParkFilter walkingDistanceFilter = new DistanceFilter((float)2.5, parkFinder.getUserLat(), parkFinder.getUserLon());
//		FilterCheckBox walkingDistanceCheckBox = new FilterCheckBox("Walking Distance", this, walkingDistanceFilter);
//		this.add(walkingDistanceCheckBox);

		this.add(neighbourhoodPanel);
		neighbourhoodPanel.add(neighbourhoodTree);
		neighbourhoodTree.addItem(neighbourhoods);
		neighbourhoods.setText("Neighbourhoods");


		allNeighbourhoodsCheckBox.setValue(true);
		allNeighbourhoodsCheckBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (allNeighbourhoodsCheckBox.getValue()) {
					allNeighbourhoodsCheckBox.setValue(true);
					allNeighbourhoodsBool = true;
					for (ParkFilter f: filters){
						if (f.getClass() == NeighbourhoodFilter.class){
							filters.remove(f);
							//uncheck all individual neighbouhood boxes
							checkedNeighbourhoodStrings = new HashMap<String, String>();
							for (NeighbourhoodCheckBox box: checkedNeighbourhoodBoxes){
								box.setValue(false);
							}
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

		List<String> neighbourhoodStrings = new LinkedList<String>(Arrays.asList("Downtown","Arbutus Ridge","Dunbar-Southlands",
				"Fairview","Grandview-Woodland","Hastings-Sunrise","Kensington-Cedar Cottage",
				"Kerrisdale","Killarney","Kitsilano","Marpole","Mount Pleasant","Oakridge","Renfrew-Collingwood",
				"Riley-Little Mountain","Shaughnessy","South Cambie","Strathcona","Sunset","Victoria-Fraserview",
				"West End","West Point Grey"));
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
		parkCellList = parkFinder.parkCellList(freshParks, true);
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
			checkedNeighbourhoodBoxes.add(this);
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
						checkedNeighbourhoodStrings.put(text, text);
						allNeighbourhoodsBool = false;
						allNeighbourhoodsCheckBox.setValue(false);
					} else {
						fcb.setValue(false);
						checkedNeighbourhoodStrings.remove(text);
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
