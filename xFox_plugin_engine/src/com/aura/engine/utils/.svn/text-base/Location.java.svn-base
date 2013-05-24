package com.aura.engine.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;

public class Location {
	public double x, y;
	
	public Location() {
		this(0, 0);
	}
	public Location(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	
	public double distance(Location loc) {
		double px = loc.x - this.x;
		double py = loc.y - this.y;
        return Math.sqrt(px * px + py * py);
	}
	
	private Location tempLocation;
	public Location sub(Location location) {
		if (tempLocation == null)
			tempLocation = new Location(0, 0);
		tempLocation.x = x-location.x;
		tempLocation.y = y-location.y;
		return tempLocation;
	}
	
	public Location add(Location location) {
		if (tempLocation == null)
			tempLocation = new Location(0, 0);
		tempLocation.x = x+location.x;
		tempLocation.y = y+location.y;
		return tempLocation;
	}
	
	public void write(Aura aura, DataOutputStream output) throws IOException {
		output.writeUTF(x+"#"+y);
		output.flush();
	}

	public void read(DataInputStream input) throws IOException {
		String[] split =  input.readUTF().split("#");
		x = Double.parseDouble(split[0]);
		y = Double.parseDouble(split[1]);
	}
	
	@Override
	public String toString() {
		return super.toString() + " : [" +x+", "+y+"]"; 
	}
}