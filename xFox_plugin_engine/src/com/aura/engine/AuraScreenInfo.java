package com.aura.engine;

import com.aura.engine.utils.Location;

public class AuraScreenInfo {
	public Location currentCurseur = new Location();
	public Location currentOrigine = new Location();
	public int screenWidth;
	public int screenHeight;
	public long currentTime;
	public long deltaTime;
	public long sleepTime;
	
	public long updateTime;
	public long drawableTime;
	public long executeDrawTime;
	
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