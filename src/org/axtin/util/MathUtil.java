package org.axtin.util;

import org.bukkit.Location;

public class MathUtil {
	
	public static boolean isInside(Location loc, Location min, Location max){
	    return loc.getWorld().equals(min.getWorld()) &
			   min.getX() < loc.getX() &&
			   max.getX() > loc.getX() &&
			   min.getY() < loc.getY() &&
			   max.getY() > loc.getY() &&
			   min.getZ() < loc.getZ() &&
			   max.getZ() > loc.getZ();
	}
}
