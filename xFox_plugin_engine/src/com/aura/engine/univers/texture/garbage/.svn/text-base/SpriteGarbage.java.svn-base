package com.aura.engine.univers.texture.garbage;

import java.util.HashMap;
import java.util.Map;

import com.aura.engine.univers.texture.EPSpriteCM;
import com.aura.engine.univers.texture.Sprite;

public class SpriteGarbage<E> {
	private Sprite focus;
	
	public Sprite getFocus() {
		return focus;
	}
	public void setFocus(E e) {
		this.focus = getSpriteBy(e);
	}
	
	private Map<E, Sprite> sprites;
	private synchronized Map<E, Sprite> getSprites() {
		if (sprites == null)
			sprites = new HashMap<E, Sprite>();
		return sprites;
	}
	public Sprite getSpriteBy(E e) {
		return getSprites().get(e);
	}
	public void registerSprite(E e, Sprite sprite) {
		getSprites().put(e, sprite);
	}

	private final EPSpriteCM spriteManager;
	public EPSpriteCM getSpriteManager() {
		return spriteManager;
	}
	
	public SpriteGarbage(EPSpriteCM spriteManager) {
		this.spriteManager = spriteManager;
	}
}