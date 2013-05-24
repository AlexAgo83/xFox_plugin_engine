package com.aura.engine.univers.texture.garbage;

import com.aura.engine.univers.texture.EPSpriteCM;

public class SGGrid extends SpriteGarbageAnimated<TypeGrid> {
	public SGGrid(EPSpriteCM spriteManager) {
		super(spriteManager);
		registerSprite(TypeGrid.FLOOR_CLEAR, getSpriteManager().getElement(EPSpriteCM.GROUND_ALL).create());
		registerSprite(TypeGrid.FLOOR_SHELL_1, getSpriteManager().getElement(EPSpriteCM.GROUND_ALL).create());
		registerSprite(TypeGrid.FLOOR_SHELL_2, getSpriteManager().getElement(EPSpriteCM.GROUND_ALL).create());
		registerSprite(TypeGrid.FLOOR_TUBE_1, getSpriteManager().getElement(EPSpriteCM.GROUND_ALL).create());
		registerSprite(TypeGrid.FLOOR_TUBE_2, getSpriteManager().getElement(EPSpriteCM.GROUND_ALL).create());
		
		registerSprite(TypeGrid.VENTIL, getSpriteManager().getElement(EPSpriteCM.GROUND_VENTIL).create());
		registerSprite(TypeGrid.MACHINE, getSpriteManager().getElement(EPSpriteCM.GROUND_MACHINE).create());
		
		setFocus(TypeGrid.FLOOR_CLEAR);
	}
}