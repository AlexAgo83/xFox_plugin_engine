package com.aura.engine.univers.texture;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class SpriteSurface {
	private final BufferedImage[] cells;
	
	public SpriteSurface(BufferedImage image) {
		this(image, 1, 1);
	}
	public SpriteSurface(BufferedImage image, int xCell, int yCell) {
		this(image, xCell, yCell, 100, 100, SpriteScaleFilter.NONE);
	}
	public SpriteSurface(BufferedImage image, int xCell, int yCell, int xPcScale, int yPcScale, SpriteScaleFilter filter) {
		xCell = xCell < 1 ? 1 : xCell;
		yCell = yCell < 1 ? 1 : yCell;

		image = appliquerScale(image, xPcScale, yPcScale, filter);
		
		cells = new BufferedImage[xCell*yCell];
		int cursor = 0;
		if (xCell > 1 || yCell > 1) {
			for (int y=0; y<yCell; y++) {
				for (int x=0; x<xCell; x++) {
					cells[cursor] = image.getSubimage(
						x * (image.getWidth() / xCell), 
						y * (image.getHeight() / yCell),
						(image.getWidth() / xCell), 
						(image.getHeight() / yCell));
					cursor += 1;
				}
			}
		} else {
			cells[cursor] = image;
		}
	}
	
	public BufferedImage getImage(int cursor) {
		return cells[cursor];
	}
	
	public int getMinCursor() {
		return 0;
	}
	public int getMaxCursor() {
		return cells.length-1;
	}
	
	public static BufferedImage appliquerScale(BufferedImage image, int xPcScale, int yPcScale, SpriteScaleFilter filter) {
		if (xPcScale == 100 || yPcScale == 100) {
			return image;
		}
		
		AffineTransform tx = AffineTransform.getScaleInstance(xPcScale / 100.0, yPcScale / 100.0);
		AffineTransformOp op = null;
		
		switch (filter) {
			case BILINEAR :
				op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
				break;
			case BICUBIC :
				op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
				break;
			case NONE :
				op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		}
		
		return op.filter(image, null);
	}
}