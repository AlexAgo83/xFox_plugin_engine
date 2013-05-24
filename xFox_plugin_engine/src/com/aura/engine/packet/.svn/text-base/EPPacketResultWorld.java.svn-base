package com.aura.engine.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public class EPPacketResultWorld extends AbstractPacket {
	private Long worldId;
	
	public EPPacketResultWorld(int id) {
		super(id);
		
		this.worldId = 0l;
	}
	
	public Long getWorldId() {
		return worldId;
	}
	public void setWorldId(Long worldId) {
		this.worldId = worldId;
	}
	
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			setWorldId(input.readLong());
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de lecture RESULT_WORLD sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			output.writeLong(getWorldId());
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du RESULT_WORLD [+" 
					+ getWorldId() + "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isValid() {
		return getWorldId() != null;
	}
}