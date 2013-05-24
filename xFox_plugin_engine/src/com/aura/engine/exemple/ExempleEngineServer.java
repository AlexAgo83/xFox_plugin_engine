package com.aura.engine.exemple;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.aura.base.Aura;
import com.aura.base.utils.AuraLogger;
import com.aura.engine.plugin.EngineServerPlugin;
import com.aura.exemple.server.ExempleServerMonitorView;
import com.aura.server.AuraServer;

public class ExempleEngineServer {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		frame.setContentPane(panel);
		{
			AuraServer server = new AuraServer() {
				@Override
				public void initPlugin(final Aura self) {
					addPlugin(new EngineServerPlugin(this)); 
				}
			};
			new ExempleServerMonitorView(server, panel);
			AuraLogger.debug(true);
		}
		frame.setVisible(true);
	}
}