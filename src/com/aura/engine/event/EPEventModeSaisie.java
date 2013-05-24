package com.aura.engine.event;

import com.aura.base.event.AbstractEvent;

public class EPEventModeSaisie extends AbstractEvent {
	private boolean modeSaisie;
	public boolean getModeSaisie() {
		return modeSaisie;
	}
	public void setModeSaisie(boolean modeSaisie) {
		this.modeSaisie = modeSaisie;
	}
	public EPEventModeSaisie(int id) {
		super(id);
	}
}
