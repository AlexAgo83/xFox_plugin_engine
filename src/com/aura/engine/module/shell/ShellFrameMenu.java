package com.aura.engine.module.shell;

import java.awt.Graphics2D;

import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.event.EPEventCM;
import com.aura.engine.event.EPEventLaunchShell;
import com.aura.engine.inputMap.key.EPKeyMapCM;
import com.aura.engine.module.object.EMObjectAction;
import com.aura.engine.module.object.EMObjectClickable;

public class ShellFrameMenu extends AbstractShellFrame {
	
	@Override
	public boolean isWorldFocusNeeded() {
		return true;
	}
	
	public ShellFrameMenu(AuraClient aura, AuraScreen screen, AuraEngine scene) {
		super(aura, screen, scene, TypeShell.FRAME_MENU, EPKeyMapCM.ESCAPE);
	}
	
	@Override
	public void initialize() {
		attach(GridShell.createGbc(0, 0, false), getBtTest());
		attach(GridShell.createGbc(0, 1, false), getBtExit());
	}

	@Override public void implDoDraw(Graphics2D g) {}
	@Override public void implDoUpdate() {}
	@Override public void implDoDrawDebug() {}
	
	private EMObjectClickable btTest;
	public EMObjectClickable getBtTest() {
		if (btTest == null) {
			btTest = new EMObjectClickable(this, "TEST", 100, 30);
			btTest.attach(new EMObjectAction() {
				@Override
				public void execute() {
					show(false);
					EPEventLaunchShell e = (EPEventLaunchShell) getAura().createEvent(EPEventCM.LAUNCH_SHELL);
					e.setShell(TypeShell.DIALOG_TEST);
					getAura().getEventManager().addEvent(e);
				}
			});
		}
		return btTest;
	}
	
	private EMObjectClickable btExit;
	public EMObjectClickable getBtExit() {
		if (btExit == null) {
			btExit = new EMObjectClickable(this, "EXIT", 100, 30);
			btExit.attach(new EMObjectAction() {
				@Override
				public void execute() {
					getScreen().getPlugin().kill(null);
				}
			});
		}
		return btExit;
	}
}