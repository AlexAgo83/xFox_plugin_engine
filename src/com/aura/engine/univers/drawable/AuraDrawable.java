package com.aura.engine.univers.drawable;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.aura.base.Aura;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreenInfo;
import com.aura.engine.univers.DrawableGarbage;
import com.aura.engine.univers.texture.Sprite;
import com.aura.engine.univers.texture.garbage.SpriteGarbage;
import com.aura.engine.utils.Location;

public abstract class AuraDrawable<A extends Aura, E extends SpriteGarbage<?>> extends AbstractDrawable {
	private final DrawableQueueItem<A> queueItem;
	
	private final A aura;
	
	private E spriteGarbage;
	
	private int currentXHud;
	private int currentYHud;
	
	protected AuraDrawable(A aura, long id, DrawableGarbage dg, String name, Location loc, 
			boolean isCentered, Color color, boolean plain, 
			boolean allowHudMarker, boolean allowHudTarget,
			int width, int depth, E spriteGarbage) {
		super(id, dg, name, loc, isCentered, color, plain, allowHudMarker, allowHudTarget, width);
		this.aura = aura;
		this.spriteGarbage = spriteGarbage;
		this.queueItem = new DrawableQueueItem<A>(this, depth);
	}
	
	public A getAura() {
		return aura;
	}
	
	public DrawableQueueItem<A> getQueueItem() {
		return queueItem;
	}
	
	public E getSpriteGarbage() {
		return spriteGarbage;
	}
	public Sprite getSprite() {
		return getSpriteGarbage().getFocus();
	}
	
	public boolean isDrawable(AuraScreenInfo info) {
//		Location test = getLocation().scale(info.zoom.getValue());
		return !(getCurrentX() < -getWidth() || getCurrentX() > info.frameWidth
			|| getCurrentY() < -getWidth() || getCurrentY() > info.frameHeight);
	}
	
	public int getCurrentXHud() {
		return currentXHud;
	}
	public void setCurrentXHud(int currentXHud) {
		this.currentXHud = currentXHud;
	}
	
	public int getCurrentYHud() {
		return currentYHud;
	}
	public void setCurrentYHud(int currentYHud) {
		this.currentYHud = currentYHud;
	}	
	
	private boolean collide;
	public boolean isCollide() {
		return collide;
	}
	
	public boolean testCollide(AuraDrawable<?, ?> d) {
		return d.getLocation().distance(getLocation()) < getWidth()/2;
	}
//	public boolean testCollide(AuraDrawable<?, ?> d) {
//		return d.getLocation().x > getLocation().x - getWidth()*3/4
//			&& d.getLocation().x < getLocation().x + getWidth()*3/4
//			&& d.getLocation().y > getLocation().y - getWidth()*3/4
//			&& d.getLocation().y < getLocation().y + getWidth()*3/4;
//	}
	
	public AuraDrawable<?, ?> doTestCollide(AuraDrawable<?, ?>[] dws) {
		AuraDrawable<?, ?> tempCollide = null; 
		for (final AuraDrawable<?, ?> dw: dws) {
			if (!this.equals(dw) && testCollide(dw)) {
				tempCollide = dw;
				break;
			}
		}
		this.collide = tempCollide != null;
		return tempCollide;
	}
	public void forceCollide() {
		this.collide = true;
	}

	protected void update(AuraEngine engine) {
		// UPDATE POSITION
		setCurrentScreenX((int) getScreenXPos(engine.getScreenInfo(), getLocation(), true, false));
		setCurrentScreenY((int) getScreenYPos(engine.getScreenInfo(), getLocation(), true, false));
		
		setCurrentScreenXScaled((int) getScreenXPos(engine.getScreenInfo(), getLocation(), false, true));
		setCurrentScreenYScaled((int) getScreenYPos(engine.getScreenInfo(), getLocation(), false, true));

		// UPDATE TARGET
		if (isAllowHudTarget()) {
			int xPos = getCurrentX();
			int yPos = getCurrentY();
			
			if (xPos < 2) xPos = 2;
			if (xPos > engine.getScreenInfo().frameWidth - 6) xPos = engine.getScreenInfo().frameWidth - 6;
			if (yPos < 2) yPos = 2;
			if (yPos > engine.getScreenInfo().frameHeight - 6) yPos = engine.getScreenInfo().frameHeight - 6;
			setCurrentXHud(xPos);
			setCurrentYHud(yPos);
		}
	}
	
	public void draw(AuraEngine scene, Graphics2D g) {
		if (getSprite() != null) 
			getSprite().draw(scene, g, getCurrentX(), getCurrentY());
	}

	public void drawHudMarker(AuraEngine scene, Graphics2D g) {
		if (!isAllowHudMarker())
			return;
		g.setColor(scene.getUiColor());
		g.fillRect(getCurrentXHud(), getCurrentYHud(), 4, 4);
	}
	
	public void drawHudTarget(AuraEngine scene, Graphics2D g, int indexY) {
		if (!isAllowHudTarget())
			return;
		
		g.setColor(scene.getUiColor());
		Composite temp = g.getComposite();
		scene.setComposite(g, .5f);
		int x = scene.getScreenInfo().frameWidth - 80;
		int y = 10 + (indexY * 16);
		g.fillRect(x, y, 70, 14);
		scene.setComposite(g, temp);
		g.drawRect(x, y, 70, 14);
		
		g.drawRect(x-14, y, 14, 14);
		if (getSprite() != null && !scene.isLowQuality()) 
			getSprite().draw(scene, g, x-14, y, 14, 14);

		Font tempFont = g.getFont();
		if (font == null) {
			font = new Font(tempFont.getName(), tempFont.getStyle(), tempFont.getSize() - 3); 
		}
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString(getId()+"@"+getName(), x+6, y+10);
		g.setFont(tempFont);
	}
	
	private Font font;
	public void drawDebug(AuraEngine scene, Graphics2D g) {
		g.setColor(Color.WHITE);

		Font tempFont = g.getFont();
		if (font == null) 
			font = new Font(tempFont.getName(), tempFont.getStyle(), tempFont.getSize() - 3); 
		g.setFont(font);
		
		g.drawString("[ "+getId()+" : "+getToken().getId()+" ] "+getName(), getCurrentX() + 5, getCurrentY() + 12);
		g.drawString(
			new BigDecimal(getLocation().x, new MathContext(3, RoundingMode.HALF_EVEN)).doubleValue() +", " + 
			new BigDecimal(getLocation().y, new MathContext(3, RoundingMode.HALF_EVEN)).doubleValue(), 
			getCurrentX() + 5, getCurrentY() + getWidth() - 4);
		g.setFont(tempFont);
	}
}