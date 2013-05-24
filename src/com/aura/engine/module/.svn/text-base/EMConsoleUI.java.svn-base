package com.aura.engine.module;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;

import com.aura.base.TypeSide;
import com.aura.base.utils.AuraBaliseParser;
import com.aura.base.utils.AuraBaliseParser.ColorText;
import com.aura.base.utils.AuraLogger;
import com.aura.base.utils.CallableAction;
import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.configuration.EPConfigCM;
import com.aura.engine.event.EPEventCM;
import com.aura.engine.event.EPEventInputKey;
import com.aura.engine.event.EPEventInputMouse;
import com.aura.engine.event.EPEventModeSaisie;
import com.aura.engine.inputMap.EPMapCE;
import com.aura.engine.inputMap.key.EPKeyMapCM;

public class EMConsoleUI extends AuraEngineModule {
	private final List<String> console;
	
	private float currentMaxTick;
	private float currentDelta;
	private float currentFade;
	private final Map<Integer, ColorText[]> consoleToDraw;
	
	private Long consoleFade;
	private float consoleFadeMinTick = -1;
	private float consoleFadeMaxTick = -1;
	private boolean markAsDraw;
	
	private String champSaisie;
	private String lastCommit;
	private boolean modeSaisie;
	
	private String colorSystemText;
	private String colorSystemHighlite;
	private String colorSystemDefault;
	
	@Override
	public boolean isWorldFocusNeeded() {
		return false;
	}
	
	public EMConsoleUI(AuraClient a, AuraScreen screen, AuraEngine scene) {
		super(a, screen, scene);
		
		this.consoleToDraw = new HashMap<Integer, ColorText[]>();
		this.champSaisie = "";
		this.modeSaisie = false;
		
		// Init colors
		this.colorSystemText = getAura().getCfgManager().getConfigStringValue(EPConfigCM.CONSOLE_COLOR_SYSTEM);
		this.colorSystemHighlite = getAura().getCfgManager().getConfigStringValue(EPConfigCM.CONSOLE_COLOR_HIGHLITE);
		this.colorSystemDefault = getAura().getCfgManager().getConfigStringValue(EPConfigCM.CONSOLE_COLOR_DEFAULT);
		this.consoleFadeMinTick = getAura().getCfgManager().getConfigIntegerValue(EPConfigCM.CONSOLE_FADE_MIN_TICK);
		this.consoleFadeMaxTick = getAura().getCfgManager().getConfigIntegerValue(EPConfigCM.CONSOLE_FADE_MAX_TICK);
		
		// Init Console
		this.console = new ArrayList<String>();
		this.consoleFade = scene.getCurrentTime();
		
		// Link les logs à la console
		AuraLogger.addAction(new CallableAction() {
			@Override
			public void execute(TypeSide side, Level info, String msg) {
				try {
					if (console.size() > 35) {
						String[] cs = console.toArray(new String[0]);
						for (int i=0; i < console.size()-36; i++) {
							
								console.remove(cs[i]);
							
						}
					}
					console.add((info != null ? 
							(side == TypeSide.SERVER ?  
								AuraBaliseParser.COLOR.getBalise(colorSystemHighlite) + "[" +
								AuraBaliseParser.COLOR.getBalise(colorSystemText) + side + 
								AuraBaliseParser.COLOR.getBalise(colorSystemHighlite) + "] "
								: "") 
						: "") + 
							AuraBaliseParser.COLOR.getBalise(colorSystemDefault) + msg);
					
					consoleFade = getScene().getCurrentTime();
					markAsDraw = true;
				
				} catch (Exception e) {
					// FIXME XF(AA) voir pour régler le problème de liaison concurentiel ici
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void doKeyPressed(EPEventInputKey e, EPMapCE map) {
		if (modeSaisie) {
			if (StringUtils.isAsciiPrintable(""+ e.getKeyChar())) 
				champSaisie += e.getKeyChar();
			else if (e.getCode() == KeyEvent.VK_BACK_SPACE 
					&& champSaisie != null 
					&& champSaisie.length() > 1) 
				champSaisie = champSaisie.substring(0, champSaisie.length()-1);
		}
	}
	
	@Override
	public void doKeyReleased(EPEventInputKey e, EPMapCE map) {
		if (map == null)
			return;
			
		if (map.getId() == EPKeyMapCM.ESCAPE) {
			if (modeSaisie) {
				modeSaisie = false;
				
				EPEventModeSaisie ms = (EPEventModeSaisie) getAura().createEvent(EPEventCM.MODE_SAISIE);
				ms.setModeSaisie(modeSaisie);
				getAura().getEventManager().addEvent(ms);
				
				releaseFocus();
			}
		} else if (map.getId() == EPKeyMapCM.ENTER) {
			if (modeSaisie) {
				if (champSaisie.trim().length() > 0) {
					lastCommit = champSaisie;
					String[] cmd = getAura().getConsoleManager().formatCommand(champSaisie);
					getAura().getConsoleManager().onConsoleCommand(cmd);
				}
				// init fin mode saisie
				champSaisie = "";
			} else {
				// init debut mode saisie
				if (champSaisie.length() == 0)
					champSaisie += "/say ";
			}
			modeSaisie = !modeSaisie;
			consoleFade = getScene().getCurrentTime();
			
			EPEventModeSaisie ms = (EPEventModeSaisie) getAura().createEvent(EPEventCM.MODE_SAISIE);
			ms.setModeSaisie(modeSaisie);
			getAura().getEventManager().addEvent(ms);
			
			if (modeSaisie)
				gainFocus();
			else
				releaseFocus();
		} else if (map.getId() == EPKeyMapCM.UP) {
			if (modeSaisie && lastCommit != null 
					&& lastCommit.trim().length() > 0
					&& getAura().getCfgManager().getConfigBooleanValue(EPConfigCM.CONSOLE_LAST_COMMIT)) {
				champSaisie = lastCommit; 
				consoleFade = getScene().getCurrentTime();
			}
		}
	}
	
	@Override public void doMousePressed(EPEventInputMouse e, EPMapCE map) {}
	@Override public void doMouseReleased(EPEventInputMouse e, EPMapCE map) {}
	
	@Override
	public void doDraw(Graphics2D g) {
		if (currentFade >= .1f) {
			Composite tempComposite = g.getComposite();
			
			if (modeSaisie) {
				g.setColor(Color.BLACK);
				getScene().setComposite(g, .85f);
				g.fillRect(0, 0, 
					getScene().getScreenInfo().frameWidth, 
					getScene().getScreenInfo().frameHeight);
			}

			getScene().setComposite(g, currentFade);
			for (Integer i: consoleToDraw.keySet()) {
				ColorText[] ctx = consoleToDraw.get(i);
				if (ctx != null)
					drawText(g, ctx, i);
			}
			getScene().setComposite(g, tempComposite);
		}
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(champSaisie, 5, getScene().getScreenInfo().frameHeight - 5);
	}
	
	@Override 
	public void doUpdate() {
		currentMaxTick = consoleFadeMaxTick + consoleFadeMinTick;
		currentDelta = getScene().getCurrentTime() - consoleFade;
		if (currentDelta > currentMaxTick) 
			currentDelta = currentMaxTick;
		
		// Soit le delta n'a toujours pas atteint le minimum pour fade, soit le mode saisie est activé
		currentFade = currentDelta <= consoleFadeMinTick || modeSaisie ? 
			1.00f :  // Sinon on fait le ratio du delta
			1 - ((currentDelta - consoleFadeMinTick) / consoleFadeMinTick);
		
		if (currentFade >= .1f && markAsDraw) {
			markAsDraw = false;
			consoleToDraw.clear();
			String[] cs = console.toArray(new String[0]);
			int cpt = 0;
			for (int i=cs.length-1; i >= 0; i--) {
				String frame = cs[i];
				if (frame != null) {
					consoleToDraw.put(
						getScene().getScreenInfo().frameHeight - 20 - (cpt*11), 
						AuraBaliseParser.COLOR.formatText(frame));
					cpt+=1;
				}
			}
		}
		
		if (getScene().isDebug()) {
			getScene().addLog(
				AuraBaliseParser.COLOR.getBalise("orange") + "Mode Saisie: " 
				+ AuraBaliseParser.COLOR.getBalise("white") + modeSaisie);
		}
	}
}