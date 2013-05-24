package com.aura.engine.module;

import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.module.shell.ShellDialogTest;
import com.aura.engine.module.shell.ShellFrameMenu;

public class ShellContainer {
	private final AuraClient aura;
	private final AuraScreen screen;
	private final AuraEngine scene;
	
	public ShellContainer(AuraClient aura, AuraScreen screen, AuraEngine scene) {
		this.aura = aura;
		this.screen = screen;
		this.scene = scene;
	}
	
	public void init() {
		// DIALOG(S)
		scene.attachModule(1, getDialogTest());
		
		// FRAME(S)
		scene.attachModule(2, getFrameMenu());
	}
	
	private ShellDialogTest dialogTest;
	public ShellDialogTest getDialogTest() {
		if (dialogTest == null) {
			dialogTest = new ShellDialogTest(aura, screen, scene);
		}
		return dialogTest;
	}

	private ShellFrameMenu frameMenu;
	private ShellFrameMenu getFrameMenu() {
		if (frameMenu == null) {
			frameMenu = new ShellFrameMenu(aura, screen, scene);
		}
		return frameMenu;
	}
}