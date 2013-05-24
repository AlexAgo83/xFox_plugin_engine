package com.aura.engine.univers.drawable;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;

public class DrawableQueueManager {
	private final AuraEngine engine;
	private final Map<Integer, List<DrawableQueueItem<AuraClient>>> queueList;
	
	public DrawableQueueManager(AuraEngine engine) {
		this.engine = engine;
		this.queueList = new HashMap<Integer, List<DrawableQueueItem<AuraClient>>>();
	}
	
	public synchronized Map<Integer, List<DrawableQueueItem<AuraClient>>> getQueueList() {
		return queueList;
	}
	public AuraEngine getEngine() {
		return engine;
	}
	
	public int getQueueSize() {
		int cpt = 0;
		Integer[] is = getQueueList().keySet().toArray(new Integer[0]);
		for (Integer i: is) {
			DrawableQueueItem<?>[] ds = getQueueList().get(i).toArray(new DrawableQueueItem<?>[0]);
			cpt += ds.length;
		}
		return cpt;
	}
	
	public synchronized void register(DrawableQueueItem<AuraClient> q) {
		List<DrawableQueueItem<AuraClient>> ql = getQueueList().get(q.getDepth());
		if (ql == null) {
			ql = new ArrayList<DrawableQueueItem<AuraClient>>();
			getQueueList().put(q.getDepth(), ql);
		}
		ql.add(q);
	}
	
	public synchronized void remove(DrawableQueueItem<?> q) {
		List<DrawableQueueItem<AuraClient>> l = getQueueList().get(q.getDepth());
		if (l != null) {
			l.remove(q);
		}
	}
	
	private int lastPrintCount = 0;
	public int getLastPrintCount() {
		return lastPrintCount;
	}
	
	private List<DrawableOnCondition> toDraw;
	private List<DrawableOnCondition> getToDraw() {
		if (toDraw == null)
			toDraw = new ArrayList<DrawableOnCondition>();
		return toDraw;
	}
	
	private List<DrawableOnCondition> toDrawMarker;
	private List<DrawableOnCondition> getToDrawMarker() {
		if (toDrawMarker == null)
			toDrawMarker = new ArrayList<DrawableOnCondition>();
		return toDrawMarker;
	}
	
	private List<DrawableOnCondition> toDrawTarget;
	private List<DrawableOnCondition> getToDrawTarget() {
		if (toDrawTarget == null)
			toDrawTarget = new ArrayList<DrawableOnCondition>();
		return toDrawTarget;
	}
	
	public void doUpdate() {
		Integer[] is = getQueueList().keySet().toArray(new Integer[0]);
		
		lastPrintCount = 0;
		getToDraw().clear();
		getToDrawMarker().clear();
		getToDrawTarget().clear();
		
		int countTarget = 0;
		int maxTarget = 20;
		
		for (Integer i: is) {
			DrawableQueueItem<?>[] ds = getQueueList().get(i).toArray(new DrawableQueueItem<?>[0]);
			for (DrawableQueueItem<?> d: ds) {
				if (d.getDrawable().isMarkAsRemove()) {
					remove(d);
				} else {
					DrawableOnCondition doc = new DrawableOnCondition();
					doc.drawable = d.getDrawable();
					doc.drawable.update(getEngine());
					
					if (doc.drawable.isDrawable(getEngine().getScreenInfo())) {
						getToDraw().add(doc);
						lastPrintCount += 1;
					} else if (d.getDrawable().isAllowHudMarker()) {
						getToDrawMarker().add(doc);
					}
					
					if (countTarget < maxTarget 
							&& d.getDrawable().isAllowHudTarget() 
							&& getEngine().getUnivers().getFocusWorld() != null
							&& getEngine().getUnivers().getFocusWorld().isSelectionContains(d.getDrawable())) {
						countTarget += 1;
						getToDrawTarget().add(doc);
					}
				}
			}
		}
	}
	
	public void draw(Graphics2D gBoardEntity) {
		for (DrawableOnCondition doc: getToDraw()) {
			doc.drawable.draw(getEngine(), gBoardEntity);
			if (getEngine().isDebug()) 
				doc.drawable.drawDebug(getEngine(), gBoardEntity);
		}
	}
	public void drawHud(Graphics2D gBoard) {
		for (DrawableOnCondition doc: getToDrawMarker())
			doc.drawable.drawHudMarker(getEngine(), gBoard);
		int hudIndex = 0;
		for (DrawableOnCondition doc: getToDrawTarget()) {
			doc.drawable.drawHudTarget(getEngine(), gBoard, hudIndex);
			hudIndex += 1;
		}
	}
}