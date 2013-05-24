package com.aura.engine.plugin;

import javax.swing.JOptionPane;

import com.aura.base.TypeSide;
import com.aura.base.container.AbstractContainerManager.KeyContainerAlreadyUsed;
import com.aura.base.event.AbstractEvent;
import com.aura.base.event.AuraEventListener;
import com.aura.base.event.impl.KickEvent;
import com.aura.base.manager.AbstractPacketManager;
import com.aura.base.manager.configuration.ConfigCM;
import com.aura.base.manager.console.ConsoleCE;
import com.aura.base.manager.event.EventCM;
import com.aura.base.packet.AbstractPacket;
import com.aura.base.packet.impl.PacketLoginAttempt;
import com.aura.base.utils.AuraLogger;
import com.aura.client.AuraClient;
import com.aura.engine.AuraScreen;
import com.aura.engine.console.EPConsoleCM;
import com.aura.engine.event.EPEventCM;
import com.aura.engine.event.EPEventEntityMove;
import com.aura.engine.event.EPEventEntityRemove;
import com.aura.engine.event.EPEventTokenGranted;
import com.aura.engine.inputMap.key.EPKeyMapCM;
import com.aura.engine.inputMap.mouse.EPMouseMapCM;
import com.aura.engine.packet.EPPacketCM;
import com.aura.engine.packet.EPPacketEntityMove;
import com.aura.engine.packet.EPPacketEntityRemove;
import com.aura.engine.packet.EPPacketResultToken;
import com.aura.engine.packet.EPPacketResultWorld;
import com.aura.engine.packet.EntityElementMoveOrientation;
import com.aura.engine.univers.EntityTokenClient;
import com.aura.engine.univers.UniversClient;
import com.aura.engine.univers.texture.EPSpriteCM;
import com.aura.engine.univers.texture.EPSpriteSurfaceCM;
import com.aura.engine.univers.world.WorldClient;
import com.aura.engine.utils.Location;
import com.aura.server.AuraServer;

public abstract class EngineClientPlugin extends EnginePlugin<AuraClient> {
	private AuraServer selfServer;
	public AuraServer getSelfServer() {
		return selfServer;
	}
	public void setSelfServer(AuraServer selfServer) {
		this.selfServer = selfServer;
	}
	
	private UniversClient univers;
	public UniversClient getUnivers() {
		return univers;
	}
	
	private EntityTokenClient lastToken;
	public EntityTokenClient getLastToken() {
		return lastToken;
	}
	
	private final EPKeyMapCM keyMapCM;
	public EPKeyMapCM getKeyMapCM() {
		return keyMapCM;
	}
	
	private final EPMouseMapCM mouseMapCM;
	public EPMouseMapCM getMouseMapCM() {
		return mouseMapCM;
	}
	
	private final EPSpriteSurfaceCM spriteSurfaceCM;
	public EPSpriteSurfaceCM getSpriteSurfaceCM() {
		return spriteSurfaceCM;
	}
	
	private final EPSpriteCM spriteCM;
	public EPSpriteCM getSpriteCM() {
		return spriteCM;
	}
	
	public EngineClientPlugin(AuraClient client) {
		super(client);
		this.keyMapCM = new EPKeyMapCM(client, this);
		this.mouseMapCM = new EPMouseMapCM(client, this);
		this.spriteSurfaceCM = new EPSpriteSurfaceCM(client);
		this.spriteCM = new EPSpriteCM(client, spriteSurfaceCM);
	}
	
	@Override
	public boolean load() {
		if (!super.load())
			return false;
		try {
			keyMapCM.load();
			mouseMapCM.load();
			spriteSurfaceCM.load();
			spriteCM.load();
		} catch (KeyContainerAlreadyUsed exc) {
			exc.printLog(getAura().getSide());
			return false;
		}
		return true;
	}
	
	private AuraScreen screen;
	public AuraScreen getScreen() {
		return screen;
	}
	
	@Override
	public void onSystemLoad() {
		
		// Event d'intercept de connection
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(
				getAura().getEvent(EventCM.USER_CONNECT_OK)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				PacketLoginAttempt attempt = (PacketLoginAttempt) getAura().createPacket(EPPacketCM.USER_LOGIN_ATTEMPT);
				attempt.setLogin(getAura().getCfgManager().getConfigStringValue(ConfigCM.SOCKET_LOGIN));
				attempt.setPassword(
					getAura().getNetworkManager().getClientConnection().getPassword() != null ?
						getAura().getNetworkManager().getClientConnection().getPassword():
						"");
				getAura().getNetworkManager().envoyerPaquet(attempt);	
			}
		});
		
		// Event d'intercept d'err. de connection
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(
				getAura().getEvent(EventCM.USER_CONNECT_KO)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				kill("Connection error. Cause: "+getAura().getNetworkManager().getClientConnection().getResult());
			}
		});
		
		// Event d'intercept d'err. de connection
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(
				getAura().getEvent(EventCM.USER_CONNECT_ERR)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				kill("Connection impossible. Serveur offline ?");
			}
		});
		
		// Event d'intercept de login
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(
				getAura().getEvent(EventCM.USER_LOGIN_OK)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				getAura().getNetworkManager().envoyerPaquet(getAura().createPacket(EPPacketCM.REQUEST_TOKEN));
			}
		});
		
		// Event d'intercept d'err. de login
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(
				getAura().getEvent(EventCM.USER_LOGIN_KO),
				getAura().getEvent(EventCM.USER_LOGIN_NOT_EXIST),
				getAura().getEvent(EventCM.USER_LOGIN_BAD_PASSWORD)
				) {
			@Override
			protected void implExecute(AbstractEvent event) {
				kill("Login error. Cause: "+getAura().getNetworkManager().getClientConnection().getResult());
			}
		});
		
		// Event d'intercept d'un kick
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(
				getAura().getEvent(EventCM.USER_LOGIN_KICK)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				kill("Kick from server. Cause: "+((KickEvent) event).getTypeKick());
			}
		});
		
		// Event d'intercept d'un token granted
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(
				getAura().getEvent(EPEventCM.TOKEN_GRANTED)) {
				@Override
			protected void implExecute(AbstractEvent event) {
				lastToken = ((EPEventTokenGranted) event).getToken();
			}
		});
		
		// Event d'intercept d'un launch engine
		getAura().getEventManager().getDispatchThread().addListener(
			new AuraEventListener(
				getAura().getEvent(EPEventCM.LAUNCH_ENGINE)) {
			@Override
			protected void implExecute(AbstractEvent event) {
				if (lastToken != null) {
					if (univers == null) {
						univers = new UniversClient(getAura());
					}
					univers.setSelf(lastToken);
					getAura().getNetworkManager().envoyerPaquet(getAura().createPacket(EPPacketCM.REQUEST_WORLD));
				}
			}
		});
	}
	
	public abstract AuraScreen createScreen(final AuraClient a);
	
	public void kill(String raison) {
		if (selfServer != null && selfServer.isRunning())
			selfServer.turnOff(false);
		getAura().turnOff(false);
		
		if (raison != null)
			JOptionPane.showConfirmDialog(null, raison, null, JOptionPane.CANCEL_OPTION);
		if (screen != null) {
			screen.getMainFrame().setVisible(false);
		}
		
		System.exit(0);
	}
	
	@Override
	public void onPacketReceive(AbstractPacketManager pm, AbstractPacket ap) {
		if (ap.getId() == EPPacketCM.RESULT_TOKEN) {
			EPPacketResultToken p = (EPPacketResultToken) ap;
			EPEventTokenGranted e = (EPEventTokenGranted) getAura().createEvent(EPEventCM.TOKEN_GRANTED);
			e.setToken(new EntityTokenClient(p.getTokenId()));
			getAura().getEventManager().addEvent(e);
			return;
		}
		
		if (getUnivers() != null && getUnivers().getSelf() != null) 
			whileTokenLoaded(pm, ap);
	}
	
	private void whileTokenLoaded(final AbstractPacketManager pm, final AbstractPacket ap) {
		if (ap.getId() == EPPacketCM.RESULT_WORLD) {
			EPPacketResultWorld p = (EPPacketResultWorld) ap;
			
			if (univers.getFocusWorld() == null) {
				screen = createScreen(getAura());
				screen.getEngine().play();				
			}
			univers.setFocusWorld(new WorldClient(getAura(), univers, 
				p.getWorldId(), 
				p.getWidth(), 
				p.getHeight()));
			
			// INIT SELF-SCENE -> provoque l'affichage des entitées (request-server)
			getAura().getNetworkManager().envoyerPaquet(getAura().createPacket(EPPacketCM.REQUEST_ENTITY));
			return;
		}
		
		// FIXME XF(AA) voir si c'est pas dangereux !!
		if (getUnivers().getFocusWorld() != null)
//			new Thread() {
//				public void run() {
					whileWorldLoaded(pm, ap);
//				};
//			}.start();
	}
	
	private void whileWorldLoaded(AbstractPacketManager pm, AbstractPacket ap) {
		if (ap.getId() == EPPacketCM.ENTITY_MOVE) {
			EPPacketEntityMove m = (EPPacketEntityMove) ap;
			EPEventEntityMove e = (EPEventEntityMove) getAura().createEvent(EPEventCM.ENTITY_MOVE);
			e.setPoolMode(m.isPoolMode());
			e.attach(m.getCurrentElements());
			getAura().getEventManager().addEvent(e);	
		} else if (ap.getId() == EPPacketCM.ENTITY_REMOVE) {
			EPPacketEntityRemove p = (EPPacketEntityRemove) ap;
			if (p.getWorldId().equals(getUnivers().getFocusWorld().getId())) {
				EPEventEntityRemove e = (EPEventEntityRemove) getAura().createEvent(EPEventCM.ENTITY_REMOVE);
				e.setDrawableId(p.getDrawableId());
				e.setTokenId(p.getTokenId());
				e.setWorldId(p.getWorldId());
				getAura().getEventManager().addEvent(e);
			} else {
				AuraLogger.info(getAura().getSide(), "Packet 'Remove' annulée pour l'entité ["
					+p.getTokenId()+" , world: "+p.getWorldId()+" contre "
					+getUnivers().getFocusWorld().getId()+"], le monde est entrain de se recycler.");
			}
		}
	}
	
	@Override
	public void onCommand(String[] cmd) {
		if (selfServer != null)
			selfServer.getConsoleManager().onConsoleCommand(cmd);
		
		ConsoleCE cp = getAura().getConsoleManager().getContainerManager().dispatcher(TypeSide.CLIENT, cmd, false);
		if (cp == null) 
			return;
		
		if (cp.getId() == EPConsoleCM.SPAWN) {
			try {
				requestSpawn(Integer.parseInt(cmd[1]));
			} catch (Exception e) {
				AuraLogger.severe(getAura().getSide(), "Demande de spawn impossible.", e);
			}
		}
	}
	
	public void requestSpawn(int count) {
		if (count < 1) count = 1;
		if (count > 500) count = 500;
		AuraLogger.info(getAura().getSide(), "Demande de spawn" 
			+ (count > 1 ? ", count: " + count : ""));
		EPPacketEntityMove m = (EPPacketEntityMove) getAura().createPacket(EPPacketCM.ENTITY_MOVE);
		Location origine = new Location().sub(getScreen().getEngine().getScreenInfo().currentOrigine);
		for (int i=0; i<count; i++) {
			EntityElementMoveOrientation em = new EntityElementMoveOrientation();
			em.setName(getAura().getNetworkManager().getClientConnection().getLogin());
			em.setLocation(origine);
			m.setPoolMode(false);
			m.attachElements(em);
		}
		getAura().getNetworkManager().envoyerPaquet(m);
	}
	
	@Override public Class<?>[] onLoadDbClass() { return null; }
}