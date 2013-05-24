package com.aura.engine.module.shell;

import java.awt.GridBagConstraints;
import java.awt.Insets;

@SuppressWarnings("serial")
public class GridShell extends GridBagConstraints {
	private boolean fake;
	private boolean allowClickOnPin;
	
	public GridShell() {
		this(false, false);
	}
	public GridShell(boolean isFake, boolean isAllowClickOnPin) {
		this.fake = isFake;
		this.allowClickOnPin = isAllowClickOnPin;
	}
	
	public boolean isFake() {
		return fake;
	}
	public void setFake(boolean fake) {
		this.fake = fake;
	}
	
	public boolean isAllowClickOnPin() {
		return allowClickOnPin;
	}
	public void setAllowClickOnPin(boolean allowClickOnPin) {
		this.allowClickOnPin = allowClickOnPin;
	}
	
	private static int fakeXGbc = -1;
	private static int getFakeXGbc() {
		int temp = fakeXGbc;
		fakeXGbc -= 1;
		return temp;
	}
	
	public static GridShell createFakeGbc() {
		GridShell shell = createGbc(getFakeXGbc(), 0, false, 0);
		shell.setFake(true);
		return shell;
	}
	public static GridShell createGbc(int x, int y, boolean fill) {
		return createGbc(x, y, fill, 0);
	}
	public static GridShell createGbc(int x, int y, boolean fill, int gridwidth) {
		GridShell gbc = new GridShell();
		gbc.gridx = x; gbc.gridy = y;
		gbc.fill = fill ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
		gbc.weightx = fill ? 1 : 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		if (gridwidth > 0)
			gbc.gridwidth = gridwidth;
		return gbc;
	}
}