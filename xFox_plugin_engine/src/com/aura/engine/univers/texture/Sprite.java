package com.aura.engine.univers.texture;

import java.awt.Graphics2D;

import com.aura.engine.AuraEngine;

public class Sprite {
	// FIXME XF(AA) voir http://www.zetcode.com/gfx/java2d/java2dimages/
	private final SpriteSurface surface;
	
	private final int cursorStart;
	private final int cursorStop;
	
	private int mainCursor;
	
	private SpriteAnimationState state;
	private final boolean loop;
	
	public Sprite(SpriteSurface surface) {
		this(surface, surface.getMinCursor());
	}
	public Sprite(SpriteSurface surface, int cursor) {
		this(surface, cursor, cursor, false);
	}
	public Sprite(SpriteSurface surface, int cursorStart, int cursorStop, boolean loop) {
		this.surface = surface;
		this.cursorStart = cursorStart;
		this.cursorStop = cursorStop;
		this.state = SpriteAnimationState.READY;
		this.loop = loop;
		
		this.mainCursor = cursorStart;
	}
	
	public void draw(AuraEngine scene, Graphics2D g, int x, int y) {
		if (scene.isShowSprite())
			g.drawImage(surface.getImage(mainCursor), x, y, null);	
	}
	public void draw(AuraEngine scene, Graphics2D g, int x, int y, int width, int height) {
		if (scene.isShowSprite())
			g.drawImage(surface.getImage(mainCursor), x, y, width, height, null);	
	}
	
	public void next() {
		if (state == SpriteAnimationState.READY 
				|| state == SpriteAnimationState.ANIMATING) { 
			if (mainCursor + 1 <= cursorStop) {
				mainCursor = mainCursor + 1;
			} else if (!loop) {
				state = SpriteAnimationState.FINISHED;
			} else {
				mainCursor = cursorStart;
			}
		}
	}
	public void prev() {
		if (state == SpriteAnimationState.READY 
				|| state == SpriteAnimationState.ANIMATING) { 
			if (mainCursor - 1 >= cursorStart) {
				mainCursor = mainCursor - 1;
			} else if (!loop) {
				state = SpriteAnimationState.FINISHED;
			} else {
				mainCursor = cursorStop;
			}
		}
	}
	
	public void forceCursor(int i) {
		if (mainCursor > cursorStop || mainCursor < cursorStart)
			throw new RuntimeException("cursor invalide");
		mainCursor = i;
	}
}