package com.aura.engine.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;

public enum Orientation {
	IDLE(0,true,	 	   0,	  0),
	IDLE_NORD(1,true,	   0,	  0),
	IDLE_OUEST(2,true,	   0,	  0),
	IDLE_EST(3,true,	   0,	  0),
	
	NORD(4,false,		   0,	  1),
	EST(5,false,		   1,	  0),
	SUD(6,false,		   0,	 -1),
	OUEST(7,false,	      -1,	  0),
	
	NORD_EST(8,false,	  .7,	 .7, 	NORD, EST),
	SUD_EST(9,false,	  .7,	-.7, 	SUD, EST),
	SUD_OUEST(10,false,   -.7,	-.7, 	SUD, OUEST),
	NORD_OUEST(11,false,  -.7,	 .7, 	NORD, OUEST);

	private final int id;
	private final boolean idle;
	private final double x, y;
	private Orientation[] c;
	
	private Orientation(int id, boolean idle, double x, double y, Orientation... combinaisons) {
		this.id = id;
		this.idle = idle;
		this.x = x;
		this.y = y;
		this.c = combinaisons;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isIdle() {
		return idle;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public Orientation[] getCombinaisons() {
		return c;
	}
	
	public static Orientation getConbinaison(Orientation a, Orientation b) {
		for (Orientation o: Orientation.values()) {
			if (!o.isIdle() && o.getCombinaisons() != null && o.getCombinaisons().length > 0) {
				if ((o.getCombinaisons()[0] == a && o.getCombinaisons()[1] == b)
					|| (o.getCombinaisons()[0] == b && o.getCombinaisons()[1] == a))
					return o;
			}
		}
		return null;
	}

	public void writeStream(Aura aura, DataOutputStream output) throws IOException {
		output.writeInt(getId());
		output.flush();
	}

	public static Orientation parseStream(DataInputStream input) throws IOException {
		return getById(input.readInt());
	}
	
	public static Orientation lastTest;
	public static Orientation getById(int id) {
		if (lastTest != null && lastTest.getId() == id)
			return lastTest;
		for (Orientation o: Orientation.values()) {
			if (o.getId() == id) {
				lastTest = o;
				return o;
			}
		}
		return null;
	}
	
	public static Orientation getByTarget(double x, double y, double ratio) {
		for (Orientation o: Orientation.values()) {
			if (!o.isIdle() && x/ratio >= o.x-.5 && x/ratio <= o.x+.5 && y/ratio >= o.y-.5 && y/ratio <= o.y+.5)
				return o;
		}
		return Orientation.IDLE;
	}
}