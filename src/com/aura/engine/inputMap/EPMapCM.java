package com.aura.engine.inputMap;

import java.util.HashMap;
import java.util.Map;

import com.aura.base.container.AbstractContainerManager;
import com.aura.client.AuraClient;
import com.aura.engine.plugin.EngineClientPlugin;

public abstract class EPMapCM extends AbstractContainerManager<AuraClient, EPMapCE> {
	private final EngineClientPlugin plugin;
	public EngineClientPlugin getPlugin() {
		return plugin;
	}
	
	private Map<EPMapCE, Boolean> pressed;
	public Map<EPMapCE, Boolean> getPressed() {
		if (pressed == null)
			pressed = new HashMap<EPMapCE, Boolean>();
		return pressed;
	}
	
	public boolean isPressed(int map) {
		EPMapCE ce = getElement(map);
		if (ce != null)
			return getPressed().get(ce) != null ? getPressed().get(ce) : false;
		return false;
	}
	
	public EPMapCM(AuraClient client, EngineClientPlugin plugin) {
		super(client);
		this.plugin = plugin;
	}
	
	public EPMapCE getByKey(int keyCode) {
		Integer[] ids = getContainerKeyTab();
		for (Integer i: ids) {
			EPMapCE km = getElement(i);
			if (km.getCode() == keyCode)
				return km;
		}
		return null;
	}
}