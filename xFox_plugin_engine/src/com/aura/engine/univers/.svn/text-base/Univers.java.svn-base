package com.aura.engine.univers;

import com.aura.base.Aura;
import com.aura.engine.univers.world.World;

public abstract class Univers<A extends Aura, W extends World<A, ?>> {
	private final A aura;
	public A getAura() {
		return aura;
	}
	
	private W defaultWorld;
	public W getDefaultWorld() {
		if (defaultWorld == null)
			defaultWorld = initDefaultWorld();
		return defaultWorld;
	}
	
	public Univers(A aura) {
		this.aura = aura;
	}
	
	public abstract W initDefaultWorld();
}