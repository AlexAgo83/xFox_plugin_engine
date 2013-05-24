package com.aura.engine.univers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aura.base.manager.AbstractPacketManager;
import com.aura.engine.configuration.EPConfigCM;
import com.aura.engine.univers.world.WorldServer;
import com.aura.server.AuraServer;

public class UniversServer extends Univers<AuraServer, WorldServer> {
	private Map<AbstractPacketManager, EntityTokenServer> pmEntities;
	private synchronized Map<AbstractPacketManager, EntityTokenServer> getPmEntities() {
		if (pmEntities == null)
			pmEntities = new HashMap<AbstractPacketManager, EntityTokenServer>();
		return pmEntities;
	}

	private final Map<Long, WorldServer> worlds;
	public synchronized Map<Long, WorldServer> getWorlds() {
		return worlds;
	}
	
	public Long[] getAllWorldId() {
		return getWorlds().keySet().toArray(new Long[0]);
	}
	
	public UniversServer(AuraServer server) {
		super(server);
		this.worlds = new HashMap<Long, WorldServer>();
	}
	
	public WorldServer createNewWorld(long id, int width, int height) {
		WorldServer w = new WorldServer(getAura(), this, id, width, height);
		getWorlds().put(w.getId(), w);
		return w;
	}
	
	private WorldServer defaultWorld;
	public WorldServer getDefaultWorld() {
		if (defaultWorld == null) {
			defaultWorld = createNewWorld(
				getAura().getCfgManager().getConfigIntegerValue(EPConfigCM.WORLD_DEFAULT_SEED),
				getAura().getCfgManager().getConfigIntegerValue(EPConfigCM.WORLD_DEFAULT_WIDTH),
				getAura().getCfgManager().getConfigIntegerValue(EPConfigCM.WORLD_DEFAULT_HEIGHT));
		}
		return defaultWorld; 
	}

	public EntityTokenServer getEntityToken(AbstractPacketManager pm) {
		return getPmEntities().get(pm);
	}
	public EntityTokenServer getEntityTokenByPacket(AbstractPacketManager pm) {
		EntityTokenServer ID = getEntityToken(pm);
		if (ID == null) {
			ID = new EntityTokenServer(EntityToken.GENERER_ID(), pm);
			// Par défaut l'utilisateur rejoint le monde par défaut
			getDefaultWorld().init(ID);
			ID.setWorld(getDefaultWorld());
			getPmEntities().put(pm, ID);
		}
		return ID;
	}
	
	public AbstractPacketManager[] getAllPms() {
		return getPmEntities().keySet().toArray(new AbstractPacketManager[0]);
	}
	
	public EntityTokenServer[] getAllTokens() {
		AbstractPacketManager[] pms = getPmEntities().keySet().toArray(new AbstractPacketManager[0]);
		List<EntityTokenServer> tks = new ArrayList<EntityTokenServer>();
		for (AbstractPacketManager pm: pms) {
			tks.add(getPmEntities().get(pm));
		}
		return tks.toArray(new EntityTokenServer[0]);
	}

	public AbstractPacketManager getPacketByEntityToken(EntityTokenServer token) {
		return token.getPacketManager();
	}
}