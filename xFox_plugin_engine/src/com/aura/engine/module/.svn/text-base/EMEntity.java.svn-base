package com.aura.engine.module;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.aura.base.event.AbstractEvent;
import com.aura.base.event.AuraEventListener;
import com.aura.base.utils.AuraBaliseParser;
import com.aura.base.utils.AuraLogger;
import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.event.EPEventCM;
import com.aura.engine.event.EPEventEntityMove;
import com.aura.engine.event.EPEventEntityRemove;
import com.aura.engine.event.EPEventInputKey;
import com.aura.engine.event.EPEventInputMouse;
import com.aura.engine.inputMap.EPMapCE;
import com.aura.engine.inputMap.key.EPKeyMapCM;
import com.aura.engine.inputMap.mouse.EPMouseMapCM;
import com.aura.engine.packet.EPPacketCM;
import com.aura.engine.packet.EPPacketEntityMove;
import com.aura.engine.packet.EntityElementMove;
import com.aura.engine.packet.EntityElementMoveOrientation;
import com.aura.engine.packet.EntityElementMoveTarget;
import com.aura.engine.packet.TypeEntityElementMove;
import com.aura.engine.univers.DrawableGarbage;
import com.aura.engine.univers.EntityToken;
import com.aura.engine.univers.EntityTokenClient;
import com.aura.engine.univers.drawable.AbstractDrawable;
import com.aura.engine.univers.drawable.DEntity;
import com.aura.engine.univers.texture.garbage.SGEntity;
import com.aura.engine.utils.Location;
import com.aura.engine.utils.Orientation;

public class EMEntity extends AuraEngineModule {
	private Orientation inputOrientation;
	private Location selectionStart;
	
	private int selectionX = 0; 
	private int selectionY = 0;
	private int selectionW = 0; 
	private int selectionH = 0;
	
	private boolean selectionCommit;
	private List<AbstractDrawable> toAddSelection; 
	
	public EMEntity(AuraClient aura, AuraScreen screen, AuraEngine scene) {
		super(aura, screen, scene);
		this.inputOrientation = Orientation.IDLE;
		this.selectionCommit = false;
		this.toAddSelection = new ArrayList<AbstractDrawable>();
		
		// Deplacement des entitées.
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(
				getAura().getEvent(EPEventCM.ENTITY_MOVE)) {
			@Override
			protected void implExecute(final AbstractEvent event) {
				onMove(event);
			}
			
			@SuppressWarnings("unchecked")
			private void onMove(AbstractEvent event) {
				EPEventEntityMove me = (EPEventEntityMove) event;
				
				EntityTokenClient tempToken = null;
				DrawableGarbage tempDg = null;
				for (EntityElementMove e : me.getElements()) {
					
					if (!e.getWorldId().equals(getUnivers().getFocusWorld().getId())) {
						AuraLogger.info(getAura().getSide(), "Event 'Move' annulée pour l'entité ["
								+e.getTokenId()+":"+e.getName()+" , world: "+e.getWorldId()+" contre "
								+getUnivers().getFocusWorld().getId()+"], le monde est entrain de se recycler.");
						return;
					}
					
					if (tempToken == null || tempToken.getId() != e.getTokenId()) {
						tempToken = (EntityTokenClient) getUnivers().getFocusWorld().getEntityById(e.getTokenId());
					}
					if (tempToken == null) {
						tempToken = new EntityTokenClient(e.getTokenId());
						getUnivers().getFocusWorld().init(tempToken);
					}
					
					if (tempDg == null || !tempDg.getToken().equals(tempToken)) {
						tempDg = getUnivers().getFocusWorld().getEntity(tempToken);
					}
					
					DEntity<AuraClient> d = (DEntity<AuraClient>) tempDg.getById(e.getDrawableId());
					if (d == null) {
						SGEntity sgEntity = new SGEntity(getScreen().getPlugin().getSpriteCM());
						sgEntity.setFocus(Orientation.IDLE);
						d = new DEntity<AuraClient>(getAura(), 
								e.getDrawableId(), tempDg, 
								e.getName(), e.getLocation(), sgEntity);
						
						// FIXME XF(AA) [EMEntity] création de l'entité coté client à faire en mieux. 
						getUnivers().getFocusWorld().attach(d);
						getDrawableQueueManager().register(d.getQueueItem());
						AuraLogger.info(getAura().getSide(), "Entité [" + tempToken.getId()+":"+d.getName() + "] créée.");
					}
					
					d.setLocation(e.getLocation());
					if (e.getMoveElementType() == TypeEntityElementMove.ORIENTATION) {
						EntityElementMoveOrientation mo = (EntityElementMoveOrientation) e;
						d.move(mo.getOrientation(), mo.getSpeed());
					} else if (e.getMoveElementType() == TypeEntityElementMove.TARGET) {
						EntityElementMoveTarget mt = (EntityElementMoveTarget) e;
						d.move(mt.getTarget(), mt.getSpeed());
					}
				}
			}
		});
		
		// Suppressions des entitées.
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(getAura().getEvent(EPEventCM.ENTITY_REMOVE)) {
			@SuppressWarnings("unchecked")
			@Override
			protected void implExecute(AbstractEvent event) {
				EPEventEntityRemove e = (EPEventEntityRemove) event;
				
				if (!e.getWorldId().equals(getUnivers().getFocusWorld().getId())) {
					AuraLogger.info(getAura().getSide(), "Event 'Move' annulée pour l'entité ["
							+e.getTokenId()+" , world: "+e.getWorldId()+" contre "
							+getUnivers().getFocusWorld().getId()+"], le monde est entrain de se recycler.");
					return;
				}
				
				EntityTokenClient fake = (EntityTokenClient) getUnivers().getFocusWorld().getEntityById(e.getTokenId());
				if (fake != null) {
					DEntity<AuraClient> d = (DEntity<AuraClient>) fake.getDrawable().getById(e.getDrawableId());
					if (d != null) {
						getUnivers().getFocusWorld().rmvSelection(d);
						getDrawableQueueManager().remove(d.getQueueItem());
						getUnivers().getFocusWorld().removeDrawable(d);
						AuraLogger.info(getAura().getSide(), "Entité [" + fake.getId() + "] supprimée.");
					}
				}
			}
		});
	}
	
	@Override
	public void doKeyPressed(EPEventInputKey e, EPMapCE map) {
		if (map == null) // pas une touche de direction
			return;
		
		// CAMERA ON
		Orientation temp = getScene().getKeyMapManager().getByKeyMap();
		if (temp != inputOrientation) 
			inputOrientation = temp != null ? temp : Orientation.IDLE;
	}
	
	@Override
	public void doKeyReleased(EPEventInputKey e, EPMapCE map) {
		if (map == null) // pas une touche de direction
			return;
		
		// CAMERA OFF
		Orientation temp = getScene().getKeyMapManager().getByKeyMap();
		if (temp != inputOrientation) 
			inputOrientation = temp != null ? temp : Orientation.IDLE;
		
		if (map.getId() == EPKeyMapCM.SELECT_ALL) {
			DrawableGarbage dg = getUnivers().getFocusWorld().getEntity(getUnivers().getSelf());
			if (dg != null) {
				AbstractDrawable[] dws = dg.getDrawableTab();
				List<AbstractDrawable> td = new ArrayList<AbstractDrawable>();		
				for (AbstractDrawable d: dws) {
					if (d.getToken().equals(getUnivers().getSelf()))
						td.add(d);
				}
				toAddSelection.clear();
				toAddSelection.addAll(td);
				selectionCommit = true;
			}
		} else if (map.getId() == EPKeyMapCM.FOCUS) {
			// FIXME XF(AA) focus à terminer ici
//			AbstractDrawable[] dws = getUnivers().getFocusWorld().getDrawableSelectionTab();
//			if (dws.length == 0) {
//				DrawableGarbage dg = getUnivers().getFocusWorld().getEntity(getUnivers().getSelf());
//				if (dg != null) 
//					dws = dg.getDrawableTab();
//			}
//			if (dws.length > 0) {
//				Location newLoc = dws[0].getLocation();
//				getUnivers().getFocusWorld().getCameraPosition().x = newLoc.x;
//				getUnivers().getFocusWorld().getCameraPosition().y = newLoc.y;
//			}
		}
	}
	
	@Override 
	public void doMousePressed(EPEventInputMouse e, EPMapCE map) {
		if (map == null) // pas un clique
			return;
		if (map.getId() == EPMouseMapCM.SELECT) 
			selectionStart = e.getLocation();
	}
	
	@Override 
	public void doMouseReleased(EPEventInputMouse e, EPMapCE map) {
		if (map == null) // pas un clique
			return;
		if (map.getId() == EPMouseMapCM.SELECT) {
			List<AbstractDrawable> dws = new ArrayList<AbstractDrawable>();
			EntityToken[] tokens = getUnivers().getFocusWorld().getEntitiesTab();
			for (EntityToken t: tokens) {
				DrawableGarbage dg = t.getDrawable();
				for (AbstractDrawable d: dg.getDrawableTab()) {
					if (d.getToken().equals(getUnivers().getSelf())) {
						int x = (int) d.getScreenXPos(getScene().getScreenInfo(), d.getLocation(), true);
						int y = (int) d.getScreenYPos(getScene().getScreenInfo(), d.getLocation(), true);
						if (	   selectionX <= x 
								&& selectionY <= y 
								&& selectionX + selectionW >= x
								&& selectionY + selectionH >= y) 
							dws.add(d);
					}
				}
			}
			toAddSelection.clear();
			toAddSelection.addAll(dws);
			selectionStart = null;
			selectionCommit = true;
		} else if (map.getId() == EPMouseMapCM.MOVE_TO) {
			AbstractDrawable[] dws = getUnivers().getFocusWorld().getDrawableSelectionTab();
			
			int xDecal = 0; 
			int yDecal = 0;
			
			int curseurColonne = 0; 
			int maxCol = new BigDecimal(Math.sqrt(dws.length)).setScale(0, RoundingMode.UP).intValue();
			
			int _subC = 0;  
			int _subR = 0;
			
			int countElem = 0; 
			int maxCount = 20; // max element par Packet
			
			EPPacketEntityMove eMove = null;
			Location target = e.getCoordinate().sub(getUnivers().getFocusWorld().getCameraPosition());
			
			boolean flipX = true;
			boolean flipY = true;
			
			for (AbstractDrawable d: dws) {
				if (d.getToken().equals(getUnivers().getSelf())) {
					final EntityElementMoveTarget elt = new EntityElementMoveTarget();
					elt.getTarget().x = target.x + xDecal;
					elt.getTarget().y = target.y + yDecal;
					elt.setDrawableId(d.getId());
					elt.setTokenId(d.getToken().getId());
					elt.setSpeed(DEntity.DEFAULT_SPEED);
					
					if (eMove == null)
						eMove = (EPPacketEntityMove) getAura().createPacket(EPPacketCM.ENTITY_MOVE);
					eMove.attachElements(elt);
					
					if (flipX) {
						_subC += 1;
						xDecal = d.getWidth() * _subC;
					} else {
						xDecal = - d.getWidth() * _subC;
					}
					flipX = !flipX;
					
					curseurColonne += 1;
					if (curseurColonne == maxCol) {
						xDecal = 0;
						curseurColonne = 0;
						_subC = 0;
						
						if (flipY) {
							_subR += 1;
							yDecal = d.getHeight() * _subR;
						} else {
							yDecal = - d.getHeight() * _subR;
						}
						flipY = !flipY;
					}
					
					countElem += 1;
					if (countElem >= maxCount) {
						getAura().getNetworkManager().envoyerPaquet(eMove);
						eMove = null;
						countElem = 0;
					}
				}
			}
			if (eMove != null)
				getAura().getNetworkManager().envoyerPaquet(eMove);
		}
	}
	
	@Override 
	public void doDraw(Graphics2D g) {
		if (selectionStart != null) {
			Composite tempComposite = g.getComposite();
			g.setColor(scene.getUiColor());
			g.drawRect(selectionX, selectionY, selectionW, selectionH);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(.25f, 1.0f))) ;
			g.fillRect(selectionX, selectionY, selectionW, selectionH);
			g.setComposite(tempComposite);
		}
	}
	
	@Override
	public void doDrawDebug(List<String> msg) {
		// Orientation par input
		msg.add(
				AuraBaliseParser.COLOR.getBalise("orange") + "Ori. Input: " 
				+ AuraBaliseParser.COLOR.getBalise("white") + inputOrientation);
		
		// List des touches préssées
		Integer[] ids = getPlugin().getKeyMapCM().getContainerKeyTab();
		String msgStr = AuraBaliseParser.COLOR.getBalise("orange") + "Key pressed: [";
		boolean first = true;
		for (int i=0; i<ids.length; i++) {
			EPMapCE m = getPlugin().getKeyMapCM().getElement(ids[i]);
			Boolean value = getPlugin().getKeyMapCM().getPressed().get(m);
			if (value != null && value) {
				if (!first) msgStr += AuraBaliseParser.COLOR.getBalise("orange") + ", ";
				first = false;
				msgStr += AuraBaliseParser.COLOR.getBalise("white") + m.getLabel();
			}
		}
		msg.add(msgStr + AuraBaliseParser.COLOR.getBalise("orange") + "]");
		
		// List des touches préssées
		ids = getPlugin().getMouseMapCM().getContainerKeyTab();
		msgStr = AuraBaliseParser.COLOR.getBalise("orange") + "Mouse pressed: [";
		first = true;
		for (int i=0; i<ids.length; i++) {
			EPMapCE m = getPlugin().getMouseMapCM().getElement(ids[i]);
			Boolean value = getPlugin().getMouseMapCM().getPressed().get(m);
			if (value != null && value) {
				if (!first) msgStr += AuraBaliseParser.COLOR.getBalise("orange") + ", ";
				first = false;
				msgStr += AuraBaliseParser.COLOR.getBalise("white") + m.getLabel();
			}
		}
		msg.add(msgStr + AuraBaliseParser.COLOR.getBalise("orange") + "]");
		
		// Show selection
		msgStr = AuraBaliseParser.COLOR.getBalise("orange") + "Selection: [";
		if (selectionStart != null) {
			msgStr += AuraBaliseParser.COLOR.getBalise("orange") + "x: ";
			msgStr += AuraBaliseParser.COLOR.getBalise("white") + selectionStart.x;
			msgStr += AuraBaliseParser.COLOR.getBalise("orange") + ", y: ";
			msgStr += AuraBaliseParser.COLOR.getBalise("white") + selectionStart.y;
			msgStr += AuraBaliseParser.COLOR.getBalise("orange") + " To x: ";
			msgStr += AuraBaliseParser.COLOR.getBalise("white") + getScene().getScreenInfo().currentCurseur.x;
			msgStr += AuraBaliseParser.COLOR.getBalise("orange") + ", y: ";
			msgStr += AuraBaliseParser.COLOR.getBalise("white") + getScene().getScreenInfo().currentCurseur.y;
		}
		msg.add(msgStr + AuraBaliseParser.COLOR.getBalise("orange") + "]");
	}
	
	@Override 
	public void doUpdate() {
		getUnivers().getFocusWorld().getCameraPosition().x -= inputOrientation.getX() * 10;
		getUnivers().getFocusWorld().getCameraPosition().y += inputOrientation.getY() * 10;
		
		if (selectionCommit) {
			selectionCommit = false;
			getUnivers().getFocusWorld().pushSelection(toAddSelection, true);
		}
		
		if (selectionStart != null) {
			Location current = getScene().getScreenInfo().currentCurseur;
			
			if (selectionStart.x <= current.x) {
				selectionX = (int) selectionStart.x;
				selectionW = (int) current.x - selectionX;
			} else {
				selectionX = (int) current.x;
				selectionW = (int) selectionStart.x - selectionX;
			}
			
			if (selectionStart.y <= current.y) {
				selectionY = (int) selectionStart.y;
				selectionH = (int) current.y - selectionY;
			} else {
				selectionY = (int) current.y;
				selectionH = (int) selectionStart.y - selectionY;
			}
		} else {
			selectionX = 0; selectionY = 0;
			selectionW = 0; selectionH = 0;
		}
	}
}