package com.aura.engine.console;

import com.aura.base.Aura;
import com.aura.base.TypeSide;
import com.aura.base.manager.console.ConsoleCE;
import com.aura.base.manager.console.ConsoleCM;

public class EPConsoleCM<A extends Aura> extends ConsoleCM<A> {
	
	public final static int ADD_WORLD = CURSEUR.nextVal();
	public final static int LIST_WORLD = CURSEUR.nextVal();
	public final static int TP_PLAYER_TO = CURSEUR.nextVal();
	public final static int SPAWN = CURSEUR.nextVal();
	
	public EPConsoleCM(ConsoleCM<A> selfManager) {
		super(selfManager);
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new ConsoleCE(ADD_WORLD, "ADD_WORLD", "addworld", TypeSide.SERVER, 2));
		register(new ConsoleCE(LIST_WORLD, "LIST_WORLD", "listworld", TypeSide.SERVER, 1));
		register(new ConsoleCE(TP_PLAYER_TO, "TP_PLAYER_TO", "teleport", TypeSide.SERVER, 3));
		register(new ConsoleCE(SPAWN, "SPAWN", "spawn", TypeSide.CLIENT, 2));
	}
}