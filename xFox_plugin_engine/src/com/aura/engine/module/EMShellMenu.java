package com.aura.engine.module;

import java.awt.Graphics2D;
import java.util.List;

import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.inputMap.key.EPKeyMapCM;

public class EMShellMenu extends EMShell {
	public EMShellMenu(AuraClient aura, AuraScreen screen, AuraEngine scene) {
		super(aura, screen, scene, EPKeyMapCM.MENU);
	}
	
	@Override
	public void implDoUpdate() {
	}
	@Override
	public void implDoDraw(Graphics2D g) {
		getBtExit().draw(g);
		getBtSpawn().draw(g);
	}
	@Override
	public void implDoDrawDebug(List<String> msg) {
	}
	
	private MObject btSpawn;
	public MObject getBtSpawn() {
		if (btSpawn == null) {
			btSpawn = new MObject(getScene(), "SPAWN", 
				getScene().getScreenInfo().screenWidth / 2 - 50, 
				(getScene().getScreenInfo().screenHeight / 2 - 15) - 40, 
				100, 30);
			btSpawn.attach(new MObjectAction() {
				@Override
				public void execute() {
					getPlugin().requestSpawn(1);
				}
			});
			getObjects().add(btSpawn);
		}
		return btSpawn;
	}
	
	private MObject btExit;
	public MObject getBtExit() {
		if (btExit == null) {
			btExit = new MObject(getScene(), "EXIT", 
				getScene().getScreenInfo().screenWidth / 2 - 50, 
				getScene().getScreenInfo().screenHeight / 2 - 15, 
				100, 30);
			btExit.attach(new MObjectAction() {
				@Override
				public void execute() {
					getScreen().getPlugin().kill(null);
				}
			});
			getObjects().add(btExit);
		}
		return btExit;
	}
}