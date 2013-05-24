package com.aura.engine;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class AuraFrame extends JFrame implements WindowFocusListener {
	private final AuraScreen screen;
	public AuraScreen getScreen() {
		return screen;
	}
	
	public AuraFrame(String title, GraphicsConfiguration configuration, AuraScreen screen) {
		super(title, configuration);
		this.screen = screen;
		initialize();
		addWindowFocusListener(this);
	}
	
	private void initialize() {
		setUndecorated(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(getScreen().getDisplayMode().isWindowed() ? 
			getScreen().getDisplayMode().getDimension(): 
			getScreen().getEnvironment().getMaximumWindowBounds().getSize());
		setBackground(Color.BLACK);
		setEnabled(true);
		setIgnoreRepaint(true);
		add(getScreen().getCanvas());
		setVisible(true);
//		setAlwaysOnTop(true);
	}
}