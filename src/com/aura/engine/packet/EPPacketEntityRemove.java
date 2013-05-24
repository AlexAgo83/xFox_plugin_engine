package com.aura.engine.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public class EPPacketEntityRemove extends AbstractPacket {
	private Long drawableId;
	private Long tokenId;
	private Long worldId;
	
	public EPPacketEntityRemove(int id) {
		super(id);
		this.drawableId = 0l;
		this.tokenId = 0l;
		this.worldId = 0l;
	}
	
	public Long getDrawableId() {
		return drawableId;
	}
	public void setDrawableId(Long drawableId) {
		this.drawableId = drawableId;
	}
	
	public Long getTokenId() {
		return tokenId;
	}
	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}
	
	public Long getWorldId() {
		return worldId;
	}
	public void setWorldId(Long worldId) {
		this.worldId = worldId;
	}
	
	@Override
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			output.writeLong(getDrawableId());
			output.flush();
			output.writeLong(getTokenId());
			output.flush();
			output.writeLong(getWorldId());
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du ENTITY_REMOVE [+" 
					+ getTokenId() + "] sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			setDrawableId(input.readLong());
			setTokenId(input.readLong());
			setWorldId(input.readLong());
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de lecture ENTITY_REMOVE sur le stream du paquet ["
					+ this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isValid() {
		return getDrawableId() != null 
				&& getTokenId() != null 
				&& getWorldId() != null;
	}
}