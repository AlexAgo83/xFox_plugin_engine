package com.aura.engine.event;

public class EPEventInputKey extends EPEventInput {
	private char keyChar;
	
	public EPEventInputKey(int id) {
		super(id);
	}
	
	public char getKeyChar() {
		return keyChar;
	}
	public void setKeyChar(char keyChar) {
		this.keyChar = keyChar;
	}
}