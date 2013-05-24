package com.aura.engine.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.engine.utils.Orientation;

public class EntityElementMoveOrientation extends EntityElementMove {
	@Override
	public TypeEntityElementMove getMoveElementType() {
		return TypeEntityElementMove.ORIENTATION;
	}
	
	private Orientation orientation;
	
	public EntityElementMoveOrientation() {
		super();
		this.orientation = Orientation.IDLE;
	}
	
	public Orientation getOrientation() {
		return orientation;
	}
	public void setOrientation(Orientation ori) {
		this.orientation = ori;
	}
	
	@Override
	public String complementWrite(Aura aura, DataOutputStream output) throws IOException {
		return super.complementWrite(aura, output) 
			+ "#" + getOrientation().getId();
	}
	@Override
	public void complementRead(Aura aura, String[] split) throws IOException {
		super.complementRead(aura, split);
		setOrientation(Orientation.getById(Integer.parseInt(split[7])));
	}
	@Override
	public boolean complementValid() {
		return super.complementValid() && getOrientation() != null;
	}
}