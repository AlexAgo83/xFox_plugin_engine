package com.aura.engine.univers;


public abstract class EntityToken {
	private static long ID_TRACKER = 1;
	public static long GENERER_ID() {
		long id = ID_TRACKER;
		ID_TRACKER+=1;
		return id;
	}
	
	private static long ID_FAKE_TRACKER = -1;
	public static long GENERER_FAKE_ID() {
		long id = ID_FAKE_TRACKER;
		ID_FAKE_TRACKER-=1;
		return id;
	}

	private final long id;
	private DrawableGarbage drawable;
	
	public EntityToken(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	public DrawableGarbage getDrawable() {
		if (drawable == null) {
			drawable = new DrawableGarbage(this);
		}
		return drawable;
	}
	public void setDrawable(DrawableGarbage drawable) {
		this.drawable = drawable;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		EntityToken other = (EntityToken) obj;
		if (id != other.id)
			return false;
		return true;
	}
}