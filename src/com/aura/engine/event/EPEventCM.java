package com.aura.engine.event;

import com.aura.base.Aura;
import com.aura.base.event.AbstractEvent;
import com.aura.base.event.impl.DefaultEvent;
import com.aura.base.manager.event.EventCE;
import com.aura.base.manager.event.EventCM;

public class EPEventCM<A extends Aura> extends EventCM<A> {
	
	public final static int TOKEN_GRANTED = CURSEUR.nextVal();
	public final static int LAUNCH_ENGINE = CURSEUR.nextVal();
	
	public final static int KEY_PRESSED = CURSEUR.nextVal();
	public final static int KEY_RELEASED = CURSEUR.nextVal();
	
	public final static int MOUSE_PRESSED = CURSEUR.nextVal();
	public final static int MOUSE_RELEASED = CURSEUR.nextVal();

	public final static int ENTITY_MOVE = CURSEUR.nextVal();
	public final static int ENTITY_REMOVE = CURSEUR.nextVal();
	
	public final static int MODE_SAISIE = CURSEUR.nextVal();
	public final static int ZOOM_CHANGE = CURSEUR.nextVal();
	
	public final static int LAUNCH_SHELL = CURSEUR.nextVal();
	
	public EPEventCM(EventCM<A> selfManager) {
		super(selfManager);
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new EventCE(TOKEN_GRANTED, "TOKEN_GRANTED") {@Override public AbstractEvent create(int id) {return new EPEventTokenGranted(getId());}});
		register(new EventCE(LAUNCH_ENGINE, "LAUNCH_ENGINE") {@Override public AbstractEvent create(int id) {return new DefaultEvent(getId());}});
		
		register(new EventCE(KEY_PRESSED, "KEY_PRESSED") {@Override public AbstractEvent create(int id) {return new EPEventInputKey(getId());}});
		register(new EventCE(KEY_RELEASED, "KEY_RELEASED") {@Override public AbstractEvent create(int id) {return new EPEventInputKey(getId());}});
		
		register(new EventCE(MOUSE_PRESSED, "MOUSE_PRESSED") {@Override public AbstractEvent create(int id) {return new EPEventInputMouse(getId());}});
		register(new EventCE(MOUSE_RELEASED, "MOUSE_RELEASED") {@Override public AbstractEvent create(int id) {return new EPEventInputMouse(getId());}});
		
		register(new EventCE(ENTITY_MOVE, "ENTITY_MOVE") {@Override public AbstractEvent create(int id) {return new EPEventEntityMove(getId());}});
		register(new EventCE(ENTITY_REMOVE, "ENTITY_REMOVE") {@Override public AbstractEvent create(int id) {return new EPEventEntityRemove(getId());}});
		
		register(new EventCE(MODE_SAISIE, "MODE_SAISIE") {@Override public AbstractEvent create(int id) {return new EPEventModeSaisie(getId());}});
		register(new EventCE(ZOOM_CHANGE, "ZOOM_CHANGE") {@Override public AbstractEvent create(int id) {return new EPEventZoomChange(getId());}});
		
		register(new EventCE(LAUNCH_SHELL, "LAUNCH_SHELL") {@Override public AbstractEvent create(int id) {return new EPEventLaunchShell(getId());}});
	}
}