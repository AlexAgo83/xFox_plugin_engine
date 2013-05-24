package com.aura.engine.univers.texture.garbage;

public enum TypeGrid {
	FLOOR_CLEAR(.50),
	FLOOR_SHELL_1(.30),
	FLOOR_SHELL_2(.10),
	FLOOR_TUBE_1(.07),
	FLOOR_TUBE_2(.05),
	MACHINE(.02),
	VENTIL(.00);
	
	private double pc;
	private TypeGrid(double pc) {
		this.pc = pc;
	}
	public double getPourcent() {
		return pc;
	}
	
	public static TypeGrid parsePourcent(double pc) {
		TypeGrid focus = null;
		for (TypeGrid t: TypeGrid.values()) {
			if (t.getPourcent() <= pc && (focus == null || t.getPourcent() >= focus.getPourcent()))
				focus = t;
		}
		return focus;
	}
}