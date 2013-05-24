package com.aura.engine.packet;

public enum TypeEntityElementMove {
	ORIENTATION(0),
	TARGET(1);
	private int id;
	private TypeEntityElementMove(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public static TypeEntityElementMove parseInt(int i) {
		for (TypeEntityElementMove me: TypeEntityElementMove.values())
			if (me.getId() == i)
				return me;
		return null;
	}
}
