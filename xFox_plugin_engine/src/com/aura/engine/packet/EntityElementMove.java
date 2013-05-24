package com.aura.engine.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.engine.utils.Location;

public abstract class EntityElementMove extends EntityElement {
	private String name;
	private Location location;
	private Double speed;
	
	public EntityElementMove() {
		super();
		this.name = "";
		this.location = new Location(0, 0);
		this.speed = new Double(1);
	}
	
	public abstract TypeEntityElementMove getMoveElementType();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location loc) {
		this.location.x = Math.round(loc.x);
		this.location.y = Math.round(loc.y);
	}
	public void setLocation(int x, int y) {
		this.location.x = Math.round(x);
		this.location.y = Math.round(y);
	}
	
	public Double getSpeed() {
		return speed;
	}
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	
	@Override
	public String complementWrite(Aura aura, DataOutputStream output)
			throws IOException {
		return "#" + getName()
			 + "#" + getSpeed() 
			 + "#" + (int) getLocation().x
			 + "#" + (int) getLocation().y;
	}
	
	@Override
	public void complementRead(Aura aura, String[] split)
			throws IOException {
		setName(split[3]);
		setSpeed(Double.parseDouble(split[4]));
		setLocation(Integer.parseInt(split[5]), Integer.parseInt(split[6]));
	}
	
	@Override
	public boolean complementValid() {
		return getName() != null
			&& getLocation() != null
			&& getSpeed() != null;
	}
}