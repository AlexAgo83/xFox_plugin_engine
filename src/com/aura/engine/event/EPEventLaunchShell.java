package com.aura.engine.event;

import com.aura.base.event.AbstractEvent;
import com.aura.engine.module.shell.TypeShell;

public class EPEventLaunchShell extends AbstractEvent {
	private TypeShell shell;
	
	public EPEventLaunchShell(int id) {
		super(id);
	}
	
	public TypeShell getShell() {
		return shell;
	}
	public void setShell(TypeShell shell) {
		this.shell = shell;
	}
}
