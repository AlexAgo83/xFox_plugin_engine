package com.aura.engine;

import java.awt.Dimension;
import java.awt.DisplayMode;

import com.aura.base.configuration.PropertiesLoader.PropertiesLoaderException;
import com.aura.client.AuraClient;
import com.aura.engine.configuration.EPConfigCM;

public class AuraDisplayMode {
	private final AuraClient aura;
	
	public AuraDisplayMode(AuraClient aura) {
		this.aura = aura;
	}
	
	public int getWidth() {
		return aura.getCfgManager().getConfigIntegerValue(EPConfigCM.DISPLAY_WIDTH);
	}
	public int getHeight() {
		return aura.getCfgManager().getConfigIntegerValue(EPConfigCM.DISPLAY_HEIGHT);
	}
	public boolean isWindowed() {
		return aura.getCfgManager().getConfigBooleanValue(EPConfigCM.DISPLAY_WINDOWED);
	}
	public int getDesiredRate() {
		return isWindowed() ?
			aura.getCfgManager().getConfigIntegerValue(EPConfigCM.DISPLAY_RATE) :
			DisplayMode.REFRESH_RATE_UNKNOWN;
	}
	
	private Dimension dimension;
	public Dimension getDimension() {
		if (dimension == null) {
			dimension = new Dimension(getWidth(), getHeight());
		}
		return dimension;
	}
	
	private DisplayMode displayMode;
	public DisplayMode getDisplayMode() throws PropertiesLoaderException {
		if (displayMode == null) {
			displayMode = new DisplayMode(
					getWidth(), getHeight(), 
					aura.getCfgManager().getConfigIntegerValue(EPConfigCM.DISPLAY_DEPTH), 
					getDesiredRate());
		}
		return displayMode;
	}
}