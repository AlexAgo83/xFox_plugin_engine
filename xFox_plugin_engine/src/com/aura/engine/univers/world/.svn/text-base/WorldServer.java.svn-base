package com.aura.engine.univers.world;

import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;
import com.aura.engine.univers.EntityToken;
import com.aura.engine.univers.EntityTokenServer;
import com.aura.engine.univers.UniversServer;
import com.aura.engine.univers.drawable.AbstractDrawable;
import com.aura.server.AuraServer;

public class WorldServer extends World<AuraServer, UniversServer> {
	public WorldServer(AuraServer server, UniversServer univers) {
		super(server, univers);
	}
	public WorldServer(AuraServer server, UniversServer univers, Long id) {
		super(server, univers, id);
	}
	
	public void broadCast(AbstractPacket packet) {
		EntityToken[] tab = getEntitiesTab();
		for (EntityToken t: tab) {
			EntityTokenServer s = (EntityTokenServer) t;
			if (s.getPacketManager() != null)
				getAura().getNetworkManager().envoyerPacket(s.getPacketManager(), packet);
		}
	}
	
	@Override
	public void attach(AbstractDrawable d) {
		super.attach(d);
		AuraLogger.info(getAura().getSide(), "Entité ["
			+ d.getToken().getId() 
			+ ":"
			+ d.getName()
			+ "] attachée au monde ["+getId()+"] .");
	}
	@Override
	public void removeDrawable(AbstractDrawable d) {
		super.removeDrawable(d);
		AuraLogger.info(getAura().getSide(), "Entité ["
				+ d.getToken().getId() 
				+ ":"
				+ d.getName()
				+ "] détachée du monde ["+getId()+"] .");
	}
}