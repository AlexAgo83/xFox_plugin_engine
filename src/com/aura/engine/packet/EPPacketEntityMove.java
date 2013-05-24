package com.aura.engine.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public class EPPacketEntityMove extends AbstractPacket {
	private long count;
	private boolean poolMode;
	private List<EntityElementMove> entityElementMoves;
	
	public EPPacketEntityMove(int id) {
		super(id);
		this.count = 0;
		this.poolMode = false;
		this.entityElementMoves = new ArrayList<EntityElementMove>();
	}
	
	public void attachElements(EntityElementMove me) {
		this.entityElementMoves.add(me);
		setCount(entityElementMoves.size());
	}
	public EntityElementMove[] getElements() {
		return this.entityElementMoves.toArray(new EntityElementMove[0]);
	}
	public List<EntityElementMove> getCurrentElements() {
		return Collections.unmodifiableList(entityElementMoves);
	}
	
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	public boolean isPoolMode() {
		return poolMode;
	}
	public void setPoolMode(boolean poolMode) {
		this.poolMode = poolMode;
	}
	
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		boolean lResult = true;
		try {
			output.writeLong(getCount());
			output.flush();
			output.writeBoolean(isPoolMode());
			output.flush();
			
			for (EntityElementMove m: entityElementMoves) {
				output.writeInt(m.getMoveElementType().getId());
				output.flush();
				if (!m.implWrite(aura, output)) {
					lResult = false;
					break;
				}
			}
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du ENTITY_MOVE_<???> sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return lResult;
	}
	
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		boolean lResult = true;
		try {
			long count = input.readLong(); // ne pas set directement , attachEl.. le fait tout seul
			setPoolMode(input.readBoolean());
			for (int i=1; i<=count; i++) {
				TypeEntityElementMove t = TypeEntityElementMove.parseInt(input.readInt());
				EntityElementMove me = null;
				switch (t) {
					case ORIENTATION : me = new EntityElementMoveOrientation(); break;
					case TARGET : me = new EntityElementMoveTarget(); break;
				}
				if (!me.implRead(aura, input)) {
					lResult = false;
					break;
				}
				attachElements(me);
			}
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de lecture du ENTITY_MOVE_<???> sur le stream du paquet [" + this.getClass() + "].", e);
			return false;
		}
		return lResult;
	}
	
	@Override
	public boolean isValid() {
		boolean lResult = true;
		for (EntityElementMove m: entityElementMoves) {
			if (!m.isValid()) {
				lResult = false;
				break;
			}
		}
		return lResult;
	}
}