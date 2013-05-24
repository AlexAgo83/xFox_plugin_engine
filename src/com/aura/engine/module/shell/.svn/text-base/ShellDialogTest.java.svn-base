package com.aura.engine.module.shell;

import java.awt.Graphics2D;

import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.module.object.EMObjectAction;
import com.aura.engine.module.object.EMObjectClickable;

public class ShellDialogTest extends AbstractShellDialog {
	
	@Override
	public boolean isWorldFocusNeeded() {
		return true;
	}
	
	public ShellDialogTest(AuraClient aura, AuraScreen screen, AuraEngine scene) {
		super(aura, screen, scene, TypeShell.DIALOG_TEST, -1, "TEST");
	}
	
	@Override
	public void initializeForDialog() {
		GridShell gbcSpawn = GridShell.createGbc(0, 0, false);
		gbcSpawn.setAllowClickOnPin(true);
		attach(gbcSpawn, getBtSpawn());
		attach(GridShell.createGbc(0, 1, false), getBtExit());
		attach(GridShell.createGbc(1, 1, false), getBtReturn());
	}
	
	@Override public void implDoDraw(Graphics2D g) {}
	@Override public void implDoUpdate() {}
	@Override public void implDoDrawDebug() {}
	
	private EMObjectClickable btSpawn;
	public EMObjectClickable getBtSpawn() {
		if (btSpawn == null) {
			btSpawn = new EMObjectClickable(this, "SPAWN", 80, 30);
			btSpawn.attach(new EMObjectAction() {
				@Override
				public void execute() {
					getPlugin().requestSpawn(1);
				}
			});
		}
		return btSpawn;
	}
	
	private EMObjectClickable btExit;
	public EMObjectClickable getBtExit() {
		if (btExit == null) {
			btExit = new EMObjectClickable(this, "EXIT", 80, 30);
			btExit.attach(new EMObjectAction() {
				@Override
				public void execute() {
					getScreen().getPlugin().kill(null);
				}
			});
		}
		return btExit;
	}
	
	private EMObjectClickable btReturn;
	public EMObjectClickable getBtReturn() {
		if (btReturn == null) {
			btReturn = new EMObjectClickable(this, "Close", 80, 30);
			btReturn.attach(new EMObjectAction() {
				@Override
				public void execute() {
					show(false);
				}
			});
		}
		return btReturn;
	}
}