package com.aura.engine.packet;

import com.aura.base.Aura;
import com.aura.base.manager.packet.PacketCE;
import com.aura.base.manager.packet.PacketCM;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.packet.PacketSimpleTask;

public class EPPacketCM<A extends Aura> extends PacketCM<A> {
	
	public static final int REQUEST_TOKEN = CURSEUR.nextVal();
	public static final int RESULT_TOKEN = CURSEUR.nextVal();
	
	public static final int REQUEST_WORLD = CURSEUR.nextVal();
	public static final int RESULT_WORLD = CURSEUR.nextVal();
	
	public static final int REQUEST_ENTITY = CURSEUR.nextVal();
	
	public static final int ENTITY_MOVE = CURSEUR.nextVal();
	
	public static final int ENTITY_REMOVE = CURSEUR.nextVal();
	
	public EPPacketCM(A aura) {
		super(aura);
	}
	public EPPacketCM(PacketCM<A> selfManager) {
		super(selfManager);
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		register(new PacketCE(REQUEST_TOKEN, "REQUEST_TOKEN") {@Override public AbstractPacket create(int id) {
			return new PacketSimpleTask(getId());}});
		register(new PacketCE(RESULT_TOKEN, "RESULT_TOKEN") {@Override public AbstractPacket create(int id) {
			return new EPPacketResultToken(getId());}});
		
		register(new PacketCE(REQUEST_WORLD, "REQUEST_WORLD") {@Override public AbstractPacket create(int id) {
			return new PacketSimpleTask(getId());}});
		register(new PacketCE(RESULT_WORLD, "RESULT_WORLD") {@Override public AbstractPacket create(int id) {
			return new EPPacketResultWorld(getId());}});
		
		register(new PacketCE(REQUEST_ENTITY, "REQUEST_ENTITY") {@Override public AbstractPacket create(int id) {
			return new PacketSimpleTask(getId());}});
		
		register(new PacketCE(ENTITY_MOVE, "ENTITY_MOVE") {@Override public AbstractPacket create(int id) {
			return new EPPacketEntityMove(getId());}});
		
		register(new PacketCE(ENTITY_REMOVE, "ENTITY_REMOVE") {@Override public AbstractPacket create(int id) {
			return new EPPacketEntityRemove(getId());}});
	}
}