package com.aura.engine.event;

import com.aura.engine.utils.Location;

public class EPEventInputMouse extends EPEventInput {
	private Location loc;
	private Location coordinate;
	
	public EPEventInputMouse(int id) {
		super(id);
	}
	
	public Location getLocation() {
		return loc;
	}
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	public Location getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Location coordinate) {
		this.coordinate = coordinate;
	}
}