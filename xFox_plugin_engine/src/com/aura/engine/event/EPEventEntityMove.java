package com.aura.engine.event;

import java.util.ArrayList;
import java.util.List;

import com.aura.base.event.AbstractEvent;
import com.aura.engine.packet.EntityElementMove;

public class EPEventEntityMove extends AbstractEvent {
	private final List<EntityElementMove> entityElementMoves;
	
	public EPEventEntityMove(int id) {
		super(id);
		this.entityElementMoves = new ArrayList<EntityElementMove>();
	}
	
	public void attach(EntityElementMove me) {
		this.entityElementMoves.add(me);
	}
	public void attach(List<EntityElementMove> lst) {
		this.entityElementMoves.addAll(lst);
	}
	public EntityElementMove[] getElements() {
		return entityElementMoves.toArray(new EntityElementMove[0]);
	}
}