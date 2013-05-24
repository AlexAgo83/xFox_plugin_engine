package com.aura.engine;

public enum EngineZoom {
	PC_DEFAULT(1),
	PC_105(1.05f),
	PC_110(1.1f),
	PC_120(1.2f),
	PC_130(1.3f),
	PC_140(1.4f),
	PC_150(1.5f),
	PC_160(1.6f),
	PC_170(1.7f),
	PC_180(1.8f),
	PC_190(1.9f),
	PC_200(2.0f);
	
	private final float value;
	
	private EngineZoom(float value) {
		this.value = value;
	}
	public float getValue() {
		return value;
	}
	public float getInverse() {
		return 1 / getValue();
	}
	
	public EngineZoom next() {
		EngineZoom[] tab = EngineZoom.values();
		EngineZoom toReturn = this;
		for (int i=0; i<tab.length; i++) { 
			if (tab[i] == this) {
				if (i+1 < tab.length)
					toReturn = tab[i+1];
				break;
			}
		}
		return toReturn;
	}
	
	public EngineZoom prev() {
		EngineZoom[] tab = EngineZoom.values();
		EngineZoom toReturn = this;
		for (int i=0; i<tab.length; i++) { 
			if (tab[i] == this) {
				if (i-1 >= 0)
					toReturn = tab[i-1];
				break;
			}
		}
		return toReturn;
	}
}