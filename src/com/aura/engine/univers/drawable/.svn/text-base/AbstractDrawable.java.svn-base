package com.aura.engine.univers.drawable;

import java.awt.Color;

import com.aura.base.utils.Validate;
import com.aura.engine.AuraScreenInfo;
import com.aura.engine.univers.DrawableGarbage;
import com.aura.engine.univers.EntityToken;
import com.aura.engine.utils.Location;

public class AbstractDrawable {
	private static long ID_TRACKER = 1;
	public static long GENERER_ID() {
		long id = ID_TRACKER;
		ID_TRACKER += 1;
		return id;
	}
	
	private static long ID_FAKE_TRACKER = -1;
	public static long GENERER_FAKE_ID() {
		long id = ID_FAKE_TRACKER;
		ID_FAKE_TRACKER -= 1;
		return id;
	}
	
	private final long id;
	
	private final EntityToken token;
	private final String name;
	private final Location location;
	private final boolean centered;
	private final int width;
	private Color color;
	private final boolean plain;
	private final boolean allowHudMarker;
	private final boolean allowHudTarget;

	private boolean markAsRemove;
	
	private int currentX, currentY;
	private int currentXScaled, currentYScaled;
	
	protected AbstractDrawable(long id, DrawableGarbage dg, String name, Location loc, 
			boolean isCentered, Color color, boolean plain,
			boolean allowHudMarker, boolean allowHudTarget,
			int width) {

		this.id = id;
		this.token = dg.getToken();
		this.token.setDrawable(dg);
		this.name = name;
		this.location = loc;
		this.centered = isCentered;
		this.color = color;
		this.plain = plain;
		this.allowHudMarker = allowHudMarker;
		this.allowHudTarget = allowHudTarget;
		this.width = width;
		this.markAsRemove = false;
	}
	
	public Long getId() {
		return id;
	}
	public EntityToken getToken() {
		return token;
	}
	
	public String getName() {
		return name;
	}
	
	public int getWidth() {
		return width;
	}
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isPlain() {
		return plain;
	}
	
	public boolean isAllowHudMarker() {
		return allowHudMarker;
	}
	public boolean isAllowHudTarget() {
		return allowHudTarget;
	}
	
	public boolean isCentered() {
		return centered;
	}
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location loc) {
		getLocation().x = loc.x;
		getLocation().y = loc.y;
	}
	public void setLocation(int x, int y) {
		getLocation().x = x;
		getLocation().y = y;
	}
	
	public int getCurrentX() {
		return currentX;
	}
	protected void setCurrentScreenX(int currentX) {
		this.currentX = currentX;
	}
	
	public int getCurrentY() {
		return currentY;
	}
	protected void setCurrentScreenY(int currentY) {
		this.currentY = currentY;
	}
	
	public int getCurrentXScaled() {
		return currentXScaled;
	}
	public void setCurrentScreenXScaled(int currentXScaled) {
		this.currentXScaled = currentXScaled;
	}
	
	public int getCurrentYScaled() {
		return currentYScaled;
	}
	public void setCurrentScreenYScaled(int currentYScaled) {
		this.currentYScaled = currentYScaled;
	}
	
	protected double getScreenXPos(AuraScreenInfo info, Location loc, boolean adjustCenter, boolean scaled) {
		Validate.notNull(info);
		Validate.notNull(loc);
		try {
			return ((scaled ? loc.scale(info.zoom.getValue()) : loc).x 
				+ (scaled ? info.currentOrigine.scale(info.zoom.getValue()) : info.currentOrigine).x 
				+ info.frameWidth / 2) 
				- (adjustCenter && isCentered() ? 
					(scaled ? getWidth() * info.zoom.getValue() : getWidth()) / 2 : 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	protected double getScreenYPos(AuraScreenInfo info, Location loc,  boolean adjustCenter, boolean scaled) {
		Validate.notNull(info);
		Validate.notNull(loc);
		try {
			return ((scaled ? loc.scale(info.zoom.getValue()) : loc).y 
				+ (scaled ? info.currentOrigine.scale(info.zoom.getValue()) : info.currentOrigine).y 
				+ info.frameHeight / 2) 
				- (adjustCenter && isCentered() ? 
					(scaled ? getWidth() * info.zoom.getValue() : getWidth()) / 2 : 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean isMarkAsRemove() {
		return markAsRemove;
	}
	public void markAsRemove() {
		this.markAsRemove = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractDrawable other = (AbstractDrawable) obj;
		if (id != other.id)
			return false;
		return true;
	}
}