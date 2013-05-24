package com.aura.engine.module;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import com.aura.base.utils.AuraBaliseParser;
import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.event.EPEventInputKey;
import com.aura.engine.event.EPEventInputMouse;
import com.aura.engine.inputMap.EPMapCE;

public class EMDebugUI extends AuraEngineModule {
	@Override
	public boolean isWorldFocusNeeded() {
		return false;
	}
	
	public EMDebugUI(AuraClient aura, AuraScreen screen, AuraEngine scene) {
		super(aura, screen, scene);
	}

	private String frameInfo;
	
	@Override 
	public void doUpdate() {
		frameInfo = AuraBaliseParser.COLOR.getBalise("orange") + "FPS: " 
			+ AuraBaliseParser.COLOR.getBalise("white") + getScene().getFps() 
			+ AuraBaliseParser.COLOR.getBalise("orange") + " [Target: " 
			+ AuraBaliseParser.COLOR.getBalise("white")+ getScene().getTargetFps() 
			+ AuraBaliseParser.COLOR.getBalise("orange") + "]  PING: " 
			+ AuraBaliseParser.COLOR.getBalise("white") + getAura().getNetworkManager().getClientConnection().getTimePingServer();
		
		if (getScene().isDebug()) {
			getScene().addLog(
				AuraBaliseParser.COLOR.getBalise("orange") + "Free/Delta time: " 
			 	+ AuraBaliseParser.COLOR.getBalise("white") + getScene().getScreenInfo().sleepTime
			 	+ AuraBaliseParser.COLOR.getBalise("orange") + " / "
			 	+ AuraBaliseParser.COLOR.getBalise("white") + getScene().getScreenInfo().deltaTime);
			getScene().addLog(
				AuraBaliseParser.COLOR.getBalise("orange") + "Update/Drawable/Execute time: " 
			 	+ AuraBaliseParser.COLOR.getBalise("white") + getScene().getScreenInfo().updateTime
			 	+ AuraBaliseParser.COLOR.getBalise("orange") + " / "
			 	+ AuraBaliseParser.COLOR.getBalise("white") + getScene().getScreenInfo().drawableTime
			 	+ AuraBaliseParser.COLOR.getBalise("orange") + " / "
			 	+ AuraBaliseParser.COLOR.getBalise("white") + getScene().getScreenInfo().executeDrawTime);
			
			if (getScreen().getPlugin().getUnivers().getFocusWorld() != null)
				getScene().addLog(
					AuraBaliseParser.COLOR.getBalise("orange") + "Entities: " 
					+ AuraBaliseParser.COLOR.getBalise("white") + getScreen().getPlugin().getUnivers().getFocusWorld().getEntitySize());
			
			getScene().addLog(
				AuraBaliseParser.COLOR.getBalise("orange") + "Depth size: " 
				+ AuraBaliseParser.COLOR.getBalise("white") + getScreen().getEngine().getDrawableQueueManager().getQueueList().size());
			getScene().addLog(
				AuraBaliseParser.COLOR.getBalise("orange") + "Drawables: " 
				+ AuraBaliseParser.COLOR.getBalise("white") + getScreen().getEngine().getDrawableQueueManager().getQueueSize()
				+ AuraBaliseParser.COLOR.getBalise("orange") + " [Visible: " 
				+ AuraBaliseParser.COLOR.getBalise("white") + getScreen().getEngine().getDrawableQueueManager().getLastPrintCount()
				+ AuraBaliseParser.COLOR.getBalise("orange") + "]");
			
			if (getScreen().getPlugin().getUnivers().getFocusWorld() != null)
				getScene().addLog(
					AuraBaliseParser.COLOR.getBalise("orange") + "Selection: "
					+ AuraBaliseParser.COLOR.getBalise("white") + getUnivers().getFocusWorld().getDrawableSelectionTab().length);
		}
	}
	
	@Override 
	public void doDraw(Graphics2D g) {
		
		int baseY = 0;
		if (frameInfo != null) {
			// PRINT FRAME INFO
			baseY = 22;
			Composite tempComposite = g.getComposite();
			getScene().setComposite(g, .5f) ;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 250, baseY);
			getScene().setComposite(g, tempComposite);
			AuraEngineModule.drawText(g, frameInfo, 15);
		}
		
		if (getScene().getLog().size() > 0) {
			// PRINT DEBUG MESSAGES
			Composite tempComposite = g.getComposite();
			getScene().setComposite(g, .5f) ;
			g.setColor(Color.BLACK);
			g.fillRect(0, baseY, 250, (getScene().getLog().size() * 11) + 11);
			getScene().setComposite(g, tempComposite);
			
			int p = baseY + 15;
			for (String str: getScene().getLog()) {
				AuraEngineModule.drawText(g, str, p);
				p += 11;
			}
		}
	}
	@Override public void doMousePressed(EPEventInputMouse e, EPMapCE map) {}
	@Override public void doMouseReleased(EPEventInputMouse e, EPMapCE map) {}
	@Override public void doKeyPressed(EPEventInputKey e, EPMapCE map) {}
	@Override public void doKeyReleased(EPEventInputKey e, EPMapCE map) {}
}