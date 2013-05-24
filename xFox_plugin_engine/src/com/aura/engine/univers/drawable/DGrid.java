package com.aura.engine.univers.drawable;

import java.awt.Color;

import com.aura.client.AuraClient;
import com.aura.engine.univers.DrawableGarbage;
import com.aura.engine.univers.texture.garbage.SGGrid;
import com.aura.engine.utils.Location;

public class DGrid extends AuraDrawable<AuraClient, SGGrid> {
	public DGrid(AuraClient aura, DrawableGarbage dg, Location loc, int size, SGGrid sprite) {
		super(aura, GENERER_FAKE_ID(), dg, "", loc, false, Color.DARK_GRAY, false, false, false, size, size, 0, sprite);
	}
}