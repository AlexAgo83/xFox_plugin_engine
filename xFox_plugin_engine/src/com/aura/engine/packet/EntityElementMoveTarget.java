package com.aura.engine.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.engine.utils.Location;

public class EntityElementMoveTarget extends EntityElementMove {
	@Override
	public TypeEntityElementMove getMoveElementType() {
		return TypeEntityElementMove.TARGET;
	}
	
	private Location target;
	
	public EntityElementMoveTarget() {
		super();
		this.target = new Location(0, 0);
	}
	
	public Location getTarget() {
		return target;
	}
	public void setTarget(Location target) {
		this.target.x = Math.round(target.x);
		this.target.y = Math.round(target.y);
	}
	public void setTarget(int x, int y) {
		this.target.x = Math.round(x);
		this.target.y = Math.round(y);
	}

	@Override
	public String complementWrite(Aura aura, DataOutputStream output) throws IOException {
		return super.complementWrite(aura, output) 
			+ "#" + (int) getTarget().x 
			+ "#" + (int) getTarget().y;
	}
	@Override
	public void complementRead(Aura aura, String[] split) throws IOException {
		super.complementRead(aura, split);
		setTarget(Integer.parseInt(split[7]), Integer.parseInt(split[8]));
	}
	@Override
	public boolean complementValid() {
		return super.complementValid() && getTarget() != null;
	}
}