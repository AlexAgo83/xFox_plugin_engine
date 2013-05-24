package com.aura.engine;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aura.base.event.AbstractEvent;
import com.aura.base.event.AuraEventListener;
import com.aura.base.thread.AuraThread;
import com.aura.base.utils.AuraBaliseParser;
import com.aura.base.utils.AuraLogger;
import com.aura.client.AuraClient;
import com.aura.engine.configuration.EPConfigCM;
import com.aura.engine.event.EPEventCM;
import com.aura.engine.event.EPEventInputKey;
import com.aura.engine.event.EPEventInputMouse;
import com.aura.engine.inputMap.EPMapCE;
import com.aura.engine.inputMap.key.EPKeyMapCM;
import com.aura.engine.inputMap.mouse.EPMouseMapCM;
import com.aura.engine.module.AuraEngineModule;
import com.aura.engine.univers.UniversClient;
import com.aura.engine.univers.drawable.DrawableQueueManager;
import com.aura.engine.univers.texture.SpriteSurfaceQuality;
import com.aura.engine.utils.Location;

public abstract class AuraEngine extends AuraThread implements KeyListener, MouseListener, MouseMotionListener {
	private final AuraScreen screen;
	public AuraScreen getScreen() {
		return screen;
	}
	
	public AuraEngine(AuraClient a, AuraScreen s, int fpsTarget) {
		super(a, "AuraScreenThread", fpsTarget > 0 ? 1000 / fpsTarget : 0);
		this.screen = s;
		this.drawableQueueManager = new DrawableQueueManager(this);
		
		// Init key Event
		getMainAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(getMainAura().getEvent(EPEventCM.KEY_PRESSED)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				EPEventInputKey e = (EPEventInputKey) event;
				EPMapCE map = getKeyMapManager().getByKey(e.getCode());
				if (map != null) 
					getKeyMapManager().getPressed().put(map, true);
				for (Integer i: getModules().keySet())
					for (AuraEngineModule m: getModules().get(i))
						if (getModuleFocus() == null || getModuleFocus().equals(m))
							m.doKeyPressed(e, map);
			}
		});
		getMainAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(getMainAura().getEvent(EPEventCM.KEY_RELEASED)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				EPEventInputKey e = (EPEventInputKey) event;
				EPMapCE map = getKeyMapManager().getByKey(e.getCode());
				if (map != null) {
					getKeyMapManager().getPressed().put(map, false);
//					if (map.getId() == EPKeyMapCM.MENU) {
//						getScreen().getPlugin().kill(null);
//						return;
//					} else 
					if (map.getId() == EPKeyMapCM.DEBUG) {
						setDebug(!isDebug());
						return;
					} else if (map.getId() == EPKeyMapCM.DEBUG_NO_TEXTURE) {
						showSprite = !showSprite;
						return;
					}
				}
				
				for (Integer i: getModules().keySet())
					for (AuraEngineModule m: getModules().get(i))
						if (getModuleFocus() == null || getModuleFocus().equals(m))
							m.doKeyReleased(e, map);
			}
		});	
						
		// Init mouse Event
		getMainAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(getMainAura().getEvent(EPEventCM.MOUSE_PRESSED)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				EPEventInputMouse e = (EPEventInputMouse) event;
				EPMapCE map = getMouseMapManager().getByKey(e.getCode());
				if (map != null) {
					getMouseMapManager().getPressed().put(map, true);
				}
				for (Integer i: getModules().keySet())
					for (AuraEngineModule m: getModules().get(i))
						if (getModuleFocus() == null || getModuleFocus().equals(m))
							m.doMousePressed(e, map);
			}
		});
		getMainAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(getMainAura().getEvent(EPEventCM.MOUSE_RELEASED)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				EPEventInputMouse e = (EPEventInputMouse) event;
				EPMapCE map = getMouseMapManager().getByKey(e.getCode());
				if (map != null) {
					getMouseMapManager().getPressed().put(map, false);
				}
				for (Integer i: getModules().keySet())
					for (AuraEngineModule m: getModules().get(i))
						if (getModuleFocus() == null || getModuleFocus().equals(m))
							m.doMouseReleased(e, map);
			}
		});	
		
		// init listener
		getScreen().getView().addMouseListener(this);
		getScreen().getMainFrame().addKeyListener(this);
		getScreen().getView().addKeyListener(this);
		getScreen().getView().addMouseMotionListener(this);
		
		init();
	}
	
	public abstract void init();
	
	private boolean start;
	public boolean isStart() {
		return start;
	}
	public void play() {
		start();
		this.start = true;
	}
	
	private Map<Integer, List<AuraEngineModule>> modules;
	protected Map<Integer, List<AuraEngineModule>> getModules() {
		if (modules == null)
			modules = new HashMap<Integer, List<AuraEngineModule>>();
		return modules;
	}
	public void attachModule(int depth, AuraEngineModule module) {
		if (getModules().get(depth) == null) {
			getModules().put(depth, new ArrayList<AuraEngineModule>());
		}
		getModules().get(depth).add(module);
	}
	
	private AuraEngineModule moduleFocus;
	public AuraEngineModule getModuleFocus() {
		return moduleFocus;
	}
	public void setModuleFocus(AuraEngineModule moduleFocus) {
		AuraEngineModule temp = getModuleFocus();
		this.moduleFocus = moduleFocus;
		if (moduleFocus != null)
			AuraLogger.fine(getMainAura().getSide(), "Focus request: "+moduleFocus);
		else
			AuraLogger.fine(getMainAura().getSide(), "Focus release: "+temp);
	}
	
	private DrawableQueueManager drawableQueueManager;
	public DrawableQueueManager getDrawableQueueManager() {
		return drawableQueueManager;
	}
	
	public UniversClient getUnivers() {
		return getScreen().getPlugin().getUnivers();
	}
	
	private AuraScreenInfo screenInfo;
	public void initScreenInfo() {
		if (screenInfo == null) 
			screenInfo = new AuraScreenInfo();
		
		screenInfo.currentCurseur.x = getCurrentCurseurLocation().x;
		screenInfo.currentCurseur.y = getCurrentCurseurLocation().y;
		
		screenInfo.currentOrigine.x = getUnivers().getFocusWorld().getCameraPosition().x;
		screenInfo.currentOrigine.y = getUnivers().getFocusWorld().getCameraPosition().y;
		
		screenInfo.screenWidth = getScreen().getView().getWidth();
		screenInfo.screenHeight = getScreen().getView().getHeight();
		
		screenInfo.currentTime = getCurrentTime();
		
		screenInfo.deltaTime = getDeltaTime();
		screenInfo.sleepTime = getSleepTime();
	}
	public AuraScreenInfo getScreenInfo() {
		return screenInfo;
	}
	
	@Override public boolean condition() { return getMainAura().isRunning();}
	@Override public void doOnClose() {}
	
	private Map<Key, Object> hints; 
	public Map<Key, Object> getGraphicsHints() {
		if (hints == null) {
			hints = new HashMap<RenderingHints.Key, Object>();
			
			// ANTI-ALIASING
			Object antiAliasing = (getMainAura().getCfgManager().getConfigBooleanValue(EPConfigCM.DISPLAY_ANTI_ALIASING)) ?
				RenderingHints.VALUE_ANTIALIAS_ON :
				RenderingHints.VALUE_ANTIALIAS_OFF;
			hints.put(RenderingHints.KEY_ANTIALIASING, antiAliasing);
			
			// RENDERING
			Object rendering = (isLowQuality()) ?
				RenderingHints.VALUE_RENDER_SPEED :
				RenderingHints.VALUE_RENDER_QUALITY;
			hints.put(RenderingHints.KEY_RENDERING, rendering);
			
			// RENDERING COLOR
			Object color = (isLowQuality()) ?
				RenderingHints.VALUE_COLOR_RENDER_SPEED:
				RenderingHints.VALUE_COLOR_RENDER_QUALITY;
			hints.put(RenderingHints.KEY_COLOR_RENDERING, color);
		}
		return hints;
	}
	
	private Graphics2D getGraphics() {
		Graphics2D g = (Graphics2D) getScreen().getBufferStrategy().getDrawGraphics();
		g.setRenderingHints(getGraphicsHints());
		return g;
	}
	
	@Override
	public void action() {
		initScreenInfo();
		
		// UPDATE
		for (Integer i: getModules().keySet())
			for (AuraEngineModule m: getModules().get(i))
				m.doUpdate(); 
		getDrawableQueueManager().doUpdate();
		getScreenInfo().traceUpdateTime();
				
		final Graphics2D g = getGraphics();
		{
			drawReset(g); 
			
			// PAINT ENTITIES
			getDrawableQueueManager().draw(g);
			getScreenInfo().traceDrawableTime();
			
			// PAINT MODULE
			for (Integer i: getModules().keySet()) 
				for (AuraEngineModule m: getModules().get(i))
					m.doDraw(g);
			
			// PAINT DEBUG
			drawDebug(g);
		}	
		// EXECUTE
		executeDraw(g); 
		getScreenInfo().traceExecuteDrawTime();
	}
	
	private void drawReset(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 
			screen.getDisplayMode().getWidth(), 
			screen.getDisplayMode().getHeight());
	}

	private boolean lDebug = false;
	public boolean isDebug() {
		return lDebug;
	}
	public void setDebug(boolean value) {
		this.lDebug = value;
	}
	
	private void drawDebug(Graphics2D g) {
		List<String> msg = new ArrayList<String>();
		msg.add(
			AuraBaliseParser.COLOR.getBalise("orange") + "FPS: " 
			+ AuraBaliseParser.COLOR.getBalise("white") + getFps() 
			+ AuraBaliseParser.COLOR.getBalise("orange") + " [Target: " 
			+ AuraBaliseParser.COLOR.getBalise("white")+ getTargetFps() 
			+ AuraBaliseParser.COLOR.getBalise("orange") + "]  PING: " 
			+ AuraBaliseParser.COLOR.getBalise("white") + ((AuraClient) getMainAura()).getNetworkManager().getClientConnection().getTimePingServer());
		if (lDebug) {
			msg.add(
				AuraBaliseParser.COLOR.getBalise("orange") + "Free/Delta time: " 
			 	+ AuraBaliseParser.COLOR.getBalise("white") + getScreenInfo().sleepTime
			 	+ AuraBaliseParser.COLOR.getBalise("orange") + " / "
			 	+ AuraBaliseParser.COLOR.getBalise("white") + getScreenInfo().deltaTime);
			msg.add(
				AuraBaliseParser.COLOR.getBalise("orange") + "Update/Drawable/Execute time: " 
			 	+ AuraBaliseParser.COLOR.getBalise("white") + getScreenInfo().updateTime
			 	+ AuraBaliseParser.COLOR.getBalise("orange") + " / "
			 	+ AuraBaliseParser.COLOR.getBalise("white") + getScreenInfo().drawableTime
			 	+ AuraBaliseParser.COLOR.getBalise("orange") + " / "
			 	+ AuraBaliseParser.COLOR.getBalise("white") + getScreenInfo().executeDrawTime);
			msg.add(
				AuraBaliseParser.COLOR.getBalise("orange") + "Entities: " 
				+ AuraBaliseParser.COLOR.getBalise("white") + getScreen().getPlugin().getUnivers().getFocusWorld().getEntitySize());
			msg.add(
				AuraBaliseParser.COLOR.getBalise("orange") + "Depth size: " 
				+ AuraBaliseParser.COLOR.getBalise("white") + getScreen().getEngine().getDrawableQueueManager().getQueueList().size());
			msg.add(
				AuraBaliseParser.COLOR.getBalise("orange") + "Drawables: " 
				+ AuraBaliseParser.COLOR.getBalise("white") + getScreen().getEngine().getDrawableQueueManager().getQueueSize()
				+ AuraBaliseParser.COLOR.getBalise("orange") + " [Visible: " 
				+ AuraBaliseParser.COLOR.getBalise("white") + getScreen().getEngine().getDrawableQueueManager().getLastPrintCount()
				+ AuraBaliseParser.COLOR.getBalise("orange") + "]");
			msg.add(
				AuraBaliseParser.COLOR.getBalise("orange") + "Selection: "
				+ AuraBaliseParser.COLOR.getBalise("white") + getUnivers().getFocusWorld().getDrawableSelectionTab().length);
			
			for (Integer i: getModules().keySet())
				for (AuraEngineModule m: getModules().get(i))
					m.doDrawDebug(msg);
		}
		
		Composite tempComposite = g.getComposite();
		setComposite(g, .5f) ;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 
			280, 
			(msg.size() * 11) + 11);
		setComposite(g, tempComposite);
		
		int p = 15;
		for (String str: msg) {
			AuraEngineModule.drawText(g, str, p);
			p += 11;
		}
	}
	
	private void executeDraw(Graphics2D g) {
		g.dispose(); g = null;
		screen.getBufferStrategy().show();
		Toolkit.getDefaultToolkit().sync();		
	}
	
	@Override public void keyTyped(KeyEvent e) {}
	
	@Override public void mouseClicked(MouseEvent e) {}
	@Override 
	public void mouseEntered(MouseEvent e) {
//		resumeThread();
	}
	@Override 
	public void mouseExited(MouseEvent e) {
//		pauseThread();
	}
	
	@Override 
	public void mouseReleased(MouseEvent e) {
		EPEventInputMouse c = (EPEventInputMouse) getMainAura().createEvent(EPEventCM.MOUSE_RELEASED);
		c.setCode(e.getButton());
		c.setLocation(new Location(
			e.getXOnScreen() - getScreen().getView().getLocationOnScreen().x, 
			e.getYOnScreen() - getScreen().getView().getLocationOnScreen().y
		));
		c.setCoordinate(new Location(
				c.getLocation().x - (getScreen().getView().getWidth() / 2),
				c.getLocation().y - (getScreen().getView().getHeight() / 2)
		));
		getMainAura().getEventManager().addEvent(c);
	}
	
	@Override 
	public void mousePressed(MouseEvent e) {
		EPEventInputMouse c = (EPEventInputMouse) getMainAura().createEvent(EPEventCM.MOUSE_PRESSED);
		c.setCode(e.getButton());
		c.setLocation(new Location(
			e.getXOnScreen() - getScreen().getView().getLocationOnScreen().x, 
			e.getYOnScreen() - getScreen().getView().getLocationOnScreen().y
		));
		c.setCoordinate(new Location(
			c.getLocation().x - (getScreen().getView().getWidth() / 2),
			c.getLocation().y - (getScreen().getView().getHeight() / 2)
		));
		getMainAura().getEventManager().addEvent(c);
	}
	
	private Location currentCurseurLocation;
	public Location getCurrentCurseurLocation() {
		if (currentCurseurLocation == null) {
			currentCurseurLocation = new Location(0, 0);
		}
		return currentCurseurLocation;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		try {
			getCurrentCurseurLocation().x = e.getXOnScreen() - getScreen().getView().getLocationOnScreen().x;
			getCurrentCurseurLocation().y = e.getYOnScreen() - getScreen().getView().getLocationOnScreen().y;
		} catch (Exception exc) {
			AuraLogger.severe(getMainAura().getSide(), "Mouse moved event abort.", exc);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		try {
			getCurrentCurseurLocation().x = e.getXOnScreen() - getScreen().getView().getLocationOnScreen().x;
			getCurrentCurseurLocation().y = e.getYOnScreen() - getScreen().getView().getLocationOnScreen().y;
		} catch (Exception exc) {
			AuraLogger.severe(getMainAura().getSide(), "Mouse dragged event abort.", exc);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		EPEventInputKey c = (EPEventInputKey) getMainAura().createEvent(EPEventCM.KEY_PRESSED);
		c.setCode(e.getKeyCode());
		c.setKeyChar(e.getKeyChar());
		getMainAura().getEventManager().addEvent(c);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		EPEventInputKey c = (EPEventInputKey) getMainAura().createEvent(EPEventCM.KEY_RELEASED);
		c.setCode(e.getKeyCode());
		c.setKeyChar(e.getKeyChar());
		getMainAura().getEventManager().addEvent(c);
	}
	
	public EPKeyMapCM getKeyMapManager() {
		return getScreen().getPlugin().getKeyMapCM();
	}
	public EPMouseMapCM getMouseMapManager() {
		return getScreen().getPlugin().getMouseMapCM();
	}
	
	private boolean showSprite = true;
	public boolean isShowSprite() {
		return showSprite;
	}
	
	public boolean isLowQuality() {
		return getScreen().getQuality() == SpriteSurfaceQuality.LOW;
	}
	public void setComposite(Graphics2D g, float value) {
		if (!isLowQuality())
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(value, 1.0f))) ;
	}
	public void setComposite(Graphics2D g, Composite c) {
		if (getScreen().getQuality() != SpriteSurfaceQuality.LOW)
			g.setComposite(c);
	}
}