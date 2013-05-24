package com.aura.engine.configuration;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import com.aura.base.Aura;
import com.aura.base.TypeSide;
import com.aura.base.configuration.PropertiesLoader;
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
	public static final int DISPLAY_UI_COLOR_RED = CURSEUR.nextVal();
	public static final int DISPLAY_UI_COLOR_GREEN = CURSEUR.nextVal();
	public static final int DISPLAY_UI_COLOR_BLUE = CURSEUR.nextVal();

	public static final int CONSOLE_LAST_COMMIT = CURSEUR.nextVal();
	public static final int CONSOLE_COLOR_SYSTEM = CURSEUR.nextVal();
	public static final int CONSOLE_COLOR_HIGHLITE = CURSEUR.nextVal();
	public static final int CONSOLE_COLOR_DEFAULT = CURSEUR.nextVal();
	public static final int CONSOLE_FADE_MIN_TICK = CURSEUR.nextVal();
	public static final int CONSOLE_FADE_MAX_TICK = CURSEUR.nextVal();
	
	public static final int KEYBOARD_ESCAPE = CURSEUR.nextVal();
	public static final int KEYBOARD_ENTER = CURSEUR.nextVal();
	public static final int KEYBOARD_DEBUG = CURSEUR.nextVal();
	public static final int KEYBOARD_DEBUG_NO_TEXTURE = CURSEUR.nextVal();
	
	public static final int KEYBOARD_UP = CURSEUR.nextVal();
	public static final int KEYBOARD_DOWN = CURSEUR.nextVal();
	public static final int KEYBOARD_LEFT = CURSEUR.nextVal();
	public static final int KEYBOARD_RIGHT = CURSEUR.nextVal();
	
	public static final int KEYBOARD_SHIFT = CURSEUR.nextVal();
	
	public static final int KEYBOARD_SELECT_ALL = CURSEUR.nextVal();
	public static final int KEYBOARD_FOCUS = CURSEUR.nextVal();
	
	public static final int MOUSE_SELECT = CURSEUR.nextVal(); 
	public static final int MOUSE_MOVE_TO = CURSEUR.nextVal();
	
	public static final int WORLD_DEFAULT_SEED = CURSEUR.nextVal();
	public static final int WORLD_DEFAULT_WIDTH = CURSEUR.nextVal();
	public static final int WORLD_DEFAULT_HEIGHT = CURSEUR.nextVal();
	
	private EnginePlugin<?> plugin;
	public EnginePlugin<?> getPlugin() {
		return plugin;
	}
	
	public EPConfigCM(EnginePlugin<?> plugin, ConfigCM<A> selfManager) {
		super(selfManager);
		this.plugin = plugin;
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		Map<Integer, ConfigCE<?>> ces = new HashMap<Integer, ConfigCE<?>>();
		ces.put(DISPLAY_WIDTH, new ConfigCEInteger(DISPLAY_WIDTH, linkTag()+".display.width", TypeSide.CLIENT, 800));
		ces.put(DISPLAY_HEIGHT, new ConfigCEInteger(DISPLAY_HEIGHT, linkTag()+".display.height", TypeSide.CLIENT, 600));
		ces.put(DISPLAY_DEPTH, new ConfigCEInteger(DISPLAY_DEPTH, linkTag()+".display.depth", TypeSide.CLIENT, 32));
		ces.put(DISPLAY_RATE, new ConfigCEInteger(DISPLAY_RATE, linkTag()+".display.rate", TypeSide.CLIENT, 30));
		ces.put(DISPLAY_WINDOWED, new ConfigCEBoolean(DISPLAY_WINDOWED, linkTag()+".display.windowed", TypeSide.CLIENT, true));
		ces.put(DISPLAY_ANTI_ALIASING, new ConfigCEBoolean(DISPLAY_ANTI_ALIASING, linkTag()+".display.antiAliasing", TypeSide.CLIENT, true));
		ces.put(DISPLAY_TEXTURE_QUALITY, new ConfigCEInteger(DISPLAY_TEXTURE_QUALITY, linkTag()+".display.textureQuality", TypeSide.CLIENT, SpriteSurfaceQuality.MEDIUM.getId()));
		
		Color colorDefault = Color.ORANGE;
		ces.put(DISPLAY_UI_COLOR_RED, new ConfigCEInteger(DISPLAY_UI_COLOR_RED, linkTag()+".display.uiColor.r", TypeSide.CLIENT, colorDefault.getRed()));
		ces.put(DISPLAY_UI_COLOR_GREEN, new ConfigCEInteger(DISPLAY_UI_COLOR_GREEN, linkTag()+".display.uiColor.g", TypeSide.CLIENT, colorDefault.getGreen()));
		ces.put(DISPLAY_UI_COLOR_BLUE, new ConfigCEInteger(DISPLAY_UI_COLOR_BLUE, linkTag()+".display.uiColor.b", TypeSide.CLIENT, colorDefault.getBlue()));
		
		ces.put(CONSOLE_LAST_COMMIT, new ConfigCEBoolean(CONSOLE_LAST_COMMIT, linkTag()+".console.lastCommit", TypeSide.CLIENT, true));
		ces.put(CONSOLE_COLOR_SYSTEM, new ConfigCEString(CONSOLE_COLOR_SYSTEM, linkTag()+".console.system", TypeSide.CLIENT, "YELLOW"));
		ces.put(CONSOLE_COLOR_HIGHLITE, new ConfigCEString(CONSOLE_COLOR_HIGHLITE, linkTag()+".console.highlite", TypeSide.CLIENT, "ORANGE"));
		ces.put(CONSOLE_COLOR_DEFAULT, new ConfigCEString(CONSOLE_COLOR_DEFAULT, linkTag()+".console.default", TypeSide.CLIENT, "LIGHT_GRAY"));
		ces.put(CONSOLE_FADE_MIN_TICK, new ConfigCEInteger(CONSOLE_FADE_MIN_TICK, linkTag()+".console.fade.minTick", TypeSide.CLIENT, 5000));
		ces.put(CONSOLE_FADE_MAX_TICK, new ConfigCEInteger(CONSOLE_FADE_MAX_TICK, linkTag()+".console.fade.maxTick", TypeSide.CLIENT, 5000));
		
		ces.put(KEYBOARD_ESCAPE, new ConfigCEInteger(KEYBOARD_ESCAPE, linkTag()+".keyBoard.escape", TypeSide.CLIENT, KeyEvent.VK_ESCAPE));
		ces.put(KEYBOARD_ENTER, new ConfigCEInteger(KEYBOARD_ENTER, linkTag()+".keyBoard.enter", TypeSide.CLIENT, KeyEvent.VK_ENTER));
		ces.put(KEYBOARD_DEBUG, new ConfigCEInteger(KEYBOARD_DEBUG, linkTag()+".keyBoard.debug.show", TypeSide.CLIENT, KeyEvent.VK_F3));
		ces.put(KEYBOARD_DEBUG_NO_TEXTURE, new ConfigCEInteger(KEYBOARD_DEBUG_NO_TEXTURE, linkTag()+".keyBoard.debug.noTexture", TypeSide.CLIENT, KeyEvent.VK_F4));
		
		ces.put(KEYBOARD_UP, new ConfigCEInteger(KEYBOARD_UP, linkTag()+".keyBoard.up", TypeSide.CLIENT, KeyEvent.VK_UP));
		ces.put(KEYBOARD_DOWN, new ConfigCEInteger(KEYBOARD_DOWN, linkTag()+".keyBoard.down", TypeSide.CLIENT, KeyEvent.VK_DOWN));
		ces.put(KEYBOARD_LEFT, new ConfigCEInteger(KEYBOARD_LEFT, linkTag()+".keyBoard.left", TypeSide.CLIENT, KeyEvent.VK_LEFT));
		ces.put(KEYBOARD_RIGHT, new ConfigCEInteger(KEYBOARD_RIGHT, linkTag()+".keyBoard.right", TypeSide.CLIENT, KeyEvent.VK_RIGHT));

		ces.put(KEYBOARD_SHIFT, new ConfigCEInteger(KEYBOARD_SHIFT, linkTag()+".keyBoard.shift", TypeSide.CLIENT, KeyEvent.VK_SHIFT));
		
		ces.put(KEYBOARD_SELECT_ALL, new ConfigCEInteger(KEYBOARD_SELECT_ALL, linkTag()+".keyBoard.selectAll", TypeSide.CLIENT, KeyEvent.VK_A));
		ces.put(KEYBOARD_FOCUS, new ConfigCEInteger(KEYBOARD_FOCUS, linkTag()+".keyBoard.focus", TypeSide.CLIENT, KeyEvent.VK_F));
		
		ces.put(MOUSE_SELECT, new ConfigCEInteger(MOUSE_SELECT, linkTag()+".mouse.select", TypeSide.CLIENT, MouseEvent.BUTTON1));
		ces.put(MOUSE_MOVE_TO, new ConfigCEInteger(MOUSE_MOVE_TO, linkTag()+".mouse.moveTo", TypeSide.CLIENT, MouseEvent.BUTTON3));
		
		ces.put(WORLD_DEFAULT_SEED, new ConfigCEInteger(WORLD_DEFAULT_SEED, linkTag()+".world.defaultSeed", TypeSide.SERVER, 1));
		ces.put(WORLD_DEFAULT_WIDTH, new ConfigCEInteger(WORLD_DEFAULT_WIDTH, linkTag()+".world.defaultWidth", TypeSide.SERVER, 0));
		ces.put(WORLD_DEFAULT_HEIGHT, new ConfigCEInteger(WORLD_DEFAULT_HEIGHT, linkTag()+".world.defaultHeight", TypeSide.SERVER, 0));
		
		for (Integer id: ces.keySet()) {
			ConfigCE<?> ce = ces.get(id);
			try {
				// Obligé de charger soit même, car déjà parse par le noyau.
				if (getAura() == null || ce.getSide() == getAura().getSide()) {
					ce.load(linkLoader());
					register(ce);
				}
			} catch (PropertiesLoaderException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * Getters "linkTag" surchargeables. ex: getPlugin().getTag();
	 */
	public String linkTag() {
		return getPlugin().getTag();
	}
	
	/** 
	 * Getters "linkLoader" surchargeables. ex: getAura().getCfgManager().getLoader();
	 */
	public PropertiesLoader linkLoader() {
		return getAura().getCfgManager().getLoader();
	}
	
	public SpriteSurfaceQuality getQuality() {
		return SpriteSurfaceQuality.parseInt(getAura().getCfgManager().getConfigIntegerValue(DISPLAY_TEXTURE_QUALITY));
	}
}