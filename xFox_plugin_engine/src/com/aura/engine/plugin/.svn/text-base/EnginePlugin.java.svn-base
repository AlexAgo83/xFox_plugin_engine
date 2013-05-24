package com.aura.engine.plugin;

import com.aura.base.AbstractAuraPlugin;
import com.aura.base.Aura;
import com.aura.base.container.AbstractContainerManager.KeyContainerAlreadyUsed;
import com.aura.engine.configuration.EPConfigCM;
import com.aura.engine.console.EPConsoleCM;
import com.aura.engine.event.EPEventCM;
import com.aura.engine.packet.EPPacketCM;

public abstract class EnginePlugin<E extends Aura> extends AbstractAuraPlugin<E> {
	public final static int PLUGIN_ID = -2;
	public EnginePlugin(E aura) {
		super(PLUGIN_ID, "engine", aura);
	}
	
	private EPConfigCM<E> cfgManager;
	@SuppressWarnings("unchecked")
	public EPConfigCM<E> getCfgManager() {
		if (cfgManager == null) {
			cfgManager = new EPConfigCM<E>(this, getAura().getCfgManager().getContainerManager());
		}
		return cfgManager;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected boolean implLoad() throws KeyContainerAlreadyUsed {
		getCfgManager().load();
		new EPPacketCM<E>(getAura().getNetworkManager().getContainerManager()).load();
		new EPEventCM<E>(getAura().getEventManager().getContainerManager()).load();
		new EPConsoleCM<E>(getAura().getConsoleManager().getContainerManager()).load();
		return true;
	}
}