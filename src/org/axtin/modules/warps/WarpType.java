package org.axtin.modules.warps;

public enum WarpType {

	MINE, OTHER;
	
	public static boolean typeExists(String name) {
		for(WarpType type : values()) {
			if(type.toString().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
	public static WarpType getType(String name) {
		if(typeExists(name)) {
			for(WarpType type : values()) {
				if(type.toString().equalsIgnoreCase(name))
					return type;
			}
		}
		return null;
	}
	
}
