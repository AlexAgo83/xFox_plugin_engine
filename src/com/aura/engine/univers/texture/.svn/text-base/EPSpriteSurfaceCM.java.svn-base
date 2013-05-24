package com.aura.engine.univers.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.aura.base.Aura;
import com.aura.base.container.AbstractContainerManager;
import com.aura.base.utils.AuraLogger;
import com.aura.base.utils.Curseur;
import com.aura.client.AuraClient;
import com.aura.engine.configuration.EPConfigCM;
import com.aura.engine.plugin.EnginePlugin;

public class EPSpriteSurfaceCM extends AbstractContainerManager<AuraClient, EPSpriteSurfaceCE> {
	protected static final Curseur CURSEUR = new Curseur(EPSpriteSurfaceCM.class);
	
	// FIXME XF(AA) [SpriteSurface] Exemple à enlever
	public static final int GROUND_BASE = CURSEUR.nextVal();
	public static final int ENTITY_BASE = CURSEUR.nextVal();
	
	public EPSpriteSurfaceCM(AuraClient client) {
		super(client);
		this.images = new HashMap<String, BufferedImage>();
	}
	
	private SpriteSurfaceQuality getConfigQuality() {
		return SpriteSurfaceQuality.parseInt(getAura().getCfgManager().getConfigIntegerValue(EPConfigCM.DISPLAY_TEXTURE_QUALITY));
	}
	
	private EPSpriteSurfaceCE[] getSpriteSurfaceToRegister() {
		return new EPSpriteSurfaceCE[] {
			new EPSpriteSurfaceCE(
				GROUND_BASE, "GROUND_BASE", Aura.D_RESS + "\\" + EnginePlugin.D_MATERIAL + "\\",
				"ground_base.png", 
				5, 5, 100, 100, SpriteScaleFilter.NONE, this),
			new EPSpriteSurfaceCE(
				ENTITY_BASE, "ENTITY_BASE", Aura.D_RESS + "\\" + EnginePlugin.D_MATERIAL + "\\",
				"entity_base.png", 
				7, 4, 100, 100, SpriteScaleFilter.NONE, this)
		};
	}
	
	@Override
	protected void implLoad() throws KeyContainerAlreadyUsed {
		EPSpriteSurfaceCE[] ces = getSpriteSurfaceToRegister();
		for (EPSpriteSurfaceCE ce: ces) 
			register(ce);
		AuraLogger.config(getAura().getSide(), ces.length + " textures chargées (Quality: " + getConfigQuality() + ")");
	}
	
	protected void reload() throws KeyContainerAlreadyUsed {
		EPSpriteSurfaceCE[] ces = getSpriteSurfaceToRegister();
		for (EPSpriteSurfaceCE ce: ces) 
			override(ce);
		AuraLogger.config(getAura().getSide(), ces.length + " textures (re)chargées (Quality: " + getConfigQuality() + ")");
	}
	
	private final Map<String, BufferedImage> images;
	protected BufferedImage loadImage(String path, String imagePath) {
		if (path == null || imagePath == null)
			return null;
		String name = path + getConfigQuality().getFileTag() + imagePath;
		if (!images.containsKey(name)) {
			try {
				images.put(name, ImageIO.read(new File(name)));
			} catch (IllegalArgumentException e) {
				AuraLogger.severe(getAura().getSide(), "Err. lors du chargement de l'image: " + name, e);
			} catch (IOException e) {
				AuraLogger.severe(getAura().getSide(), "Err. lors du chargement de l'image: " + name, e);
			}
		}
		return images.get(name);
	}
}