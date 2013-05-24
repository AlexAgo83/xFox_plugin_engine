package com.aura.engine.module;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.event.EPEventInputKey;
import com.aura.engine.event.EPEventInputMouse;
import com.aura.engine.inputMap.EPMapCE;
import com.aura.engine.univers.DrawableGarbage;
import com.aura.engine.univers.EntityToken;
import com.aura.engine.univers.EntityTokenClient;
import com.aura.engine.univers.drawable.DGrid;
import com.aura.engine.univers.texture.garbage.SGGrid;
import com.aura.engine.univers.texture.garbage.TypeGrid;
import com.aura.engine.utils.Location;

public class EMWorld extends AuraEngineModule {
	private final int size = 64;
	private final int maxX;
	private final int maxY;

	private DGrid[][] grid;
	private EntityTokenClient fakeToken;
	
	public EMWorld(AuraClient aura, AuraScreen screen, AuraEngine scene) {
		super(aura, screen, scene);
		
		this.fakeToken = new EntityTokenClient(EntityToken.GENERER_FAKE_ID());
		
		float ratio = 13f / 8f;
		this.maxX = (int) (screen.getView().getWidth() * ratio) / size;
		this.maxY = (int) (screen.getView().getHeight() * ratio) / size;
		
		List<DGrid[]> tab = new ArrayList<DGrid[]>();
		for (int y=0; y<maxY; y++) {
			List<DGrid> lst = new ArrayList<DGrid>();
			for (int x=0; x<maxX; x++) {
				DrawableGarbage fakeDg = new DrawableGarbage(fakeToken);
				SGGrid sgGrid = new SGGrid(getScreen().getPlugin().getSpriteCM());
				DGrid d = new DGrid(aura, fakeDg, new Location(x*size, y*size), size, sgGrid);
				lst.add(d);
				getDrawableQueueManager().register(d.getQueueItem());
				
			}	
			tab.add(lst.toArray(new DGrid[0]));
		}
		grid = tab.toArray(new DGrid[0][]);
	}
	
	public DGrid[][] getGrid() {
		return grid;
	}
	
	@Override public void doUpdate() {
		
		int xRatio = (int) Math.floor(getScene().getScreenInfo().currentOrigine.x / size) * size;
		int yRatio = (int) Math.floor(getScene().getScreenInfo().currentOrigine.y / size) * size;
		
		for (int y=0; y<maxY; y++) {
			for (int x=0; x<maxX; x++) {
				final DGrid g = getGrid()[y][x];

				g.getLocation().x = (x * size) - (maxX /2 * size) - xRatio;
				g.getLocation().y = (y * size) - (maxY /2 * size) - yRatio;
				
				float val = getUnivers().getFocusWorld().getValueTest(
					(int) g.getLocation().x, 
					(int) g.getLocation().y);
				g.setColor(new Color(0, 0, val/2));

				TypeGrid t = TypeGrid.parsePourcent(val);
				g.getSpriteGarbage().pause();
				g.getSpriteGarbage().setFocus(t);
				switch (t) {
				 	case FLOOR_CLEAR:
				 		g.getSprite().forceCursor(0);
				 		break;
				 	case FLOOR_SHELL_1:
				 		g.getSprite().forceCursor(1);
				 		break;
				 	case FLOOR_SHELL_2:
				 		g.getSprite().forceCursor(4);
				 		break;
				 	case FLOOR_TUBE_1:
				 		g.getSprite().forceCursor(2);
				 		break;
				 	case FLOOR_TUBE_2:
				 		g.getSprite().forceCursor(3);
				 		break;
				 	case MACHINE:
				 		g.getSpriteGarbage().play(getAura(), 100);
				 		break;
				 	case VENTIL:
						g.getSpriteGarbage().play(getAura(), 100);
				 		break;
				}
			}
		}
	}
	
	@Override public void doDraw(Graphics2D g) {}
	@Override public void doDrawDebug(List<String> msg) {}
	
	@Override public void doKeyPressed(EPEventInputKey e, EPMapCE map) {}
	@Override public void doKeyReleased(EPEventInputKey e, EPMapCE map) {}
	
	@Override public void doMousePressed(EPEventInputMouse e, EPMapCE map) {}
	@Override public void doMouseReleased(EPEventInputMouse e, EPMapCE map) {}
}