package com.aura.engine.univers.texture;

public enum SpriteSurfaceQuality {
	LOW(0, "lq_"),
	MEDIUM(1, "md_"),
	HIGH(2, "hq_");
	
	private int id;
	private String fileTag;
	private SpriteSurfaceQuality(int id, String fileTag) {
		this.id = id;
		this.fileTag = fileTag;
	}
	
	public int getId() {
		return id;
	}
	public String getFileTag() {
		return fileTag;
	}

	public static SpriteSurfaceQuality parseInt(Integer id) {
		for (SpriteSurfaceQuality q: SpriteSurfaceQuality.values()) {
			if (q.getId() == id)
				return q;
		}
		return null;
	}
}