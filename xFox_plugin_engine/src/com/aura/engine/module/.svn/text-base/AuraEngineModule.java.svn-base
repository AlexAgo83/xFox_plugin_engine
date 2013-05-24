package com.aura.engine.module;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import com.aura.base.utils.AuraBaliseParser;
import com.aura.base.utils.AuraBaliseParser.ColorText;
import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.event.EPEventInputKey;
import com.aura.engine.event.EPEventInputMouse;
import com.aura.engine.inputMap.EPMapCE;
import com.aura.engine.plugin.EngineClientPlugin;
import com.aura.engine.univers.UniversClient;
import com.aura.engine.univers.drawable.DrawableQueueManager;

public abstract class AuraEngineModule {
	private static long ID_TRACKER;
	public long GENERER_ID() {
		long id = ID_TRACKER;
		ID_TRACKER += 1;
		return id;
	}
	
	private final long id;
	private final AuraClient aura;
	private final AuraScreen screen;
	private final AuraEngine scene;
	
	public AuraEngineModule(AuraClient aura, AuraScreen screen, AuraEngine scene) {
		this.id = GENERER_ID();
		this.aura = aura;
		this.screen = screen;
		this.scene = scene;
	}
	
	public long getId() {
		return id;
	}
	
	public AuraClient getAura() {
		return aura;
	}
	public AuraScreen getScreen() {
		return screen;
	}
	public EngineClientPlugin getPlugin() {
		return getScreen().getPlugin();
	}
	public UniversClient getUnivers() {
		return getPlugin().getUnivers();
	}
	public AuraEngine getScene() {
		return scene;
	}
	public DrawableQueueManager getDrawableQueueManager() {
		return getScene().getDrawableQueueManager();
	}
	
	
	public boolean gainFocus() {
		if (getScene().getModuleFocus() == null) {
			getScene().setModuleFocus(this);
			return true;
		}
		return false;
	}
	public void releaseFocus() {
		if (getScene().getModuleFocus() != null && getScene().getModuleFocus().equals(this))
			getScene().setModuleFocus(null);
	}
	
	public abstract void doDraw(Graphics2D g);
	public abstract void doDrawDebug(List<String> msg);
	public abstract void doUpdate();
	
	public abstract void doMousePressed(EPEventInputMouse e, EPMapCE map);
	public abstract void doMouseReleased(EPEventInputMouse e, EPMapCE map);
	
	public abstract void doKeyPressed(EPEventInputKey e, EPMapCE map);
	public abstract void doKeyReleased(EPEventInputKey e, EPMapCE map);
	
	public static void drawText(Graphics2D g, String frame, int y) {
		// Formatage du text en couleur avec balises
		{
			g.setColor(Color.WHITE);
			ColorText[] cts = AuraBaliseParser.COLOR.formatText(frame);
			String lastPrint = "";
			int len = 5;
			// Draw du tableau de ColorText
			for (ColorText ct: cts) {
				if (ct.color != null)
					g.setColor(ct.color);
				len = len + g.getFontMetrics().stringWidth(lastPrint);
				g.drawString(ct.text, len, y);
				lastPrint = ct.text;
			}
		}
	}
	
	public static void drawText(Graphics2D g, ColorText[] txt, int y) {
		// Formatage du text en couleur avec balises
		{
			g.setColor(Color.WHITE);
			ColorText[] cts = txt;
			String lastPrint = "";
			int len = 5;
			// Draw du tableau de ColorText
			for (ColorText ct: cts) {
				if (ct.color != null)
					g.setColor(ct.color);
				len = len + g.getFontMetrics().stringWidth(lastPrint);
				g.drawString(ct.text, len, y);
				lastPrint = ct.text;
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuraEngineModule other = (AuraEngineModule) obj;
		if (id != other.id)
			return false;
		return true;
	}
}