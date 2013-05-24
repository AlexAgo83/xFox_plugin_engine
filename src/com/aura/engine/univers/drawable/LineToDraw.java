package com.aura.engine.univers.drawable;

import java.awt.Graphics2D;

import com.aura.base.utils.Validate;
import com.aura.engine.utils.Location;

public class LineToDraw {
	private final Location a;
	private final Location b;
	
	public LineToDraw(Location a, Location b) {
		Validate.notNull(a);
		Validate.notNull(b);
		
		this.a = a;
		this.b = b;
	}
	
	public Location getA() {
		return a;
	}
	public Location getB() {
		return b;
	}
	
	public void draw(Graphics2D g) {
		g.drawLine((int) getA().x, (int) getA().y, (int) getB().x, (int) getB().y);
		g.fillRect((int) getB().x-2, (int) getB().y-2, 4, 4);
	}
}