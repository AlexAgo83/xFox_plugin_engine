package com.aura.engine.univers.texture.garbage;

import com.aura.engine.univers.texture.EPSpriteCM;
import com.aura.engine.utils.Orientation;

public class SGEntity extends SpriteGarbageAnimated<Orientation> {
	public SGEntity(EPSpriteCM spriteManager) {
		super(spriteManager);
		
		for (Orientation o: Orientation.values()) {
			switch (o) {
				case IDLE:
					registerSprite(o, getSpriteManager().getElement(EPSpriteCM.ENTITY_WALK_STAND).create());
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
}