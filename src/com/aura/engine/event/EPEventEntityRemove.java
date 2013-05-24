package com.aura.engine.event;

import com.aura.base.event.AbstractEvent;

public class EPEventEntityRemove extends AbstractEvent {
	private Long drawableId;
	private Long tokenId;
	private Long worldId;
	
	public EPEventEntityRemove(int id) {
		super(id);
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
}