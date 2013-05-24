package com.aura.engine;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.aura.base.configuration.PropertiesLoader.PropertiesLoaderException;
import com.aura.client.AuraClient;
import com.aura.engine.plugin.EngineClientPlugin;
import com.aura.engine.univers.texture.SpriteSurfaceQuality;

public abstract class AuraScreen {
	protected final String title;
	protected final AuraClient aura;
	protected final AuraDisplayMode displayMode;
	
	protected BufferStrategy bufferStrategy;
	
	private final GraphicsEnvironment gEnvironment;
	private final GraphicsDevice gDevice;
	private final GraphicsConfiguration gConfiguration;
	
	private final AuraFrame mainFrame;
	private Window window;
	private Canvas canvas;
	
	private boolean fullScreenMode;
	
	private final AuraEngine engine;
	
	@SuppressWarnings("serial")
	public AuraScreen(String title, AuraClient aura, AuraDisplayMode displayMode) {
		this.title = title;
		this.aura = aura;
		this.displayMode = displayMode;
		
		this.gEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		this.gDevice = getEnvironment().getDefaultScreenDevice();
		this.gConfiguration = getDevice().getDefaultConfiguration();

//		sun.java2d
//		sun.java2d.opengl=true  //force ogl  
//		sun.java2d.ddscale=true //only when using direct3d  
//		sun.java2d.translaccel=true //only when using direct3d  

		// Drawer view init
		this.canvas = new Canvas(getConfiguration());
		getCanvas().setPreferredSize(new Dimension(
			displayMode.getWidth(), displayMode.getHeight()));
		getCanvas().setBackground(Color.BLACK);
		getCanvas().setPreferredSize(displayMode.getDimension());
		
		// Main frame init
		this.mainFrame = new AuraFrame(getTitle(), getConfiguration(), this) {
			@Override
			public void windowLostFocus(WindowEvent e) {
				if (getEngine() != null)
					getEngine().pauseThread();
			}
			@Override
			public void windowGainedFocus(WindowEvent e) {
				if (getEngine() != null)
					getEngine().resumeThread();
			}
		};
		fullScreenMode = displayMode.isWindowed();
		if (!isFullScreenMode() && getDevice().isFullScreenSupported()) {
			window = new Window(getMainFrame(), getConfiguration());
			
			getDevice().setFullScreenWindow(getWindow());
			try {
				getDevice().setDisplayMode(displayMode.getDisplayMode());
			} catch (PropertiesLoaderException e) {
				kill(e.getMessage());
			}
			
			getWindow().createBufferStrategy(2);
			do {
	    		bufferStrategy = getWindow().getBufferStrategy();
	    	} while (getBufferStrategy() == null);
		} else {
			getDevice().setFullScreenWindow(null);
			
			getCanvas().createBufferStrategy(2);
			do {
	    		bufferStrategy = getCanvas().getBufferStrategy();
	    	} while (getBufferStrategy() == null);
		}
		
		getMainFrame().pack();
		getMainFrame().setResizable(false);
		getMainFrame().setLocationRelativeTo(null);
		
		this.engine = createEngine(getAura(), this);
	}
	
	public void kill(String raison) {
		if (getAura() != null && getAura().isRunning()) 
			getAura().turnOff(false);
		if (raison != null)
			JOptionPane.showConfirmDialog(null, raison, null, JOptionPane.CANCEL_OPTION);
		if (getMainFrame() != null) {
			getMainFrame().setVisible(false);
		}
//		System.exit(0);
	}
	
	public abstract AuraEngine createEngine(final AuraClient client, final AuraScreen screen);

	private EngineClientPlugin plugin;
	public EngineClientPlugin getPlugin() {
		if (plugin == null)
			plugin = (EngineClientPlugin) getAura().getPlugins().get(EngineClientPlugin.PLUGIN_ID);
		return plugin;
	}

	private SpriteSurfaceQuality quality;
	public SpriteSurfaceQuality getQuality() {
		if (quality == null)
			quality = getPlugin().getCfgManager().getQuality();
		return quality;
	}
	
	public String getTitle() {
		return title;
	}
	public AuraClient getAura() {
		return aura;
	}
	public AuraDisplayMode getDisplayMode() {
		return displayMode;
	}

	public GraphicsEnvironment getEnvironment() {
		return gEnvironment;
	}
	public GraphicsDevice getDevice() {
		return gDevice;
	}
	public BufferStrategy getBufferStrategy() {
		return bufferStrategy;
	}
	public GraphicsConfiguration getConfiguration() {
		return gConfiguration;
	}
	
	public JFrame getMainFrame() {
		return mainFrame;
	}
	public Canvas getCanvas() {
		return canvas;
	}
	public Window getWindow() {
		return window;
	}
	public Component getView() {
		return getWindow() != null ? getWindow() : getCanvas();
	}
	
	public boolean isFullScreenMode() {
		return fullScreenMode;
	}
	
	public AuraEngine getEngine() {
		return engine;
	}
}