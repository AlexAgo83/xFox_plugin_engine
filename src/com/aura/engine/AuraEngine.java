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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aura.base.event.AbstractEvent;
import com.aura.base.event.AuraEventListener;
import com.aura.base.thread.AuraThread;
import com.aura.base.utils.AuraLogger;
import com.aura.client.AuraClient;
import com.aura.engine.configuration.EPConfigCM;
import com.aura.engine.event.EPEventCM;
import com.aura.engine.event.EPEventInputKey;
import com.aura.engine.event.EPEventInputMouse;
import com.aura.engine.event.EPEventZoomChange;
import com.aura.engine.inputMap.EPMapCE;
import com.aura.engine.inputMap.key.EPKeyMapCM;
import com.aura.engine.inputMap.mouse.EPMouseMapCM;
import com.aura.engine.module.AuraEngineModule;
import com.aura.engine.univers.UniversClient;
import com.aura.engine.univers.drawable.DrawableQueueManager;
import com.aura.engine.univers.texture.SpriteSurfaceQuality;
import com.aura.engine.utils.Location;

public abstract class AuraEngine extends AuraThread implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
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
						if ((getModuleFocus() == null || getModuleFocus().equals(m)) && m.isCondition())
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
						if ((getModuleFocus() == null || getModuleFocus().equals(m)) 
								&& m.isCondition())
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
						if ((getModuleFocus() == null || getModuleFocus().equals(m)) 
								&& m.isCondition())
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
						if ((getModuleFocus() == null || getModuleFocus().equals(m)) && m.isCondition())
							m.doMouseReleased(e, map);
			}
		});	
				
		// Init ZOOM CHANGE event
		getMainAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(getMainAura().getEvent(EPEventCM.ZOOM_CHANGE)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				if (drawExperimental) {
					EPEventZoomChange e = (EPEventZoomChange) event;
					zoom = e.getNewValue();
				}
			}
		});	
		
		// init listener
		getScreen().getMainFrame().addKeyListener(this);
		getScreen().getView().addKeyListener(this);
		getScreen().getView().addMouseListener(this);
		getScreen().getView().addMouseMotionListener(this);
		getScreen().getView().addMouseWheelListener(this);
		
		getScreen().getView().requestFocusInWindow();
		
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
	
	private EngineZoom zoom;
	public EngineZoom getZoom() {
		if (zoom == null) {
			zoom = EngineZoom.PC_DEFAULT;
		}
		return zoom;
	}
	
	private AuraScreenInfo screenInfo;
	public void initScreenInfo() {
		if (screenInfo == null) 
			screenInfo = new AuraScreenInfo();
		
		screenInfo.zoom = getZoom();
		screenInfo.clearMessages();
		
		screenInfo.currentCurseur.x = getCurrentCurseurLocation().x;
		screenInfo.currentCurseur.y = getCurrentCurseurLocation().y;
		
		if (getUnivers().getFocusWorld() != null) {
			screenInfo.currentOrigine.x = getUnivers().getFocusWorld().getCameraPosition().x;
			screenInfo.currentOrigine.y = getUnivers().getFocusWorld().getCameraPosition().y;
		} else {
			screenInfo.currentOrigine.x = 0;
			screenInfo.currentOrigine.y = 0;
		}
		
		screenInfo.frameWidth = getScreen().getView().getWidth();
		screenInfo.frameHeight = getScreen().getView().getHeight();
		
		screenInfo.screenWidthZoom = (int) (screenInfo.frameWidth * screenInfo.zoom.getValue());
		screenInfo.screenHeightZoom = (int) (screenInfo.frameHeight * screenInfo.zoom.getValue());
		
		screenInfo.screenOrigineXZoom = (screenInfo.frameWidth / 2 - screenInfo.screenWidthZoom / 2);
		screenInfo.screenOrigineYZoom = (screenInfo.frameHeight / 2 - screenInfo.screenHeightZoom / 2); 
		
		screenInfo.currentTime = getCurrentTime();
		
		screenInfo.deltaTime = getDeltaTime();
		screenInfo.sleepTime = getSleepTime();
	}
	
	public AuraScreenInfo getScreenInfo() {
		if (screenInfo == null) {
			initScreenInfo();
		}
		return screenInfo;
	}
	
	public List<String> getLog() {
		return getScreenInfo().getDebugMessages();
	}
	public void addLog(String msg) {
		getLog().add(msg);
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
			
			// COLOR RENDERING
			Object color = (isLowQuality()) ?
				RenderingHints.VALUE_COLOR_RENDER_SPEED :
				RenderingHints.VALUE_COLOR_RENDER_QUALITY;
			hints.put(RenderingHints.KEY_COLOR_RENDERING, color);
			
			// COLOR RENDERING
			Object rendering = (isLowQuality()) ?
				RenderingHints.VALUE_RENDER_SPEED :
				RenderingHints.VALUE_RENDER_QUALITY;
			hints.put(RenderingHints.KEY_COLOR_RENDERING, rendering);
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
		update(); getScreenInfo().traceUpdateTime();
		draw(); getScreenInfo().traceExecuteDrawTime();
		// EXPERIMENTAL DRAW
//		draw_experimental(); getScreenInfo().traceExecuteDrawTime();
	}

	//////// UPDATE ////////
	protected void update() {
		initScreenInfo();
		for (Integer i: getModules().keySet())
			for (AuraEngineModule m: getModules().get(i))
				if (m.isCondition())
					m.doUpdate(); 
		getDrawableQueueManager().doUpdate();
	}
	
	//////// DRAW ////////
	protected void draw() {
		final Graphics2D gfx = getGraphics();
		{
			drawReset(gfx); 
			
			// PAINT ENTITIES
			if (isDebug()) {
				gfx.setColor(Color.RED);
				gfx.fillRect(0, 0, getScreenInfo().frameWidth, getScreenInfo().frameHeight);
			}
			
			getDrawableQueueManager().draw(gfx);
			getDrawableQueueManager().drawHud(gfx);
			
			getScreenInfo().traceDrawableTime();
			
			// PAINT MODULE
			for (Integer i: getModules().keySet()) 
				for (AuraEngineModule m: getModules().get(i))
					if (m.isCondition())
						m.doDraw(gfx);
			
		}	
		
		// EXECUTE
		executeDraw(gfx); 
	}
	
	private boolean drawExperimental = false;
	protected void draw_experimental() {
		this.drawExperimental = true;
		
		final Graphics2D gfx = getGraphics();
		
		BufferedImage board = new BufferedImage(
				getScreenInfo().frameWidth, getScreenInfo().frameHeight, 
				BufferedImage.TYPE_INT_ARGB);
		final Graphics2D gBoard = (Graphics2D) board.getGraphics();
		
		{
			drawReset(gBoard);
			
			// PAINT ENTITIES
			BufferedImage boardEntity = new BufferedImage(
				getScreenInfo().frameWidth, getScreenInfo().frameHeight, 
				BufferedImage.TYPE_INT_ARGB);
			final Graphics2D gBoardEntity = (Graphics2D) boardEntity.getGraphics();
			
			if (isDebug()) {
				gBoardEntity.setColor(Color.RED);
				gBoardEntity.fillRect(0, 0, boardEntity.getWidth(), boardEntity.getHeight());
			}
		
			getDrawableQueueManager().draw(gBoardEntity);
			
			gBoard.drawImage(
				boardEntity, 
				getScreenInfo().screenOrigineXZoom, getScreenInfo().screenOrigineYZoom,
				getScreenInfo().screenWidthZoom, getScreenInfo().screenHeightZoom, 
				null);
			getDrawableQueueManager().drawHud(gBoard);
			
			getScreenInfo().traceDrawableTime();
			
			// PAINT MODULE
			for (Integer i: getModules().keySet()) 
				for (AuraEngineModule m: getModules().get(i))
					if (m.isCondition())
						m.doDraw(gBoard);
			
		}	
		
		// EXECUTE
		gfx.drawImage(board, null, 0, 0);
		executeDraw(gfx); 
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
				c.getLocation().x - (getScreenInfo().frameWidth / 2),
				c.getLocation().y - (getScreenInfo().frameHeight / 2)
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
			c.getLocation().x - (getScreenInfo().frameWidth / 2),
			c.getLocation().y - (getScreenInfo().frameHeight / 2)
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
	public void mouseWheelMoved(MouseWheelEvent e) {
		EPEventZoomChange c = (EPEventZoomChange) getMainAura().createEvent(EPEventCM.ZOOM_CHANGE);
		c.setOldValue(getZoom());
		if (e.getUnitsToScroll() > 0) {
			c.setNewValue(getZoom().prev());
		} else if (e.getUnitsToScroll() < 0) {
			c.setNewValue(getZoom().next());
		}
		getMainAura().getEventManager().addEvent(c);
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
	
	private Color uiColor;
	public Color getUiColor() {
		if (uiColor == null) {
			uiColor = new Color(
				getMainAura().getCfgManager().getConfigIntegerValue(EPConfigCM.DISPLAY_UI_COLOR_RED),
				getMainAura().getCfgManager().getConfigIntegerValue(EPConfigCM.DISPLAY_UI_COLOR_GREEN),
				getMainAura().getCfgManager().getConfigIntegerValue(EPConfigCM.DISPLAY_UI_COLOR_BLUE));
		}
		return uiColor;
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