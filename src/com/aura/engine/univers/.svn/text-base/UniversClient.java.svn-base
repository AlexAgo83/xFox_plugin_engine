package com.aura.engine.univers;

import com.aura.client.AuraClient;
import com.aura.engine.univers.world.WorldClient;

public class UniversClient extends Univers<AuraClient, WorldClient> {
	private EntityTokenClient self;
	public EntityTokenClient getSelf() {
		return self;
	}
	public void setSelf(EntityTokenClient self) {
		this.self = self;
	}
	
	private WorldClient focusWorld;
	public WorldClient getFocusWorld() {
		return focusWorld;
	}
	public void setFocusWorld(WorldClient focus) {
		WorldClient temp = null;
		if (getFocusWorld() != null && !getFocusWorld().equals(focus))
			temp = getFocusWorld();
		this.focusWorld = focus;
		if (temp != null)
			temp.clear();
	}
	
	public UniversClient(AuraClient client) {
		super(client);
	}
}