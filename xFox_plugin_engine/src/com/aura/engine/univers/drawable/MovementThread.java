package com.aura.engine.univers.drawable;

import com.aura.base.Aura;
import com.aura.base.thread.AuraThread;
import com.aura.engine.utils.Location;
import com.aura.engine.utils.Orientation;

public abstract class MovementThread extends AuraThread {
	private final DEntity<?> drawable;
	
	private Location positionFictive;
	public Location getPositionFictive() {
		return positionFictive;
	}
	
	private Orientation currentOrientation;
	public Orientation getCurrentOrientation() {
		return currentOrientation;
	}
	
	private Location currentTarget;
	public Location getCurrentTarget() {
		return currentTarget;
	}
	
//	private boolean intersect = false;
//	public boolean isIntersect() {
//		return intersect;
//	}
	
	public Orientation setOrientationOuTarget(Orientation orientation, Location target) {
		if (target != null) {
			this.currentTarget = target;
			this.currentOrientation = Orientation.IDLE;
		} else {
			this.currentTarget = null;
			this.currentOrientation = orientation;
		}
		return getCurrentOrientation();
	}
	
	public MovementThread(Aura aura, DEntity<?> drawable) {
		super(aura, "MovementThread", 30);
		this.drawable = drawable;
		this.currentOrientation = Orientation.IDLE;
		this.positionFictive = drawable.getLocation();
	}
	
	@Override public void doOnClose() {}
	@Override public boolean condition() {
		return getMainAura().isRunning() 
				// Si l'objet n'est plus attaché on libère le thread.
			&& !drawable.isMarkAsRemove();}
	
//	private double lastX, lastY;
	
	@Override
	public void action() {
		Orientation newOrientation = getCurrentOrientation();
			
		double speed = drawable.getSpeed();
		double x = newOrientation.getX() * speed;
		double y = newOrientation.getY() * speed;
		
		if (currentTarget != null) {
			Location local = positionFictive.sub(currentTarget);
			if (positionFictive.distance(currentTarget) < 1+(1*speed)) 
				currentTarget = null;
			
			// FIXME XF(AA) tout à revoir au niveau de la direction
			double teta = Math.atan2( local.y, local.x );
			x = - Math.cos(teta) * speed;
			y = Math.sin(teta) * speed;
			
			newOrientation = Orientation.getByTarget(x, y, speed);
		}
		
//		boolean newIntersect = false;
//		for (DEntity<?> dsec: intersect) {
//			if (!dsec.equals(this) 
//					&& dsec.intersect(this)) {
//				newIntersect = true;
//			}
//		}
//		this.intersect = newIntersect;
//		if (isIntersect()) {
//			x = lastX; 
//			y = lastY;
//			if (Math.abs(x) < Math.abs(y)) {
//				x = -x;
//			} else {
//				y = -y;
//			}
//		} else {
//			lastX = x;
//			lastY = y;
//		}
		
		positionFictive.x = positionFictive.x + x;
		positionFictive.y = positionFictive.y - y; // negat
		
		if (lastOrientation != newOrientation) {
			lastOrientation = newOrientation;
			onOrientationChange(newOrientation);
		}
	}
	
	private Orientation lastOrientation;
	public abstract void onOrientationChange(Orientation o);
}