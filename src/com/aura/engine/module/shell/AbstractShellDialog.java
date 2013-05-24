package com.aura.engine.module.shell;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.event.EPEventInputMouse;
import com.aura.engine.inputMap.EPMapCE;
import com.aura.engine.inputMap.mouse.EPMouseMapCM;
import com.aura.engine.module.object.EMObjectAction;
import com.aura.engine.module.object.EMObjectClickable;

public abstract class AbstractShellDialog extends AbstractShell {
	private String title;
	private int x, y;
	private int w, h;
	
	private boolean moveable;
	private boolean resizeable;
	
	private int minW = 200;
	private int minH = 100;
	
	public AbstractShellDialog(AuraClient aura, AuraScreen screen, AuraEngine scene, TypeShell type, int keyAction, String title) {
		this(aura, screen, scene, type, keyAction, title, 0, 0, true, true);
	}
	public AbstractShellDialog(AuraClient aura, AuraScreen screen, AuraEngine scene, TypeShell type, int keyAction, String title, int width, int height, boolean moveable, boolean resizeable) {
		super(aura, screen, scene, type, keyAction);
		this.title = title;
		
		this.w = width < minW ? minW : width;
		this.h = height < minH ? minH : height;
		
		this.moveable = moveable;
		this.resizeable = resizeable;
		
		this.x = scene.getScreenInfo().frameWidth/2 - getWidth()/2;
		this.y = scene.getScreenInfo().frameHeight/2 - getHeight()/2;
	}
	
	@Override
	public void initialize() {
		GridShell gbcBtSystemPin = GridShell.createFakeGbc(); 
		gbcBtSystemPin.setAllowClickOnPin(true);
		attach(gbcBtSystemPin, getBtSystemPin());
		
		GridShell gbcBtSystemClose = GridShell.createFakeGbc(); 
		gbcBtSystemClose.setAllowClickOnPin(true);
		attach(gbcBtSystemClose, getBtSystemClose());
		
		initializeForDialog();
	}
	public abstract void initializeForDialog();
	
	@Override
	public int getX() {
		return x;
	}
	@Override
	public int getY() {
		return y;
	}
	@Override
	public int getWidth() {
		return w;
	}
	@Override
	public int getHeight() {
		return h;
	}
	
	@Override
	public int getHeaderYDecal() {
		return 14;
	}
	
	public boolean isMoveable() {
		return moveable;
	}
	public boolean isResizeable() {
		return resizeable;
	}
	
	private boolean onMove;
	private boolean onResize;
	private int xClique, yClique;
	
	@Override
	public void doMousePressed(EPEventInputMouse e, EPMapCE map) {
		super.doMousePressed(e, map);
		if (!isVisible() && !isPinned()) return;
		if (getFocusClickable() == null
				&& map != null
				&& !isPinned()
				&& map.getId() == EPMouseMapCM.SELECT) {
			
			int currentX = (int) getScene().getCurrentCurseurLocation().x;
			int currentY = (int) getScene().getCurrentCurseurLocation().y;
			
			if (isMoveable() 
					&& currentX > getX() 
					&& currentX < getX() + getWidth()
					&& currentY > getY() 
					&& currentY < getY() + 14) {
				onMove = true;
				xClique = currentX - x;
				yClique = currentY - y;
			} else if (isResizeable()
					&& currentX > getX() + getWidth() - 14
					&& currentX < getX() + getWidth()
					&& currentY > getY() + getHeight() - 14
					&& currentY < getY() + getHeight()) {
				onResize = true;
			}
		}
	}
	
	@Override
	public void doMouseReleased(EPEventInputMouse e, EPMapCE map) {
		super.doMouseReleased(e, map);
		if (!isVisible() && !isPinned()) return;
		if (map != null && map.getId() == EPMouseMapCM.SELECT) {
			onMove = false;
			onResize = false;
		}
	}
	
	@Override
	public void doUpdate() {
		if (!isVisible() && !isPinned()) 
			return;
		
		updateDialog();
		updateObjects();
		implDoUpdate();
	}
	
	private void updateDialog() {
		if (onMove && !isPinned()) {
			int newX = (int) getScene().getCurrentCurseurLocation().x - xClique;
			int newY = (int) getScene().getCurrentCurseurLocation().y - yClique;
			
			if (newX > getScene().getScreenInfo().frameWidth - getWidth() - 10)
				x = getScene().getScreenInfo().frameWidth - getWidth() - 10;
			else if (newX < 10) 
				x = 10;
			else 
				x = newX;
			
			if (newY > getScene().getScreenInfo().frameHeight - getHeight() - 10)
				y = getScene().getScreenInfo().frameHeight - getHeight() - 10;
			else if (newY < 10) 
				y = 10;
			else 
				y = newY;
			
		} else if (onResize) {
			int newW = (int) getScene().getCurrentCurseurLocation().x - x;
			int newH = (int) getScene().getCurrentCurseurLocation().y - y;

			newW = newW < minW ? minW : newW;
			if (newW + x > getScene().getScreenInfo().frameWidth - 10) {
				newW = w;
			}
			w = newW;
			
			newH = newH < minH ? minH : newH;
			if (newH + y > getScene().getScreenInfo().frameHeight - 10) {
				newH = h;
			}
			h = newH;
		}
		
		getBtSystemPin().update(getX()+getWidth()-24, getY()+2);
		getBtSystemClose().update(getX()+getWidth()-12, getY()+2);
	}
	
	@Override
	public void doDraw(Graphics2D g) {
		if (!isVisible() && !isPinned()) 
			return;
		
		drawWindow(g);
		drawObjects(g);
		implDoDraw(g);
	}
	
	private void drawWindow(Graphics2D g) {
		Composite temp = g.getComposite();
		g.setColor(Color.BLACK);
		
		if (!isPinned()) {
			if (!getScene().isLowQuality())
				getScene().setComposite(g, .75f);
			g.fillRect(0, 0, 
				getScene().getScreenInfo().frameWidth, 
				getScene().getScreenInfo().frameHeight);
		}
		
		getScene().setComposite(g, (isPinned() ? .25f : .75f));
		{ // DRAW DU FOND
			boolean useCase = isPinned() && getScene().isLowQuality();
			if (!useCase)
				g.fillRect(getX(), getY(), getWidth(), getHeight());
		}
		
		getScene().setComposite(g, (isPinned() ? .50f : 1f));
		{ // DRAW DU CADRE
			g.setColor(getScene().getUiColor());
			g.drawRect(getX(), getY(), getWidth(), getHeight());
			g.drawRect(getX(), getY(), getWidth(), 14);
			if (title != null)
				g.drawString(title, getX()+3, getY()+12);
			if (isResizeable())
				g.drawLine(
					getX() + getWidth() - 14, 
					getY() + getHeight(), 
					getX() + getWidth(), 
					getY() + getHeight() - 14);
		}
		
		getScene().setComposite(g, temp);
	}

	private EMObjectClickable btSystemPin;
	public EMObjectClickable getBtSystemPin() {
		if (btSystemPin == null) {
			btSystemPin = new EMObjectClickable(this, "!", 10, 10);
			btSystemPin.attach(new EMObjectAction() {
				@Override
				public void execute() {
					pin(!isPinned());
				}
			});
		}
		return btSystemPin;
	}
	
	private EMObjectClickable btSystemClose;
	public EMObjectClickable getBtSystemClose() {
		if (btSystemClose == null) {
			btSystemClose = new EMObjectClickable(this, "+", 10, 10);
			btSystemClose.attach(new EMObjectAction() {
				@Override
				public void execute() {
					show(false);
				}
			});
		}
		return btSystemClose;
	}
	
	@Override
	public void show(boolean lShow) {
		super.show(lShow);
		onMove = false;
		onResize = false;
	}
}