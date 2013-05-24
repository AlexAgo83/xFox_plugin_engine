package com.aura.engine.inputMap;

import com.aura.base.container.AbstractContainerElement;

public class EPMapCE extends AbstractContainerElement {
	private final int configId;
	private int code;
	
	public EPMapCE(int id, String label, int configId) {
		super(id, label);
		this.configId = configId;
	}
	
	public int getConfigId() {
		return configId;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
}