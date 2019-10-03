package org.axtin.modules.companion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/12/2017
 */
public class CompanionManager {
	
	private List<Companion> spawnedCompanions = new ArrayList<>();
	
	public Companion createCompanion(CompanionStyle style, Location loc){
		return new Companion(this, style, loc);
	}
	
	public List<Companion> getSpawnedCompanions(){
		return this.spawnedCompanions;
	}
	
	public void removeAll(){
		for(Companion c:new ArrayList<Companion>(spawnedCompanions))
			c.remove();
	}
}