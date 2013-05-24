package com.aura.engine.univers.texture;

import com.aura.base.container.AbstractContainerElement;

public class EPSpriteSurfaceCE extends AbstractContainerElement {
	private final String path;
	private final String fileName;
	private final int xCell;
	private final int yCell;
	private final int xPcScale;
	private final int yPcScale;
	private final SpriteScaleFilter filter;
	private final EPSpriteSurfaceCM selfManager; 
	
	public EPSpriteSurfaceCE(int id, String label, String path, String fileName, int xCell, int yCell, int xPcScale, int yPcScale, SpriteScaleFilter filter, EPSpriteSurfaceCM selfManager) {
		super(id, label);
		
		this.path = path;
		this.fileName = fileName;
		
		this.xCell = xCell;
		this.yCell = yCell;
		
		this.xPcScale = xPcScale;
		this.yPcScale = yPcScale;
		
		this.filter = filter;
		
		this.selfManager = selfManager;
	}

	private SpriteSurface surface;
	public SpriteSurface getSpriteSurface() {
		if (surface == null) 
			surface = new SpriteSurface(
				selfManager.loadImage(path, fileName),
				xCell, yCell,
				xPcScale, yPcScale, filter);
		return surface;
	}
}