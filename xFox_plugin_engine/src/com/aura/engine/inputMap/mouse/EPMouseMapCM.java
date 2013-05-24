package com.aura.engine.inputMap.mouse;

import com.aura.base.manager.configuration.ConfigCEInteger;
import com.aura.base.utils.Curseur;
import com.aura.client.AuraClient;
import com.aura.engine.configuration.EPConfigCM;
import com.aura.engine.inputMap.EPMapCE;
import com.aura.engine.inputMap.EPMapCM;
import com.aura.engine.plugin.EngineClientPlugin;

public class EPMouseMapCM extends EPMapCM {
	
	public static final Curseur CURSEUR = new Curseur(EPMouseMapCM.class);
	
	public static final int SELECT = CURSEUR.nextVal();
	public static final int MOVE_TO = CURSEUR.nextVal();
	
	public EPMouseMapCM(AuraClient client, EngineClientPlugin plugin) {
		super(client, plugin);
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
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new EPMapCE(SELECT, "SELECT", EPConfigCM.MOUSE_SELECT));
		register(new EPMapCE(MOVE_TO, "MOVE_TO", EPConfigCM.MOUSE_MOVE_TO));
		
		Integer[] ids = getContainerKeyTab();
		for (Integer i: ids) {
			EPMapCE km = getElement(i);
			ConfigCEInteger ce = (ConfigCEInteger) getAura().getCfgManager().getConfig(km.getConfigId());
			km.setCode(ce.getValue());
		}
	}
}