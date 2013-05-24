package com.aura.engine.univers.drawable;

import com.aura.base.Aura;

public class DrawableQueueItem<A extends Aura> {
	private final AuraDrawable<A, ?> drawable;
	private final int depth;
	
	public DrawableQueueItem(final AuraDrawable<A, ?> drawable, final int depth) {
		this.drawable = drawable;
		this.depth = depth;
	}
	
	public AuraDrawable<A, ?> getDrawable() {
		return drawable;
	}
	public int getDepth() {
		return depth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((drawable == null) ? 0 : drawable.hashCode());
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
		DrawableQueueItem<?> other = (DrawableQueueItem<?>) obj;
		if (drawable == null) {
			if (other.drawable != null)
				return false;
		} else if (!drawable.equals(other.drawable))
			return false;
		return true;
	}
}