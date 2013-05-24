package com.aura.engine.univers;

import com.aura.base.Aura;
import com.aura.engine.univers.world.World;

public abstract class Univers<A extends Aura, W extends World<A, ?>> {
	private final A aura;
	public A getAura() {
		return aura;
	}
	
	public Univers(A aura) {
		this.aura = aura;
	}
}