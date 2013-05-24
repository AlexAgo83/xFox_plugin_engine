package com.aura.engine.univers.drawable;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.aura.base.Aura;
import com.aura.engine.AuraEngine;
import com.aura.engine.univers.DrawableGarbage;
import com.aura.engine.univers.texture.garbage.SGEntity;
import com.aura.engine.utils.Location;
import com.aura.engine.utils.Orientation;

public class DEntity<A extends Aura> extends AuraDrawable<A, SGEntity> {
	public final static double DEFAULT_SPEED = 5;
	
	public DEntity(A aura, long id, DrawableGarbage dg, String name, Location loc, SGEntity sprites, CallableEntityGarbage garbage) {
		super(aura, id, dg, name, loc, true, Color.GRAY, false, true, true, 32, 1, sprites);
		this.speed = DEFAULT_SPEED;
		this.entityGarbage = garbage;
	}
	
	private double speed;
	public double getSpeed() {
		return speed;
	}
	
	private CallableEntityGarbage entityGarbage;
	public CallableEntityGarbage getEntityGarbage() {
		return entityGarbage;
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
		move(false, ori, null, speed);
	}
	public void move(boolean poolMode, final Location target, double speed) {
		move(poolMode, null, target, speed);
	}
	private void move(boolean poolMode, final Orientation ori, final Location target, double speed) {
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
		moveThread.setOrientationOuTarget(poolMode, ori, target);
	}

	private List<LineToDraw> moveLineToDraw;
	public List<LineToDraw> getMoveLineToDraw() {
		if (moveLineToDraw == null) {
			moveLineToDraw = new ArrayList<LineToDraw>();
		}
		return moveLineToDraw;
	}
	
	@Override
	public void update(AuraEngine scene) {
		super.update(scene);
		
		if (getMoveThread() != null) {
			getMoveLineToDraw().clear();
			
			setLocation(getMoveThread().getPositionFictive());
			
			if (scene.isDebug()) {
				Orientation currOrientation = getMoveThread().getCurrentOrientation();
				Location currTarget = getMoveThread().getCurrentTarget();
				Location[] poolTarget = getMoveThread().getPoolTarget().toArray(new Location[0]);
				
				if (currTarget != null || poolTarget.length > 0) {
					// MODE TARGET
					Location cursorLocation = currTarget != null ? 
						new Location(
							getScreenXPos(scene.getScreenInfo(), currTarget, false, false),
							getScreenYPos(scene.getScreenInfo(), currTarget, false, false)) : null;
							
					if (cursorLocation != null) {
						getMoveLineToDraw().add(new LineToDraw(
							new Location(
								getCurrentX() + getWidth()/2,
								getCurrentY() + getWidth()/2),
							cursorLocation
						));
					}
					if (poolTarget.length > 0) {
						for (Location l: poolTarget) {
							Location lTemp = new Location(
								getScreenXPos(scene.getScreenInfo(), l, false, false),
								getScreenYPos(scene.getScreenInfo(), l, false, false));
							if (cursorLocation != null) {
								getMoveLineToDraw().add(new LineToDraw(cursorLocation, lTemp));
							}
							cursorLocation = lTemp;
						}
					}
				} else if (currOrientation != null && !currOrientation.isIdle()) {
					// MODE ORIENTATION
					getMoveLineToDraw().add(new LineToDraw(
						new Location(
							getCurrentX() + getWidth()/2,
							getCurrentY() + getWidth()/2),
						new Location(
							(Double) (getCurrentX() + getWidth()/2 + (currOrientation.getX()*10)), 
							(Double) (getCurrentY() + getWidth()/2 - (currOrientation.getY()*10)))
					));
				}
			}
		}
	}
	
	@Override
	public void draw(AuraEngine scene, Graphics2D g) {
		super.draw(scene, g);

		// SPRITE
		if (isCollide())
			g.setColor(Color.RED);
		else if (scene.getUnivers().getFocusWorld() != null) // ne rends pas World obligatoire pour imprimer une entité
			g.setColor(scene.getUnivers().getFocusWorld().isSelectionContains(this) ? scene.getUiColor() : getColor());
		
		// HITBOX
		Composite tempComposite = g.getComposite();
		scene.setComposite(g, .50f);
		if (isPlain())
			g.fillRect(getCurrentX(), getCurrentY(), getWidth(), getWidth());
		else 
			g.drawRect(getCurrentX(), getCurrentY(), getWidth(), getWidth());
		scene.setComposite(g, tempComposite);
	}
	
	@Override
	public void drawDebug(AuraEngine scene, Graphics2D g) {
		super.drawDebug(scene, g);
		drawMovement(scene, g);
	}
	
	protected void drawMovement(AuraEngine scene, Graphics2D g) {
		LineToDraw[] lines = getMoveLineToDraw().toArray(new LineToDraw[0]);
		if (lines.length > 0) {
			g.setColor(scene.getUiColor());
			Composite tempComposite = g.getComposite();
			scene.setComposite(g, .50f);
			for (LineToDraw l: lines) {
				l.draw(g);
			}
			scene.setComposite(g, tempComposite);
		}
	}
}