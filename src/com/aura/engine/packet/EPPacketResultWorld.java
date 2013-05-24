package com.aura.engine.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;

public class EPPacketResultWorld extends AbstractPacket {
	private Long worldId;
	private Integer width;
	private Integer height;
	
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
	
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	
	@Override
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			setWorldId(input.readLong());
			setWidth(input.readInt());
			setHeight(input.readInt());
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
			output.writeInt(getWidth());
			output.flush();
			output.writeInt(getHeight());
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
		return getWorldId() != null && getWidth() != null && getHeight() != null;
	}
}