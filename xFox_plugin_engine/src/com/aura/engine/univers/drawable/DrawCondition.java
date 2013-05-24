package com.aura.engine.univers.drawable;

public class DrawCondition {
	public boolean draw = false;
	public boolean marquer = false;
	public boolean target = false;
	
	public boolean isAllowed() {
		return draw || marquer || target;
	}
}