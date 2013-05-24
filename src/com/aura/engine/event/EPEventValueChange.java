package com.aura.engine.event;

import com.aura.base.event.AbstractEvent;

public abstract class EPEventValueChange<E> extends AbstractEvent {
	private E oldValue;
	private E newValue;
	
	public EPEventValueChange(int id) {
		super(id);
	}
	
	public E getOldValue() {
		return oldValue;
	}
	public void setOldValue(E oldValue) {
		this.oldValue = oldValue;
	}
	
	public E getNewValue() {
		return newValue;
	}
	public void setNewValue(E newValue) {
		this.newValue = newValue;
	}
}