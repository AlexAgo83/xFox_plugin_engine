package com.aura.engine;

import java.util.ArrayList;
import java.util.List;

import com.aura.engine.utils.Location;

public class AuraScreenInfo {
	public EngineZoom zoom;
	private final List<String> debugMessages = new ArrayList<String>();
	
	public int frameWidth;
	public int frameHeight;
	
	public int screenOrigineXZoom;
	public int screenOrigineYZoom;
	
	public int screenWidthZoom;
	public int screenHeightZoom;
	
	public Location currentCurseur = new Location();
	public Location currentOrigine = new Location();
	
	public long currentTime;
	public long deltaTime;
	public long sleepTime;
	
	public long updateTime;
	public long drawableTime;
	public long executeDrawTime;
	
	public List<String> getDebugMessages() {
		return debugMessages;
	}
	public void clearMessages() {
		getDebugMessages().clear();
	}
	
	public void traceUpdateTime() {
		updateTime = System.currentTimeMillis() - currentTime;
	}
	public void traceDrawableTime() {
		drawableTime = System.currentTimeMillis() - currentTime - updateTime;
	}
	public void traceExecuteDrawTime() {
		executeDrawTime = System.currentTimeMillis() - currentTime - updateTime;
	}
}