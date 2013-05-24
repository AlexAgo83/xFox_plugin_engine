package com.aura.engine.module.shell;

import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.util.Map;

import com.aura.base.event.AbstractEvent;
import com.aura.base.event.AuraEventListener;
import com.aura.base.utils.Validate;
import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.event.EPEventCM;
import com.aura.engine.event.EPEventInputKey;
import com.aura.engine.event.EPEventInputMouse;
import com.aura.engine.event.EPEventLaunchShell;
import com.aura.engine.inputMap.EPMapCE;
import com.aura.engine.inputMap.mouse.EPMouseMapCM;
import com.aura.engine.module.AuraEngineModule;
import com.aura.engine.module.object.EMObject;
import com.aura.engine.module.object.EMObjectClickable;

public abstract class AbstractShell extends AuraEngineModule {
	private final TypeShell type;
	private final int keyAction;
	
	private boolean visible;
	private boolean pinned;
	
	private final Map<GridShell, EMObject> objects;
	private final EMObject[][] objectTab;
	private final int maxXCell, maxYCell;
	
	private int maxXDecal, maxYDecal;
	
	public AbstractShell(AuraClient aura, AuraScreen screen, AuraEngine scene, TypeShell type, int keyAction) {
		super(aura, screen, scene);
		this.type = type;
		this.keyAction = keyAction;
		this.objects = new HashMap<GridShell, EMObject>();
		this.pinned = false;
		
		initialize();
		
		int minX = 0;
		int minY = 0;
		for (final GridBagConstraints gbc: getObjects().keySet()) {
			if (gbc != null) {
				if (gbc.gridx > minX) 
					minX = gbc.gridx;
				if (gbc.gridy > minY) 
					minY = gbc.gridy;
			}
		}
		
		maxXCell = minX+1;
		maxYCell = minY+1;
		
		objectTab = new EMObject[maxXCell][maxYCell];
		for (final GridBagConstraints gbc: getObjects().keySet()) {
			if (gbc != null && (gbc.gridx >= 0 && gbc.gridy >= 0))
				objectTab[gbc.gridx][gbc.gridy] = getObjects().get(gbc);
		}
		
		aura.getEventManager().getDispatchThread().addListener(
			new AuraEventListener(getAura().getEvent(EPEventCM.LAUNCH_SHELL)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				EPEventLaunchShell e = (EPEventLaunchShell) event;
				if (e.getShell() == getType())
					show(true);
			}
		});
	}
	
	public TypeShell getType() {
		return type;
	}
	
	public abstract int getX();
	public abstract int getY();
	public abstract int getWidth();
	public abstract int getHeight();
	
	public boolean isVisible() {
		return visible;
	}
	public boolean isPinned() {
		return pinned;
	}
	protected void setPinned(boolean pinned) {
		this.pinned = pinned;
	}
	
	private synchronized Map<GridShell, EMObject> getObjects() {
		return objects;
	}
	public void attach(GridShell gbc, EMObject object) {
		Validate.notNull(gbc);
		Validate.notNull(object);
		getObjects().put(gbc, object);
	}

	public abstract void initialize();
	
	public void updateObjects() {
		maxXDecal = (getWidth()) / maxXCell; 
		maxYDecal = (getHeight() - getHeaderYDecal()) / maxYCell;

		for (int y=0; y<maxYCell;y++) {
			for (int x=0; x<maxXCell;x++) {
				EMObject o = objectTab[x][y];
				if (o != null && o.isVisible()) 
					o.update(
						getX() + (x * maxXDecal) + (maxXDecal / 2) - (o.getW() / 2), 
						getY() + (y * maxYDecal) + (maxYDecal / 2) - (o.getH() / 2) + getHeaderYDecal()
					);
			}
		}
	}
	public abstract void implDoUpdate();
	
	public abstract int getHeaderYDecal();

	public abstract void implDoDraw(Graphics2D g);
	public abstract void implDoDrawDebug();
	
	public void drawObjects(Graphics2D g) {
		for (GridShell gbc: getObjects().keySet()) {
			EMObject o = getObjects().get(gbc);
			if (o.isVisible()) {
				o.draw(g, isPinned() && !gbc.isAllowClickOnPin());
			}
		}
	}
	
	@Override 
	public void doKeyReleased(EPEventInputKey e, EPMapCE map) {
		if (map != null && map.getId() == keyAction) {
			show(!visible);
			return;
		}
	}
	
	public void show(boolean lShow) {
		if (!lShow) {
			visible = false;
			pinned = false;
			releaseFocus();
		} else {
			pinned = false;
			if (gainFocus()) {
				visible = true;
			}
		}
	}
	public void pin(boolean pin) {
		if (pin) {
			pinned = true;
			releaseFocus();
		} else {
			if (gainFocus())
				pinned = false;
		}
	}

	private EMObjectClickable focusClickable;
	public EMObjectClickable getFocusClickable() {
		return focusClickable;
	}
	
	@Override 
	public void doMousePressed(EPEventInputMouse e, EPMapCE map) {
		if (!isVisible() && !isPinned()) return;
		if (map != null && map.getId() == EPMouseMapCM.SELECT) {
			EMObjectClickable temp = null;
			for (GridShell gbc: getObjects().keySet()) {
				boolean useCase = isPinned() && !gbc.isAllowClickOnPin();
				if (!useCase) {
					EMObject mo = getObjects().get(gbc);
					if (mo instanceof EMObjectClickable) {
						EMObjectClickable clickable = (EMObjectClickable) mo;
						if (clickable != null) {
							if (clickable.intersect(
									(int) e.getLocation().x, 
									(int) e.getLocation().y)) {
								temp = clickable;
								break;
							}
						}
					}
				}
			}
			focusClickable = temp;
		}
	}
	
	@Override 
	public void doMouseReleased(EPEventInputMouse e, EPMapCE map) {
		if (!isVisible() && !isPinned()) return;
		if (focusClickable != null && map != null && map.getId() == EPMouseMapCM.SELECT) {
			for (GridShell gbc: getObjects().keySet()) {
				boolean useCase = isPinned() && !gbc.isAllowClickOnPin();
				EMObject mo = getObjects().get(gbc);
				if (!useCase && focusClickable.equals(mo)) {
					EMObjectClickable clickable = (EMObjectClickable) mo;
					if (clickable.intersect(
								(int) e.getLocation().x, 
								(int) e.getLocation().y)) { 
						clickable.actionExecute();
					}
					break;
				}
			}
		}
		focusClickable = null;
	}
	
	@Override 
	public void doKeyPressed(EPEventInputKey e, EPMapCE map) {
		if (!isVisible() && !isPinned()) return;
	}
}