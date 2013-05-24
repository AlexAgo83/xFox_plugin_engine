package com.aura.engine.univers.texture;

import com.aura.base.container.AbstractContainerManager;
import com.aura.base.utils.AuraLogger;
import com.aura.base.utils.Curseur;
import com.aura.client.AuraClient;

public class EPSpriteCM extends AbstractContainerManager<AuraClient, EPSpriteCE> {
	protected static final Curseur CURSEUR = new Curseur(EPSpriteCM.class);
	
	public static final int GROUND_ALL = CURSEUR.nextVal();
	public static final int GROUND_VENTIL = CURSEUR.nextVal();
	public static final int GROUND_MACHINE = CURSEUR.nextVal();
	public static final int GROUND_GLOW = CURSEUR.nextVal();
	
	public static final int ENTITY_WALK_STAND = CURSEUR.nextVal();
	public static final int ENTITY_WALK_UP = CURSEUR.nextVal();
	public static final int ENTITY_WALK_DOWN = CURSEUR.nextVal();
	public static final int ENTITY_WALK_LEFT = CURSEUR.nextVal();
	public static final int ENTITY_WALK_RIGHT = CURSEUR.nextVal();

	public EPSpriteCM(AuraClient client, EPSpriteSurfaceCM surfaceCM) {
		super(client);
		this.surfaceCM = surfaceCM;
	}
	
	private final EPSpriteSurfaceCM surfaceCM;
	public EPSpriteSurfaceCM getSurfaceCM() {
		return surfaceCM;
	}
	
	public EPSpriteCE[] getSpriteToRegister() {
		return new EPSpriteCE[] {
			new EPSpriteCE(GROUND_ALL, "GROUND_ALL", getSurfaceCM().getElement(EPSpriteSurfaceCM.GROUND_BASE), 0, 24, true),
			new EPSpriteCE(GROUND_VENTIL, "GROUND_VENTIL", getSurfaceCM().getElement(EPSpriteSurfaceCM.GROUND_BASE), 5, 8, true),
			new EPSpriteCE(GROUND_MACHINE, "GROUND_MACHINE", getSurfaceCM().getElement(EPSpriteSurfaceCM.GROUND_BASE), 10, 13, true),
			new EPSpriteCE(GROUND_GLOW, "GROUND_GLOW", getSurfaceCM().getElement(EPSpriteSurfaceCM.GROUND_BASE), 15, 16, false),
			
			new EPSpriteCE(ENTITY_WALK_STAND, "ENTITY_WALK_STAND", getSurfaceCM().getElement(EPSpriteSurfaceCM.ENTITY_BASE), 7, 7, false),
			new EPSpriteCE(ENTITY_WALK_UP, "ENTITY_WALK_UP", getSurfaceCM().getElement(EPSpriteSurfaceCM.ENTITY_BASE), 21, 27, true),
			new EPSpriteCE(ENTITY_WALK_DOWN, "ENTITY_WALK_DOWN", getSurfaceCM().getElement(EPSpriteSurfaceCM.ENTITY_BASE), 7, 13, true),
			new EPSpriteCE(ENTITY_WALK_LEFT, "ENTITY_WALK_LEFT", getSurfaceCM().getElement(EPSpriteSurfaceCM.ENTITY_BASE), 14, 20, true),
			new EPSpriteCE(ENTITY_WALK_RIGHT, "ENTITY_WALK_RIGHT", getSurfaceCM().getElement(EPSpriteSurfaceCM.ENTITY_BASE), 0, 6, true),
		};
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		EPSpriteCE[] ces = getSpriteToRegister();
		for (EPSpriteCE ce : ces)
			register(ce);
		AuraLogger.config(getAura().getSide(), ces.length + " sprites chargées");
	}
	
	public void reload() throws KeyContainerAlreadyUsed {
		getSurfaceCM().reload();
		EPSpriteCE[] ces = getSpriteToRegister();
		for (EPSpriteCE ce : ces)
			override(ce);
		AuraLogger.config(getAura().getSide(), ces.length + " sprites (re)chargées");
	}
}