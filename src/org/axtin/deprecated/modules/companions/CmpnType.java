package org.axtin.deprecated.modules.companions;

public enum CmpnType {

	BLUE, PINK;
	
	public String getTexture() {
		switch(this) {
		case BLUE:
			return "http://textures.minecraft.net/texture/c215dbda565f5cb8eb12f555f5e3da0ea5f5519891cc5c5d676fd82c62";
		case PINK:
			return "http://textures.minecraft.net/texture/c215dbda565f5cb8eb12f555f5e3da0ea5f5519891cc5c5d676fd82c62";
		default:
			return "http://textures.minecraft.net/texture/c215dbda565f5cb8eb12f555f5e3da0ea5f5519891cc5c5d676fd82c62";
		}
	}
	
}
