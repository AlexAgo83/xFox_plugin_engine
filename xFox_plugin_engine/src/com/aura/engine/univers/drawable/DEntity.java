package com.aura.engine.univers.drawable;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import com.aura.base.Aura;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreenInfo;
import com.aura.engine.univers.DrawableGarbage;
import com.aura.engine.univers.texture.garbage.SGEntity;
import com.aura.engine.utils.Location;
import com.aura.engine.utils.Orientation;

public class DEntity<A extends Aura> extends AuraDrawable<A, SGEntity> {
	public final static double DEFAULT_SPEED = 5;
	
	public DEntity(A aura, long id, DrawableGarbage dg, String name, Location loc, SGEntity sprites) {
		super(aura, id, dg, name, loc, true, Color.GRAY, false, true, true, 32, 32, 1, sprites);
		this.speed = DEFAULT_SPEED;
	}
	
	private double speed;
	public double getSpeed() {
		return speed;
	}
	
	private MovementThread moveThread;
	public MovementThread getMoveThread() {
		return moveThread;
	}
	
	@Override
	public void markAsRemove() {
		if (getMoveThread() != null)
			getMoveThread().forceTerminate();
		if (getSpriteGarbage() != null)
			getSpriteGarbage().stop();
		super.markAsRemove();
	}
	
	public void move(final Orientation ori, double speed) {
		move(ori, null, speed);
	}
	public void move(final Location target, double speed) {
		move(null, target, speed);
	}
	private void move(final Orientation ori, final Location target, double speed) {
		if (moveThread == null) {
			moveThread = new MovementThread(getAura(), this) {
				@Override
				public void onOrientationChange(Orientation o) {
					if (getSpriteGarbage() != null) { 
						getSpriteGarbage().setFocus(o);
						getSpriteGarbage().play(getAura(), 100);
					}
				}
			};
			moveThread.start();
		}
		this.speed = speed;
		moveThread.setOrientationOuTarget(ori, target);
	}

	public boolean intersect(DEntity<?> d) {
		return d.getLocation().distance(getLocation()) < (getWidth() / 4);
	}
	
	@Override
	public void update(AuraScreenInfo info) {
		super.update(info);
		if (getMoveThread() != null) 
			setLocation(getMoveThread().getPositionFictive());
	}
	
	@Override
	public void draw(AuraEngine scene, Graphics2D g) {
		super.draw(scene, g);
		g.setColor(scene.getUnivers().getFocusWorld().isSelectionContains(this) ? scene.getUiColor() : getColor());
		Composite tempComposite = g.getComposite();
		scene.setComposite(g, .50f);
		if (isPlain())
			g.fillRect(getCurrentX(), getCurrentY(), getWidth(), getHeight());
		else 
			g.drawRect(getCurrentX(), getCurrentY(), getWidth(), getHeight());
		scene.setComposite(g, tempComposite);
	}
	
	@Override
	public void drawDebug(AuraEngine scene, Graphics2D g) {
		super.drawDebug(scene, g);
		drawMovement(scene, g);
	}
	
	public void drawMovement(AuraEngine scene, Graphics2D g) {
		g.setColor(scene.getUiColor());
		if (moveThread != null) {
			Location locTarget = moveThread.getCurrentTarget();
			Orientation orientation = moveThread.getCurrentOrientation();
			Composite tempComposite = g.getComposite();
			scene.setComposite(g, .50f);
			if (orientation != Orientation.IDLE) {
				int wl = (int) (getCurrentX() + getWidth()/2 + (orientation.getX()*10));
				int hl = (int) (getCurrentY() + getHeight()/2 - (orientation.getY()*10));
				g.drawLine(
					getCurrentX() + getWidth()/2, 
					getCurrentY() + getHeight()/2, 
					wl, hl);
				g.fillRect(wl-2, hl-2, 4, 4); 
			} else if (locTarget != null) {
				
				int xPos = (int) getScreenXPos(scene.getScreenInfo(), getLocation(), true);
				int yPos = (int) getScreenYPos(scene.getScreenInfo(), getLocation(), true);
				
				int wl = (int) getScreenXPos(scene.getScreenInfo(), locTarget, false);
				int hl = (int) getScreenYPos(scene.getScreenInfo(), locTarget, false);
				
				g.drawLine(
					xPos + getWidth()/2, 
					yPos + getHeight()/2, 
					wl, hl);
				g.fillRect(
					wl-2, hl-2, 
					4, 4); 
			}
			scene.setComposite(g, tempComposite);
		}
	}
}