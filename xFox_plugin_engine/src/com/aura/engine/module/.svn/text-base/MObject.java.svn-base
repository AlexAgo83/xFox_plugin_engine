package com.aura.engine.module;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.aura.base.utils.Validate;
import com.aura.engine.AuraEngine;

public class MObject {
	private final AuraEngine scene;
	private final String text;
	private final int x, y, w, h;
	
	private final List<MObjectAction> actions;
	
	public MObject(AuraEngine scene, String text, int x, int y, int w, int h) {
		this.scene = scene;
		this.text = text;
		
		this.x = x; this.y = y;
		this.w = w; this.h = h;
		
		this.actions = new ArrayList<MObjectAction>();
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getW() { return w; }
	public int getH() { return h; }
	
	public void draw(Graphics2D g) {
		Composite temp = g.getComposite();
		g.setColor(scene.getUiColor());
		
		scene.setComposite(g, .50f);
		g.fillRect(x, y, w, h);
		scene.setComposite(g, temp);
		
		g.drawRect(x, y, w, h);
		
		if (text != null) {
			g.setColor(Color.BLACK);
			g.drawString(text, 
				x + (w/2) - (g.getFontMetrics().stringWidth(text)/2), 
				y + (h/2) + 5);
		}
	}
	
	public boolean intersect(int xcur, int ycur) {
		return xcur >= x 
				&& xcur <= x+w
				&& ycur >= y
				&& ycur <= y+h;
	}
	
	public void attach(MObjectAction action) {
		Validate.notNull(action);
		actions.add(action);
	}
	public void actionExecute() {
		for (MObjectAction a: actions)
			if (a != null)
				a.execute();
	}
}