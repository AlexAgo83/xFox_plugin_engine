package com.aura.engine.univers.texture.garbage;

import com.aura.engine.univers.texture.EPSpriteCM;
import com.aura.engine.utils.Orientation;

public class SGEntity extends SpriteGarbageAnimated<Orientation> {
	public SGEntity(EPSpriteCM spriteManager) {
		super(spriteManager);
		
		for (Orientation o: Orientation.values()) {
			switch (o) {
				case IDLE:
					registerSprite(o, getSpriteManager().getElement(EPSpriteCM.ENTITY_WALK_STAND_DOWN).create());
					break;
				case IDLE_NORD:
					registerSprite(o, getSpriteManager().getElement(EPSpriteCM.ENTITY_WALK_STAND_UP).create());
					break;
				case IDLE_OUEST:
					registerSprite(o, getSpriteManager().getElement(EPSpriteCM.ENTITY_WALK_STAND_LEFT).create());
					break;
				case IDLE_EST:
					registerSprite(o, getSpriteManager().getElement(EPSpriteCM.ENTITY_WALK_STAND_RIGHT).create());
					break;
				case NORD:
					registerSprite(o, getSpriteManager().getElement(EPSpriteCM.ENTITY_WALK_UP).create());
					break;
				case SUD:
					registerSprite(o, getSpriteManager().getElement(EPSpriteCM.ENTITY_WALK_DOWN).create());
					break;
				case OUEST:
				case NORD_OUEST:
				case SUD_OUEST:
					registerSprite(o, getSpriteManager().getElement(EPSpriteCM.ENTITY_WALK_LEFT).create());
					break;
				case EST:
				case NORD_EST:
				case SUD_EST:
					registerSprite(o, getSpriteManager().getElement(EPSpriteCM.ENTITY_WALK_RIGHT).create());
					break;
			}
		}
	}
	
	private Orientation lastOrientation;
	public Orientation getLastOrientation() {
		return lastOrientation;
	}
	
	@Override
	public void setFocus(Orientation o) {
		if (o == Orientation.IDLE) {
			if (getLastOrientation() != null) {
				// SURCHARGE IDLE
				switch (getLastOrientation()) {
					case NORD:
					case NORD_EST:
					case NORD_OUEST:
						o = Orientation.IDLE_NORD; 
						break;
					case SUD:
					case SUD_EST:
					case SUD_OUEST:
						o = Orientation.IDLE; 
						break;
					case OUEST:
						o = Orientation.IDLE_OUEST; 
						break;
					case EST:
						o = Orientation.IDLE_EST; 
						break;
				}
			}
		}
		lastOrientation = o;
		super.setFocus(o);
	}
}