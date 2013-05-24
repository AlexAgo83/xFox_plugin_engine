package com.aura.engine.univers.world;

import java.util.ArrayList;
import java.util.List;

import com.aura.client.AuraClient;
import com.aura.engine.univers.DrawableGarbage;
import com.aura.engine.univers.EntityToken;
import com.aura.engine.univers.UniversClient;
import com.aura.engine.univers.drawable.AbstractDrawable;
import com.aura.engine.utils.Location;

public class WorldClient extends World<AuraClient, UniversClient> {
	private final Location cameraPosition = new Location(0,0);
	public Location getCameraPosition() {
		return cameraPosition;
	}
	
	private final List<AbstractDrawable> selection = new ArrayList<AbstractDrawable>();
	private synchronized List<AbstractDrawable> getSelection() {
		return selection;
	}
	
	public WorldClient(AuraClient client, UniversClient univers) {
		super(client, univers);
	}
	public WorldClient(AuraClient client, UniversClient univers, Long id) {
		super(client, univers, id);
	}
	
	@Override
	public void removeDrawable(AbstractDrawable d) {
		super.removeDrawable(d);
		if (isSelectionContains(d))
			rmvSelection(d);
	}
	
	public void clear() {
		for (EntityToken t: getEntitiesTab()) {
			DrawableGarbage dg = getEntity(t);
			for (AbstractDrawable d: dg.getDrawableTab()) {
				d.markAsRemove();
				removeDrawable(d);
			}
			dg.clear();
		}
	}
	
	public boolean isSelectionContains(AbstractDrawable d) {
		return getSelection().contains(d);
	}
	public void pushSelection(List<AbstractDrawable> dw, boolean avecClear) {
		if (avecClear)
			getSelection().clear();
		getSelection().addAll(dw);
	}
	public void rmvSelection(AbstractDrawable d) {
		getSelection().remove(d);
	}
	public AbstractDrawable[] getDrawableSelectionTab() {
		return getSelection().toArray(new AbstractDrawable[0]);
	}
}