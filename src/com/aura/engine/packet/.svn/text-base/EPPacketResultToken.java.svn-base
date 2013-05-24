package com.aura.engine.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public class EPPacketResultToken extends AbstractPacket {
	private Long tokenId;
	
	public EPPacketResultToken(int id) {
		super(id);
		
		this.tokenId = 0l;
	}
	
	public Long getTokenId() {
		return tokenId;
	}
	public void setTokenId(Long worldId) {
		this.tokenId = worldId;
	}
	
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			setTokenId(input.readLong());
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de lecture RESULT_TOKEN sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			output.writeLong(getTokenId());
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du RESULT_TOKEN [+" 
					+ getTokenId() + "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isValid() {
		return getTokenId() != null;
	}
}