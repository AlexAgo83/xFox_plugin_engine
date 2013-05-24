package com.aura.engine.exemple;

import com.aura.base.Aura;
import com.aura.base.container.AbstractAuraContainer;
import com.aura.base.manager.configuration.ConfigCM;
import com.aura.base.utils.AuraLogger;
import com.aura.client.AuraClient;
import com.aura.engine.AuraDisplayMode;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.module.EMConsoleUI;
import com.aura.engine.module.EMEntity;
import com.aura.engine.module.EMShellMenu;
import com.aura.engine.module.EMWorld;
import com.aura.engine.packet.EPPacketCM;
import com.aura.engine.plugin.EngineClientPlugin;

public class ExempleEngineClient_Multi {
	public static void main(String[] args) {
		AbstractAuraContainer<AuraClient> container = new AbstractAuraContainer<AuraClient>() {
			@Override
			public AuraClient init() {
				AuraClient client = new AuraClient(
//						"test", "test.log"
						) {
					@Override
					public void initPlugin(final Aura self) {
						self.getCfgManager().setConfigStringValue(ConfigCM.SOCKET_LOGIN, "USER_"+Math.round((float) (Math.random()*100)), true);
						
						addPlugin(new EngineClientPlugin(this) {
							@Override
							public AuraScreen createScreen(AuraClient a) {
								return new AuraScreen(a.getBuildRevision(), a, new AuraDisplayMode(getAura())) {
									@Override
									public AuraEngine createEngine(final AuraClient client, final AuraScreen screen) {
										return new AuraEngine(client, screen, 50) {
											@Override
											public void init() {
												attachModule(0, new EMWorld(client, screen, this));
												attachModule(0, new EMEntity(client, screen, this));
												attachModule(1, new EMConsoleUI(client, screen, this));
												attachModule(2, new EMShellMenu(client, screen, this));
											}
											@Override
											public void play() {
												super.play();
												
												// à mettre en commentaire
												setDebug(true);
												
												// INIT SELF-SCENE -> provoque l'affichage des entitées (request-server)
												((AuraClient) getMainAura()).getNetworkManager().envoyerPaquet(
													getMainAura().createPacket(EPPacketCM.REQUEST_ENTITY));
												((AuraClient) getMainAura()).getNetworkManager().envoyerPaquet(
													getMainAura().createPacket(EPPacketCM.ENTITY_MOVE));
												
												AuraLogger.config(getMainAura().getSide(), "Welcome ! Tape /help");
											}
										};
									}
								};
							}
						}); 
					}
				};
				AuraLogger.debug(true);
				return client;
			}
		};
		container.getAura().getBuildRevision();
	}
}