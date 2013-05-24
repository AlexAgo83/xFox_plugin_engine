package com.aura.engine.univers;

import com.aura.base.manager.AbstractPacketManager;
import com.aura.engine.univers.world.WorldServer;

public class EntityTokenServer extends EntityToken {
	private final AbstractPacketManager packetManager;
	public AbstractPacketManager getPacketManager() {
		return packetManager;
	}
	
	private WorldServer world;
	public WorldServer getWorld() {
		return world;
	}
	public void setWorld(WorldServer world) {
		this.world = world;
	}
	
	public EntityTokenServer(Long id, AbstractPacketManager pm) {
		super(id);
		this.packetManager = pm;
	}
}