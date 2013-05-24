package com.aura.engine.module.object;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.aura.base.utils.Validate;
import com.aura.engine.module.shell.AbstractShell;

public class EMObjectClickable extends EMObject {
	private final List<EMObjectAction> actions;
	
	public EMObjectClickable(AbstractShell shell, String text, int w, int h) {
		super(shell, text, w, h);
		this.actions = new ArrayList<EMObjectAction>();
	}

	public boolean intersect(int xcur, int ycur) {
		return xcur >= getX()
				&& xcur <= getX()+getW()
				&& ycur >= getY()
				&& ycur <= getY()+getH();
	}
	
	public void attach(EMObjectAction action) {
		Validate.notNull(action);
		actions.add(action);
	}
	
	@Override
	protected void drawBorder(Graphics2D g) {
		super.drawBorder(g);
		if (this.equals(getShell().getFocusClickable())) {
			g.setColor(currentUIColor.brighter());
			if (!getShell().getScene().isLowQuality() && currentPinned)
				getShell().getScene().setComposite(g, .25f);
			else
				getShell().getScene().setComposite(g, currentComposite);
			g.drawRect(getX(), getY(), getW(), getH());
			g.drawRect(getX()-1, getY()-1, getW()+2, getH()+2);
		}
	}
	
	public void actionExecute() {
		for (EMObjectAction a: actions)
			if (a != null)
				a.execute();
	}
}