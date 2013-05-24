package com.aura.engine.univers.drawable;

public abstract class CallableEntityGarbage {
	private boolean flagGarbageAsRefresh;
	public boolean isMarkGarbageAsRefresh() {
		return flagGarbageAsRefresh;
	}
	public void markGarbageAsRefresh() {
		this.flagGarbageAsRefresh = true;
	}
	
	public CallableEntityGarbage() {
		markGarbageAsRefresh();
	}
	
	private DEntity<?>[] entities = new DEntity<?>[0];
	public synchronized DEntity<?>[] getAll() {
		if (isMarkGarbageAsRefresh()) {
			entities = implGetAll();
			this.flagGarbageAsRefresh = false;
		}
		return entities;
	}
	public abstract DEntity<?>[] implGetAll();
}