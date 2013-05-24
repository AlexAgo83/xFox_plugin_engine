package com.aura.engine.module;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.aura.engine.univers.drawable.AuraDrawable;
import com.aura.engine.univers.drawable.DEntity;
import com.aura.engine.univers.texture.garbage.SGEntity;
import com.aura.engine.univers.world.WorldClient;
import com.aura.engine.utils.Location;
import com.aura.engine.utils.Orientation;

public class EMEntity extends AuraEngineModule {
	private Location selectionStart;
	
	private int selectionX = 0; 
	private int selectionY = 0;
	private int selectionW = 0; 
	private int selectionH = 0;
	
	private boolean selectionCommit;
	private List<AbstractDrawable> toAddSelection; 
	
	@Override
	public boolean isWorldFocusNeeded() {
		return true;
	}
	
	public EMEntity(AuraClient aura, AuraScreen screen, AuraEngine scene) {
		super(aura, screen, scene);
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
				
				final WorldClient world = getUnivers().getFocusWorld();
				
				for (EntityElementMove e : me.getElements()) {
					
					if (!e.getWorldId().equals(world.getId())) {
						AuraLogger.info(getAura().getSide(), "Event 'Move' annulée pour l'entité ["
								+e.getTokenId()+":"+e.getName()+" , world: "+e.getWorldId()+" contre "
								+world.getId()+"], le monde est entrain de se recycler.");
						return;
					}
					
					if (tempToken == null || tempToken.getId() != e.getTokenId()) {
						tempToken = (EntityTokenClient) world.getEntityById(e.getTokenId());
					}
					if (tempToken == null) {
						tempToken = new EntityTokenClient(e.getTokenId());
						world.init(tempToken);
					}
					
					if (tempDg == null || !tempDg.getToken().equals(tempToken)) {
						tempDg = world.getEntity(tempToken);
					}
					
					DEntity<AuraClient> d = (DEntity<AuraClient>) tempDg.getById(e.getDrawableId());
					if (d == null) {
						SGEntity sgEntity = new SGEntity(getScreen().getPlugin().getSpriteCM());
						sgEntity.setFocus(Orientation.IDLE);
						d = new DEntity<AuraClient>(getAura(), 
								e.getDrawableId(), tempDg, 
								e.getName(), e.getLocation(), 
								sgEntity, world.getEntityGarbage());
						
						// FIXME XF(AA) [EMEntity] création de l'entité coté client à faire en mieux. 
						world.attach(d);
						getDrawableQueueManager().register(d.getQueueItem());
						AuraLogger.fine(getAura().getSide(), "Entité [" + tempToken.getId()+":"+d.getName() + "] créée.");
					}
					
					d.setLocation(e.getLocation());
					if (e.getMoveElementType() == TypeEntityElementMove.ORIENTATION) {
						EntityElementMoveOrientation mo = (EntityElementMoveOrientation) e;
						d.move(mo.getOrientation(), mo.getSpeed());
					} else if (e.getMoveElementType() == TypeEntityElementMove.TARGET) {
						EntityElementMoveTarget mt = (EntityElementMoveTarget) e;
						d.move(me.isPoolMode(), mt.getTarget(), mt.getSpeed());
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
	
	@Override public void doKeyPressed(EPEventInputKey e, EPMapCE map) {}
	
	@Override
	public void doKeyReleased(EPEventInputKey e, EPMapCE map) {
		if (map == null) // pas une touche de direction
			return;
		
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
			
			Location tempPos = new Location();
			Location tempSelectionStart = new Location(selectionX, selectionY);
			Location tempSelectionStop = new Location(selectionX+selectionW, selectionY+selectionH);
			
			for (EntityToken t: tokens) {
				DrawableGarbage dg = t.getDrawable();
				for (AbstractDrawable d: dg.getDrawableTab()) {
					if (d.getToken().equals(getUnivers().getSelf())) {
						
						tempPos.x = d.getCurrentXScaled();
						tempPos.y = d.getCurrentYScaled();
						
						if (	   tempSelectionStart.x <= tempPos.x 
								&& tempSelectionStart.y <= tempPos.y 
								&& tempSelectionStop.x >= tempPos.x
								&& tempSelectionStop.y >= tempPos.y) 
							dws.add(d);
					}
				}
			}
			toAddSelection.clear();
			toAddSelection.addAll(dws);
			selectionStart = null;
			selectionCommit = true;
		} else if (map.getId() == EPMouseMapCM.MOVE_TO) {
			boolean modeAdd = getScene().getKeyMapManager().isPressed(EPKeyMapCM.SHIFT);
			
			AbstractDrawable[] dws = getUnivers().getFocusWorld().getDrawableSelectionTab();
			
			int xDecal = 0; int yDecal = 0;  
			int _subC = 0;  int _subR = 0;
			int column = 0;
			int maxColumn = new BigDecimal(Math.sqrt(dws.length)).setScale(0, RoundingMode.UP).intValue();
			
			int countElem = 0; int maxCount = 20;
			EPPacketEntityMove eMove = null;
			Location target = e.getCoordinate().scale(getScene().getScreenInfo().zoom.getInverse())
				.sub(getUnivers().getFocusWorld().getCameraPosition());
			
			boolean flipC = true;
			boolean flipR = true;
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
					eMove.setPoolMode(modeAdd);
					eMove.attachElements(elt);
					
					if (flipC) {
						_subC = _subC + 1;
						xDecal = (d.getWidth() + (d.getWidth()/4)) * _subC;
					} else {
						xDecal = -xDecal;
					}
					flipC = !flipC;
					
					column += 1;
					if (column == maxColumn) {
						xDecal = 0;
						column = 0;
						_subC = 0;
						flipC = true;
						
						if (flipR) {
							_subR = _subR + 1;
							yDecal = (d.getWidth() + (d.getWidth()/4)) * _subR;
						} else {
							yDecal = -yDecal;
						}
						flipR = !flipR;
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
			g.setColor(getScene().getUiColor());
			g.drawRect(selectionX, selectionY, selectionW, selectionH);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(.25f, 1.0f))) ;
			g.fillRect(selectionX, selectionY, selectionW, selectionH);
			g.setComposite(tempComposite);
		}
	}
	
	@Override 
	public void doUpdate() {

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
		
		// TEST COLLIDABLE
		final DEntity<?>[] dws = getUnivers().getFocusWorld().getEntityGarbage().getAll();
		final Set<AuraDrawable<?, ?>> alreadyTest = new HashSet<AuraDrawable<?, ?>>();
		for (DEntity<?> dw: dws) {
			if (!alreadyTest.contains(dw)) {
				AuraDrawable<?, ?> temp = dw.doTestCollide(dws);
				if (temp != null) {
					temp.forceCollide();
					alreadyTest.add(temp);
				}
			}
		}

		if (getScene().isDebug()) {
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
			getScene().addLog(msgStr + AuraBaliseParser.COLOR.getBalise("orange") + "]");
			
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
			getScene().addLog(msgStr + AuraBaliseParser.COLOR.getBalise("orange") + "]");
			
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
			getScene().addLog(msgStr + AuraBaliseParser.COLOR.getBalise("orange") + "]");
		}
	}
}