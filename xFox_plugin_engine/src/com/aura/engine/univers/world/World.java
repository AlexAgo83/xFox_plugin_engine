package com.aura.engine.univers.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.aura.base.Aura;
import com.aura.base.utils.AuraLogger;
import com.aura.base.utils.Validate;
import com.aura.engine.univers.DrawableGarbage;
import com.aura.engine.univers.EntityToken;
import com.aura.engine.univers.Univers;
import com.aura.engine.univers.drawable.AbstractDrawable;

public class World<A extends Aura, U extends Univers<A, ?>> {
	private final U univers;
	public U getUnivers() {
		return univers;
	}
	
	private static Long ID_GEN = 0l;
	public static Long GENERER_FAKE_ID() {
		Long newId = ID_GEN;
		ID_GEN -= 1l;
		return newId;
	}
	
	private final A aura;
	public A getAura() {
		return aura;
	}
	
	private final Long id;
	public Long getId() {
		return id;
	}
	
	private Map<EntityToken, DrawableGarbage> entities;
	private synchronized Map<EntityToken, DrawableGarbage> getEntities() {
		if (entities == null) 
			entities = new HashMap<EntityToken, DrawableGarbage>();
		return entities;
	}
	public EntityToken[] getEntitiesTab() {
		return getEntities().keySet().toArray(new EntityToken[0]);
	}
	public DrawableGarbage getEntity(EntityToken e) {
		return getEntities().get(e);
	}
	public EntityToken fetchToken(EntityToken e) {
		Validate.notNull(e);
		Validate.notNull(getEntities().get(e));
		return getEntities().get(e).getToken();
	}
	public EntityToken getEntityById(long id) {
		EntityToken[] tab = getEntitiesTab();
		for (EntityToken t: tab) {
			if (t.getId() == id)
				return t;
		}
		return null;
	}
	
	private boolean allowAttach;
	public boolean isAllowAttach() {
		return allowAttach;
	}
	public void setAllowAttach(boolean allowAttach) {
		this.allowAttach = allowAttach;
	}
	
	public int getEntitySize() {
		return getEntities().size();
	}
	public DrawableGarbage init(EntityToken token) {
		Validate.notNull(token);
		if (getEntities().get(token) == null) {
			if (token.getDrawable() == null) {
				DrawableGarbage dg = new DrawableGarbage(token);
				token.setDrawable(dg);
			}
			getEntities().put(token, token.getDrawable());
		}
		return getEntities().get(token);
	}
	public void attach(AbstractDrawable d) {
		Validate.notNull(d);
		Validate.notNull(d.getToken());
		if (isAllowAttach()) {
			init(d.getToken());
			getEntities().get(d.getToken()).add(d);
		} else
			AuraLogger.info(getAura().getSide(), "Entit� refus�e le monde est entrain de se recycler.");
	}
	
	public void removeDrawable(AbstractDrawable d) {
		Validate.notNull(d);
		Validate.notNull(d.getToken());
		getEntities().get(d.getToken()).rmv(d);
		if (getEntities().get(d.getToken()).getIdTab().length == 0)
			removeAllByToken(d.getToken());
		d.markAsRemove();
	}
	
	public void removeAllByToken(EntityToken token) {
		Validate.notNull(token);
		getEntities().remove(token);
	}
	
	public World(A aura, U univers) {
		this.aura = aura;
		this.id = GENERER_FAKE_ID();
		this.univers = univers;
		this.allowAttach = true;
	}
	public World(A aura, U univers, Long id) {
		this.aura = aura;
		this.id = id;
		this.univers = univers;
		this.allowAttach = true;
	}
	
	private Random rand = new Random();
	public Float getValueTest(int x, int y) {
		rand.setSeed(("ID:"+getId()+"#"+x+"#"+y).hashCode());
		return rand.nextFloat();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		World<?,?> other = (World<?,?>) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}