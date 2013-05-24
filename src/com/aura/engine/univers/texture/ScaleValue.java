package com.aura.engine.univers.texture;

public class ScaleValue {
	public static ScaleValue PC100 = new ScaleValue(100, 100);
	public static ScaleValue PC50 = new ScaleValue(50, 50);
	
	int x, y;
	public ScaleValue(int x, int y) {
		this.x = x; this.y = y;
	}
}
