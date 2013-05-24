package com.aura.engine.univers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aura.engine.univers.drawable.AbstractDrawable;

public class DrawableGarbage {
	private final EntityToken token;
	private final Map<Long, AbstractDrawable> drawables;
	
	public DrawableGarbage(EntityToken token) {
		this.token = token;
		this.drawables = new HashMap<Long, AbstractDrawable>();
	}
	
	public EntityToken getToken() {
		return token;
	}
	
	private synchronized Map<Long, AbstractDrawable> getDrawables() {
		return drawables;
	}
	
	public synchronized Long[] getIdTab() {
		return getDrawables().keySet().toArray(new Long[0]);
	}
	public synchronized AbstractDrawable[] getDrawableTab() {
		List<AbstractDrawable> dws = new ArrayList<AbstractDrawable>();
		Long[] tab = getIdTab();
		for (Long id: tab)
			dws.add(getById(id));
		return dws.toArray(new AbstractDrawable[0]);
	}
	
	public AbstractDrawable getById(long id) {
		return getDrawables().get(id);
	}

	public void clear() {
		getDrawables().clear();
	}
	
	public void add(AbstractDrawable d) {
		getDrawables().put(d.getId(), d);
	}
	public void rmv(AbstractDrawable d) {
		getDrawables().remove(d.getId());
	}
}