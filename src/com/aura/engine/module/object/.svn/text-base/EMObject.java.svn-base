package com.aura.engine.module.object;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import com.aura.engine.module.shell.AbstractShell;

public abstract class EMObject {
	private int x, y;
	
	private final AbstractShell shell;
	private final int w, h;
	
	private String text;
	private boolean visible;
	
	public EMObject(AbstractShell shell, String text, int w, int h) {
		this.shell = shell;
		this.text = text;
		
		this.x = 0;
		this.y = 0;
		
		this.w = w; 
		this.h = h;
		
		this.visible = true;
	}
	
	public AbstractShell getShell() {
		return shell;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getW() { return w; }
	public int getH() { return h; }
	
	public void update(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	protected Composite currentComposite;
	protected boolean currentPinned;
	protected Color currentUIColor;
	
	public void draw(Graphics2D g, boolean pinned) {
		currentComposite = g.getComposite();
		currentPinned = pinned;
		currentUIColor = shell.getScene().getUiColor();
		
		drawBackground(g);
		drawBorder(g);
		drawText(g);
		
		shell.getScene().setComposite(g, currentComposite);
	}
	
	protected void drawBackground(Graphics2D g) {
		g.setColor(currentUIColor);
		if (!shell.getScene().isLowQuality())
			shell.getScene().setComposite(g, currentPinned ? .10f : .50f);
		else
			shell.getScene().setComposite(g, currentComposite);
		if (!(shell.getScene().isLowQuality() && currentPinned))
			g.fillRect(x, y, w, h);
	}
	
	protected void drawBorder(Graphics2D g) {
		g.setColor(currentUIColor);
		if (!shell.getScene().isLowQuality() && currentPinned)
			shell.getScene().setComposite(g, .25f);
		else
			shell.getScene().setComposite(g, currentComposite);
		g.drawRect(x, y, w, h);
	}
	
	protected void drawText(Graphics2D g) {
		g.setColor(currentUIColor);
		if (text != null) {
			shell.getScene().setComposite(g, currentComposite);
			g.setColor(shell.getScene().isLowQuality() && currentPinned ? 
				shell.getScene().getUiColor() : Color.BLACK);
			g.drawString(text, 
				x + (w/2) - (g.getFontMetrics().stringWidth(text)/2), 
				y + (h/2) + 5);
		}
	}
	
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}