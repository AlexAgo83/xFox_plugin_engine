package com.aura.engine.module;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.event.EPEventInputKey;
import com.aura.engine.event.EPEventInputMouse;
import com.aura.engine.inputMap.EPMapCE;

public abstract class EMShell extends AuraEngineModule {
	private final int keyAction;
	
	private boolean visible;
	
	private final List<MObject> objects;
	
	public EMShell(AuraClient aura, AuraScreen screen, AuraEngine scene, int keyAction) {
		super(aura, screen, scene);
		this.keyAction = keyAction;
		this.objects = new ArrayList<MObject>();
	}
	
	public boolean isVisible() {
		return visible;
	}
	public synchronized List<MObject> getObjects() {
		return objects;
	}

	@Override 
	public void doUpdate() {
		if (!isVisible()) return;
		implDoUpdate();
	}
	public abstract void implDoUpdate();
	
	@Override 
	public void doDraw(Graphics2D g) {
		if (!isVisible()) return;
		Composite temp = g.getComposite();
		g.setColor(Color.BLACK);
		getScene().setComposite(g, .85f);
		g.fillRect(0, 0, 
			getScene().getScreenInfo().screenWidth, 
			getScene().getScreenInfo().screenHeight);
		getScene().setComposite(g, temp);
		implDoDraw(g);
	}
	public abstract void implDoDraw(Graphics2D g);
	
	@Override 
	public void doDrawDebug(List<String> msg) {
		if (!isVisible()) return;
		implDoDrawDebug(msg);
	}
	public abstract void implDoDrawDebug(List<String> msg);
	
	@Override 
	public void doKeyReleased(EPEventInputKey e, EPMapCE map) {
		if (map != null && map.getId() == keyAction) {
			if (visible) {
				releaseFocus();
				visible = false;
			} else {
				if (gainFocus()) 
					visible = true;
			}
			return;
		}
	}

	@Override 
	public void doMouseReleased(EPEventInputMouse e, EPMapCE map) {
		if (!isVisible()) return;
		for (MObject mo: getObjects()) {
			if (mo.intersect(
					(int) e.getLocation().x, 
					(int) e.getLocation().y)) {
				mo.actionExecute();
			}
		}
	}
	
	@Override public void doKeyPressed(EPEventInputKey e, EPMapCE map) {}
	@Override public void doMousePressed(EPEventInputMouse e, EPMapCE map) {}
}