package com.aura.engine.plugin;

import java.util.List;

import com.aura.base.TypeSide;
import com.aura.base.event.AbstractEvent;
import com.aura.base.event.AuraEventListener;
import com.aura.base.event.impl.PacketManagerDeconnectionEvent;
import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.console.ConsoleCE;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.utils.AuraLogger;
import com.aura.engine.console.EPConsoleCM;
import com.aura.engine.event.EPEventCM;
import com.aura.engine.packet.EPPacketCM;
import com.aura.engine.packet.EPPacketEntityMove;
import com.aura.engine.packet.EPPacketEntityRemove;
import com.aura.engine.packet.EPPacketResultToken;
import com.aura.engine.packet.EPPacketResultWorld;
import com.aura.engine.packet.EntityElementMove;
import com.aura.engine.packet.EntityElementMoveOrientation;
import com.aura.engine.packet.EntityElementMoveTarget;
import com.aura.engine.univers.DrawableGarbage;
import com.aura.engine.univers.EntityToken;
import com.aura.engine.univers.EntityTokenServer;
import com.aura.engine.univers.UniversServer;
import com.aura.engine.univers.drawable.AbstractDrawable;
import com.aura.engine.univers.drawable.DEntity;
import com.aura.engine.univers.world.WorldServer;
import com.aura.server.AuraServer;

public class EngineServerPlugin extends EnginePlugin<AuraServer> {
	private UniversServer univers;
	public UniversServer getUnivers() {
		return univers;
	}
	
	public EngineServerPlugin(AuraServer server) {
		super(server);
	}
	
	@Override
	public void onSystemLoad() {
		this.univers = new UniversServer(getAura());
		
		getAura().getEventManager().getDispatchThread().addListener(new AuraEventListener(
			getAura().getEvent(EPEventCM.USER_CONNECT_KO)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				PacketManagerDeconnectionEvent e = (PacketManagerDeconnectionEvent) event;
				EntityTokenServer l = getUnivers().getEntityTokenByPacket(e.getPacketManager());
				if (l != null && l.getWorld() != null) {
					DrawableGarbage dg = l.getWorld().getEntity(l);
					
					AbstractDrawable[] toRemove = dg.getDrawableTab();
					l.getWorld().removeAllByToken(l);
					
					for (AbstractDrawable d : toRemove) {
						EPPacketEntityRemove r = (EPPacketEntityRemove) getAura().createPacket(EPPacketCM.ENTITY_REMOVE);
						r.setDrawableId(d.getId());
						r.setTokenId(l.getId());
						r.setWorldId(l.getWorld().getId());
						l.getWorld().broadCast(r);
					}
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onPacketReceive(final AbstractPacketManager pm, final AbstractPacket ap) {
		// init ID tracker
		final EntityTokenServer token = getUnivers().getEntityTokenByPacket(pm);
		
		if (ap.getId() == EPPacketCM.REQUEST_TOKEN) {
			EPPacketResultToken p = (EPPacketResultToken) getAura().createPacket(EPPacketCM.RESULT_TOKEN);
			p.setTokenId(token.getId());
			getAura().getNetworkManager().envoyerPacket(pm, p);
		} else if (ap.getId() == EPPacketCM.REQUEST_WORLD) {
			if (token.getWorld() == null) // si l'utilisateur ne précise pas l'id du monde
				token.setWorld(getUnivers().getDefaultWorld());
			EPPacketResultWorld p = (EPPacketResultWorld) getAura().createPacket(EPPacketCM.RESULT_WORLD);
			p.setWorldId(token.getWorld().getId());
			p.setWidth(token.getWorld().getWidth());
			p.setHeight(token.getWorld().getHeight());
			getAura().getNetworkManager().envoyerPacket(pm, p);
		} else if (ap.getId() == EPPacketCM.REQUEST_ENTITY) {
			EntityToken[] tokens = token.getWorld().getEntitiesTab();
			EPPacketEntityMove m = (EPPacketEntityMove) getAura().createPacket(EPPacketCM.ENTITY_MOVE);
			for (EntityToken t: tokens) { 
				for (Long id: t.getDrawable().getIdTab()) {
					DEntity<AuraServer> ent = (DEntity<AuraServer>) t.getDrawable().getById(id);
					
					EntityElementMove me = null;
					if (ent.getMoveThread() != null) {
						if (ent.getMoveThread().getCurrentTarget() != null){
							me = new EntityElementMoveTarget();
							((EntityElementMoveTarget) me).setTarget(ent.getMoveThread().getCurrentTarget());
						} else {
							me = new EntityElementMoveOrientation();
							((EntityElementMoveOrientation) me).setOrientation(ent.getMoveThread().getCurrentOrientation());
						}
					} else {
						// DEFAULT MOVE
						me = new EntityElementMoveOrientation();
					}
					
					me.setDrawableId(ent.getId());
					me.setTokenId(t.getId());
					me.setWorldId(((EntityTokenServer) t).getWorld().getId());
					me.setName(ent.getName());
					me.setLocation(ent.getLocation());
					me.setSpeed(ent.getSpeed());
					m.setPoolMode(false);
					m.attachElements(me);
				}
			}
			getAura().getNetworkManager().envoyerPacket(pm, m);
		} else if (ap.getId() == EPPacketCM.ENTITY_MOVE) {
			onMove(ap, token);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void onMove(AbstractPacket ap, EntityTokenServer token) {
		EPPacketEntityMove em = (EPPacketEntityMove) ap;
		List<EntityElementMove> mes = em.getCurrentElements();
		
		for (EntityElementMove me: mes) {
			AbstractDrawable drawable = token.getDrawable().getById(me.getDrawableId());
			
			if (drawable == null) {
				long id = AbstractDrawable.GENERER_ID();
				DrawableGarbage dg = token.getWorld().init(token);
				drawable = new DEntity<AuraServer>(
					getAura(), id, dg, me.getName(), me.getLocation(), 
					null, token.getWorld().getEntityGarbage());
				token.getWorld().attach(drawable);
				AuraLogger.fine(getAura().getSide(), 
						"Entite ["+drawable.getToken().getId()+":"+drawable.getName()+"] créée.");
				
			}
			
			// UPDATE
			DEntity<AuraServer> drawableEntity = (DEntity<AuraServer>) drawable;
			me.setName(drawable.getName());
			me.setDrawableId(drawableEntity.getId());
			me.setTokenId(token.getId());
			me.setWorldId(token.getWorld().getId());
			me.setLocation(drawableEntity.getLocation());
//			me.setSpeed(drawableEntity.getSpeed()); // init par le client
			
			switch (me.getMoveElementType()) {
				case ORIENTATION :
					EntityElementMoveOrientation meo = (EntityElementMoveOrientation) me;
					drawableEntity.move(meo.getOrientation(), me.getSpeed()); 
					break;
				case TARGET : 
					EntityElementMoveTarget met = (EntityElementMoveTarget) me;
					drawableEntity.move(em.isPoolMode(), met.getTarget(), me.getSpeed()); 
					break;
			}
		}
		
		token.getWorld().broadCast(em);
	}
	
	@Override
	public void onCommand(String[] cmd) {
		ConsoleCE cp = getAura().getConsoleManager().getContainerManager().dispatcher(TypeSide.SERVER, cmd, false);
		if (cp == null) 
			return;
		if (cp.getId() == EPConsoleCM.ADD_WORLD) {
			try {
				long newSeed = Long.parseLong(cmd[1]);
				int width = Integer.parseInt(cmd[2]);
				int height = Integer.parseInt(cmd[3]);
				
				WorldServer w = getUnivers().createNewWorld(newSeed, width, height);
				AuraLogger.config(getAura().getSide(), "Nouveau monde créé ["+w.getId()+"].");
			} catch (Exception e) {
				AuraLogger.severe(getAura().getSide(), "Création du nouveau monde impossible.", e);
				return;
			}
		} else if (cp.getId() == EPConsoleCM.LIST_WORLD) {
			String list = "Mondes disponibles : ";
			Long[] wIds = getUnivers().getAllWorldId();
			for (Long l: wIds) {
				list += l+ " ";
			}
			AuraLogger.info(getAura().getSide(), list);
		} else if (cp.getId() == EPConsoleCM.TP_PLAYER_TO) {
			try {
				String player = cmd[1].trim();
				long newSeed = Long.parseLong(cmd[2]);
				
				AbstractPacketManager[] pms = getUnivers().getAllPms();
				AbstractPacketManager pmTarget = null;
				for (AbstractPacketManager pm: pms) {
					if (pm.getUser().equals(player)) {
						pmTarget = pm;
						break;
					}
				}
				if (pmTarget != null) {
					EntityTokenServer tokenPlayer = getUnivers().getEntityToken(pmTarget);
					WorldServer worldPlayer = tokenPlayer.getWorld();
					WorldServer ws = getUnivers().getWorlds().get(newSeed);
					if (ws != null) {
						tokenPlayer.setWorld(ws);
						
						if (worldPlayer != null) {
							AbstractDrawable[] dws = tokenPlayer.getDrawable().getDrawableTab();
							worldPlayer.removeAllByToken(tokenPlayer);
							for (AbstractDrawable d: dws) {
								// Si il était déjà dans un monde , on prévient le monde que l'entité disparait.
								EPPacketEntityRemove r = (EPPacketEntityRemove) getAura().createPacket(EPPacketCM.ENTITY_REMOVE);
								r.setDrawableId(d.getId());
								r.setTokenId(tokenPlayer.getId());
								r.setWorldId(worldPlayer.getId());
								worldPlayer.broadCast(r);
							}
						}
						
//						AbstractDrawable[] dws = tokenPlayer.getDrawable().getDrawableTab();
//						for (AbstractDrawable d: dws) {
//							d.setLocation(0, 0);
//							ws.attach(d);
//						}

						// On envoi le resultat du changement de map
						EPPacketResultWorld p = (EPPacketResultWorld) getAura().createPacket(EPPacketCM.RESULT_WORLD);
						p.setWorldId(ws.getId());
						p.setWidth(ws.getWidth());
						p.setHeight(ws.getHeight());
						
						getAura().getNetworkManager().envoyerPacket(pmTarget, p);
						
						AuraLogger.info(getAura().getSide(), "Player ["+player+"] tp sur monde [id:"+ws.getId()+"].");
					} else {
						AuraLogger.info(getAura().getSide(), "Monde [id:"+newSeed+"] introuvable.");
					}
				} else {
					AuraLogger.info(getAura().getSide(), "Player ["+player+"] introuvable.");
				}
			} catch (Exception e) {
				AuraLogger.severe(getAura().getSide(), "Tp impossible.", e);
				return;
			}
		} 
	}
	
	@Override public Class<?>[] onLoadDbClass() { return null; }
}