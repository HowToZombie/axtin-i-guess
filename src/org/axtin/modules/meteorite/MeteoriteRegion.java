package org.axtin.modules.meteorite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 10/6/2017
 */
public class MeteoriteRegion {
	
	private final int id;
	private final Location min, max;
	private final List<Meteorite> meteorites = new ArrayList<Meteorite>();
	
	public MeteoriteRegion(int id, Location min, Location max){
		this.id = id;
		this.min = min;
		this.max = max;
	}
	
	public int getID(){
		return this.id;
	}
	
	public Location getLocMin(){
		return this.min;
	}
	
	public Location getLocMax(){
		return this.max;
	}
	
	public List<Meteorite> getMeteorites(){
		return this.meteorites;
	}
	
	public void removeAll(){
		for(Meteorite m:new ArrayList<Meteorite>(meteorites))
			m.remove();
	}
	
	public Meteorite spawn(){
		final Random rand = new Random();
		final int x = min.getBlockX() + (max.getBlockX()-min.getBlockX() > 0 ? rand.nextInt(max.getBlockX()-min.getBlockX()) : 0);
		final int z = min.getBlockZ() + (max.getBlockZ()-min.getBlockZ() > 0 ? rand.nextInt(max.getBlockZ()-min.getBlockZ()) : 0);
		final Meteorite m = new Meteorite(this, min.getWorld().getHighestBlockAt(x, z).getLocation());
		
		this.meteorites.add(m);
		
		return m;
	}
}
