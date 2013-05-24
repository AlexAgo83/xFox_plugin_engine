package com.aura.engine.inputMap.key;

import java.util.ArrayList;
import java.util.List;

import com.aura.base.manager.configuration.ConfigCEInteger;
import com.aura.base.utils.Curseur;
import com.aura.client.AuraClient;
import com.aura.engine.configuration.EPConfigCM;
import com.aura.engine.inputMap.EPMapCE;
import com.aura.engine.inputMap.EPMapCM;
import com.aura.engine.plugin.EngineClientPlugin;
import com.aura.engine.utils.Orientation;

public class EPKeyMapCM extends EPMapCM {
	
	public static final Curseur CURSEUR = new Curseur(EPKeyMapCM.class);
	
	public static final int MENU = CURSEUR.nextVal();
	public static final int ENTER = CURSEUR.nextVal();
	public static final int DEBUG = CURSEUR.nextVal();
	public static final int DEBUG_NO_TEXTURE = CURSEUR.nextVal();
	
	public static final int UP = CURSEUR.nextVal();
	public static final int DOWN = CURSEUR.nextVal();
	public static final int LEFT = CURSEUR.nextVal();
	public static final int RIGHT = CURSEUR.nextVal();
	
	public static final int SELECT_ALL = CURSEUR.nextVal();
	public static final int FOCUS = CURSEUR.nextVal();
	
	public EPKeyMapCM(AuraClient client, EngineClientPlugin plugin) {
		super(client, plugin);
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new EPMapCE(MENU, "MENU", EPConfigCM.KEYBOARD_MENU));
		register(new EPMapCE(ENTER, "ENTER", EPConfigCM.KEYBOARD_ENTER));
		register(new EPMapCE(DEBUG, "DEBUG", EPConfigCM.KEYBOARD_DEBUG));
		register(new EPMapCE(DEBUG_NO_TEXTURE, "DEBUG_NO_TEXTURE", EPConfigCM.KEYBOARD_DEBUG_NO_TEXTURE));
		
		register(new EPKeyMapCEMovement(UP, "UP", Orientation.NORD, EPConfigCM.KEYBOARD_UP));
		register(new EPKeyMapCEMovement(DOWN, "DOWN", Orientation.SUD, EPConfigCM.KEYBOARD_DOWN));
		register(new EPKeyMapCEMovement(LEFT, "LEFT", Orientation.OUEST, EPConfigCM.KEYBOARD_LEFT));
		register(new EPKeyMapCEMovement(RIGHT, "RIGHT", Orientation.EST, EPConfigCM.KEYBOARD_RIGHT));
		
		register(new EPMapCE(SELECT_ALL, "SELECT_ALL", EPConfigCM.KEYBOARD_SELECT_ALL));
		register(new EPMapCE(FOCUS, "FOCUS", EPConfigCM.KEYBOARD_FOCUS));
		
		Integer[] ids = getContainerKeyTab();
		for (Integer i: ids) {
			EPMapCE km = getElement(i);
			ConfigCEInteger ce = (ConfigCEInteger) getAura().getCfgManager().getConfig(km.getConfigId());
			km.setCode(ce.getValue());
		}
	}
	
	public Orientation getByKeyMap() {
		List<EPKeyMapCEMovement> actives = new ArrayList<EPKeyMapCEMovement>();
		for (EPMapCE km: getPressed().keySet()) {
			Boolean b = getPressed().get(km);
			if (b != null && b && km instanceof EPKeyMapCEMovement)
				actives.add((EPKeyMapCEMovement) km);
		}
		
		Orientation result = null;
		if (actives.size() == 2) {
			return Orientation.getConbinaison(
				actives.get(0).getOrientation(), 
				actives.get(1).getOrientation()
			);
		} else if (actives.size() == 1) {
			result = actives.get(0).getOrientation();
		} else if (actives.size() == 0) {
			result = Orientation.IDLE;
		}
		return result;
	}
}