package com.aura.engine.univers.drawable;

import java.util.ArrayList;
import java.util.List;

import com.aura.base.Aura;
import com.aura.base.thread.AuraThread;
import com.aura.engine.utils.Location;
import com.aura.engine.utils.Orientation;

public abstract class MovementThread extends AuraThread {
	private final DEntity<?> drawable;
	
	private Location posFictive;
	public Location getPositionFictive() {
		return posFictive;
	}
	
	private Orientation currentOrientation;
	public Orientation getCurrentOrientation() {
		return currentOrientation;
	}
	
	private Location currentTarget;
	public Location getCurrentTarget() {
		return currentTarget;
	}
	
	private final Location currentVector;
	public Location getCurrentVector() {
		return currentVector;
	}
	
	private double currentDist;
	public double getCurrentDist() {
		return currentDist;
	}
	
	private final List<Location> poolTarget;
	public synchronized List<Location> getPoolTarget() {
		return poolTarget;
	}
	
	public Orientation setOrientationOuTarget(boolean poolMode, Orientation orientation, Location target) {
		if (target != null) {
			if (poolMode) {
				getPoolTarget().add(target);
			} else {
				getPoolTarget().clear();
				this.currentTarget = target;
			}
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
		this.currentVector = new Location(0, 0);
		this.posFictive = drawable.getLocation();
		this.poolTarget = new ArrayList<Location>();
	}
	
	@Override public void doOnClose() {}
	
	@Override 
	public boolean condition() {
		return !drawable.isMarkAsRemove();
	}
	
	@Override
	public void action() {
		Orientation newOrientation = getCurrentOrientation();
			
		currentVector.x = newOrientation.getX() * drawable.getSpeed();
		currentVector.y = -newOrientation.getY() * drawable.getSpeed();
		
		if (currentTarget != null) {
			Location local = posFictive.sub(currentTarget);
			
			// Check si l'entité est arrivée à destination
			currentDist = posFictive.distance(currentTarget);
			if (currentDist < 1 + (1 * drawable.getSpeed())) 
				currentTarget = null;
			
			// FIXME XF(AA) tout à revoir au niveau de la direction
			double teta = Math.atan2(local.y, local.x);
			currentVector.x = - Math.cos(teta) * drawable.getSpeed();
			currentVector.y = - Math.sin(teta) * drawable.getSpeed();
			
			// Mise à jour de l'orientation, par rapport à la destination.
			newOrientation = Orientation.getByTarget(currentVector.x, -currentVector.y, drawable.getSpeed());
		} else if (getPoolTarget().size() != 0) {
			currentTarget = getPoolTarget().get(0);
			getPoolTarget().remove(currentTarget);
		}
		
		posFictive.x = posFictive.x + currentVector.x;
		posFictive.y = posFictive.y + currentVector.y; // negat
		
		if (lastOrientation != newOrientation) {
			lastOrientation = newOrientation;
			onOrientationChange(newOrientation);
		}
	}
	
	private Orientation lastOrientation;
	public abstract void onOrientationChange(Orientation o);
}