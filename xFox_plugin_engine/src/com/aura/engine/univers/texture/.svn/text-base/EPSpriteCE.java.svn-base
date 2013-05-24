package com.aura.engine.univers.texture;

import com.aura.base.container.AbstractContainerElement;

public class EPSpriteCE extends AbstractContainerElement {
	private final EPSpriteSurfaceCE surface;
	private final int cursorStart;
	private final int cursorStop;
	private final boolean loop;
	
	public EPSpriteSurfaceCE getSurface() {
		return surface;
	}
	
	public EPSpriteCE(int id, String label, EPSpriteSurfaceCE surface, int cursorStart, int cursorStop, boolean loop) {
		super(id, label);

		this.surface = surface;
		this.cursorStart = cursorStart;
		this.cursorStop = cursorStop;
		this.loop = loop;
	}
	
	public Sprite create() {
		return new Sprite(
			surface.getSpriteSurface(), 
			cursorStart, cursorStop, 
			loop); 
	}
}