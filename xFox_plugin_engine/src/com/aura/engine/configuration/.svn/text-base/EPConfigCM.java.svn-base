package com.aura.engine.configuration;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import com.aura.base.Aura;
import com.aura.base.TypeSide;
import com.aura.base.configuration.PropertiesLoader.PropertiesLoaderException;
import com.aura.base.manager.configuration.ConfigCE;
import com.aura.base.manager.configuration.ConfigCEBoolean;
import com.aura.base.manager.configuration.ConfigCEInteger;
import com.aura.base.manager.configuration.ConfigCEString;
import com.aura.base.manager.configuration.ConfigCM;
import com.aura.engine.plugin.EnginePlugin;
import com.aura.engine.univers.texture.SpriteSurfaceQuality;

public class EPConfigCM<A extends Aura> extends ConfigCM<A> {

	public static final int DISPLAY_WIDTH = CURSEUR.nextVal();
	public static final int DISPLAY_HEIGHT = CURSEUR.nextVal();
	public static final int DISPLAY_DEPTH = CURSEUR.nextVal();
	public static final int DISPLAY_RATE = CURSEUR.nextVal();
	public static final int DISPLAY_WINDOWED = CURSEUR.nextVal();
	public static final int DISPLAY_ANTI_ALIASING = CURSEUR.nextVal();
	public static final int DISPLAY_TEXTURE_QUALITY = CURSEUR.nextVal();

	public static final int CONSOLE_LAST_COMMIT = CURSEUR.nextVal();
	public static final int CONSOLE_COLOR_SYSTEM = CURSEUR.nextVal();
	public static final int CONSOLE_COLOR_HIGHLITE = CURSEUR.nextVal();
	public static final int CONSOLE_COLOR_DEFAULT = CURSEUR.nextVal();
	public static final int CONSOLE_FADE_MIN_TICK = CURSEUR.nextVal();
	public static final int CONSOLE_FADE_MAX_TICK = CURSEUR.nextVal();
	
	public static final int KEYBOARD_MENU = CURSEUR.nextVal();
	public static final int KEYBOARD_ENTER = CURSEUR.nextVal();
	public static final int KEYBOARD_DEBUG = CURSEUR.nextVal();
	public static final int KEYBOARD_DEBUG_NO_TEXTURE = CURSEUR.nextVal();
	
	public static final int KEYBOARD_UP = CURSEUR.nextVal();
	public static final int KEYBOARD_DOWN = CURSEUR.nextVal();
	public static final int KEYBOARD_LEFT = CURSEUR.nextVal();
	public static final int KEYBOARD_RIGHT = CURSEUR.nextVal();
	
	public static final int KEYBOARD_SELECT_ALL = CURSEUR.nextVal();
	public static final int KEYBOARD_FOCUS = CURSEUR.nextVal();
	
	public static final int MOUSE_SELECT = CURSEUR.nextVal(); 
	public static final int MOUSE_MOVE_TO = CURSEUR.nextVal();
	
	private EnginePlugin<?> plugin;
	public EPConfigCM(EnginePlugin<?> plugin, ConfigCM<A> selfManager) {
		super(selfManager);
		this.plugin = plugin;
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		Map<Integer, ConfigCE<?>> ces = new HashMap<Integer, ConfigCE<?>>();
		ces.put(DISPLAY_WIDTH, new ConfigCEInteger(DISPLAY_WIDTH, plugin.getTag()+".display.width", TypeSide.CLIENT, 800));
		ces.put(DISPLAY_HEIGHT, new ConfigCEInteger(DISPLAY_HEIGHT, plugin.getTag()+".display.height", TypeSide.CLIENT, 600));
		ces.put(DISPLAY_DEPTH, new ConfigCEInteger(DISPLAY_DEPTH, plugin.getTag()+".display.depth", TypeSide.CLIENT, 32));
		ces.put(DISPLAY_RATE, new ConfigCEInteger(DISPLAY_RATE, plugin.getTag()+".display.rate", TypeSide.CLIENT, 30));
		ces.put(DISPLAY_WINDOWED, new ConfigCEBoolean(DISPLAY_WINDOWED, plugin.getTag()+".display.windowed", TypeSide.CLIENT, true));
		ces.put(DISPLAY_ANTI_ALIASING, new ConfigCEBoolean(DISPLAY_ANTI_ALIASING, plugin.getTag()+".display.antiAliasing", TypeSide.CLIENT, true));
		ces.put(DISPLAY_TEXTURE_QUALITY, new ConfigCEInteger(DISPLAY_TEXTURE_QUALITY, plugin.getTag()+".display.textureQuality", TypeSide.CLIENT, SpriteSurfaceQuality.MEDIUM.getId()));
		
		ces.put(CONSOLE_LAST_COMMIT, new ConfigCEBoolean(CONSOLE_LAST_COMMIT, plugin.getTag()+".console.lastCommit", TypeSide.CLIENT, true));
		ces.put(CONSOLE_COLOR_SYSTEM, new ConfigCEString(CONSOLE_COLOR_SYSTEM, plugin.getTag()+".console.system", TypeSide.CLIENT, "YELLOW"));
		ces.put(CONSOLE_COLOR_HIGHLITE, new ConfigCEString(CONSOLE_COLOR_HIGHLITE, plugin.getTag()+".console.highlite", TypeSide.CLIENT, "ORANGE"));
		ces.put(CONSOLE_COLOR_DEFAULT, new ConfigCEString(CONSOLE_COLOR_DEFAULT, plugin.getTag()+".console.default", TypeSide.CLIENT, "LIGHT_GRAY"));
		ces.put(CONSOLE_FADE_MIN_TICK, new ConfigCEInteger(CONSOLE_FADE_MIN_TICK, plugin.getTag()+".console.fade.minTick", TypeSide.CLIENT, 5000));
		ces.put(CONSOLE_FADE_MAX_TICK, new ConfigCEInteger(CONSOLE_FADE_MAX_TICK, plugin.getTag()+".console.fade.maxTick", TypeSide.CLIENT, 5000));
		
		ces.put(KEYBOARD_MENU, new ConfigCEInteger(KEYBOARD_MENU, plugin.getTag()+".keyBoard.menu", TypeSide.CLIENT, KeyEvent.VK_ESCAPE));
		ces.put(KEYBOARD_ENTER, new ConfigCEInteger(KEYBOARD_ENTER, plugin.getTag()+".keyBoard.enter", TypeSide.CLIENT, KeyEvent.VK_ENTER));
		ces.put(KEYBOARD_DEBUG, new ConfigCEInteger(KEYBOARD_DEBUG, plugin.getTag()+".keyBoard.debug.show", TypeSide.CLIENT, KeyEvent.VK_F3));
		ces.put(KEYBOARD_DEBUG_NO_TEXTURE, new ConfigCEInteger(KEYBOARD_DEBUG_NO_TEXTURE, plugin.getTag()+".keyBoard.debug.noTexture", TypeSide.CLIENT, KeyEvent.VK_F4));
		
		ces.put(KEYBOARD_UP, new ConfigCEInteger(KEYBOARD_UP, plugin.getTag()+".keyBoard.up", TypeSide.CLIENT, KeyEvent.VK_UP));
		ces.put(KEYBOARD_DOWN, new ConfigCEInteger(KEYBOARD_DOWN, plugin.getTag()+".keyBoard.down", TypeSide.CLIENT, KeyEvent.VK_DOWN));
		ces.put(KEYBOARD_LEFT, new ConfigCEInteger(KEYBOARD_LEFT, plugin.getTag()+".keyBoard.left", TypeSide.CLIENT, KeyEvent.VK_LEFT));
		ces.put(KEYBOARD_RIGHT, new ConfigCEInteger(KEYBOARD_RIGHT, plugin.getTag()+".keyBoard.right", TypeSide.CLIENT, KeyEvent.VK_RIGHT));
		
		ces.put(KEYBOARD_SELECT_ALL, new ConfigCEInteger(KEYBOARD_SELECT_ALL, plugin.getTag()+".keyBoard.selectAll", TypeSide.CLIENT, KeyEvent.VK_A));
		ces.put(KEYBOARD_FOCUS, new ConfigCEInteger(KEYBOARD_FOCUS, plugin.getTag()+".keyBoard.focus", TypeSide.CLIENT, KeyEvent.VK_F));
		
		ces.put(MOUSE_SELECT, new ConfigCEInteger(MOUSE_SELECT, plugin.getTag()+".mouse.select", TypeSide.CLIENT, MouseEvent.BUTTON1));
		ces.put(MOUSE_MOVE_TO, new ConfigCEInteger(MOUSE_MOVE_TO, plugin.getTag()+".mouse.moveTo", TypeSide.CLIENT, MouseEvent.BUTTON3));
		
		for (Integer id: ces.keySet()) {
			ConfigCE<?> ce = ces.get(id);
			try {
				// Obligé de charger soit même, car déjà parse par le noyau.
				if (ce.getSide() == getAura().getSide()) {
					ce.load(getAura().getCfgManager().getLoader());
					register(ce);
				}
			} catch (PropertiesLoaderException e) {
				e.printStackTrace();
			}
		}
	}
	
	public SpriteSurfaceQuality getQuality() {
		return SpriteSurfaceQuality.parseInt(getAura().getCfgManager().getConfigIntegerValue(DISPLAY_TEXTURE_QUALITY));
	}
}