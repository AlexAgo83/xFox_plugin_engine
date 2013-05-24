package com.aura.engine.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.aura.base.Aura;
import com.aura.base.utils.AuraLogger;

public abstract class EntityElement {
	private Long drawableId;
	private Long tokenId;
	private Long worldId;
	
	public EntityElement() {
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
	
	protected boolean implWrite(Aura aura, DataOutputStream output) {
		try {
			String stream = ""+getDrawableId()+"#"+getTokenId()+"#"+getWorldId();
			stream += complementWrite(aura, output);
			output.writeUTF(stream);
			output.flush();
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de l'écriture du ENTITY_ELEMENT sur le stream du paquet [" + this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	public abstract String complementWrite(Aura aura, DataOutputStream output) throws IOException;
	
	protected boolean implRead(Aura aura, DataInputStream input) {
		try {
			String[] split = input.readUTF().split("#");
			setDrawableId(Long.parseLong(split[0]));
			setTokenId(Long.parseLong(split[1]));
			setWorldId(Long.parseLong(split[2]));
			complementRead(aura, split);
		} catch (IOException e) {
			AuraLogger.severe(aura.getSide(), "Echec de lecture ENTITY_ELEMENT [" + this.getClass() + "].", e);
			return false;
		}
		return true;
	}
	public abstract void complementRead(Aura aura, String[] split) throws IOException;

	public boolean isValid() {
		return getDrawableId() != null
		&& getTokenId() != null 
		&& getWorldId() != null
		&& complementValid();
	}
	public abstract boolean complementValid();
}