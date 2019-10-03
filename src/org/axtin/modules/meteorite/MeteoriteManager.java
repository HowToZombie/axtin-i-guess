package org.axtin.modules.meteorite;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.axtin.container.facade.Container;
import org.axtin.util.MathUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 10/6/2017
 */
public class MeteoriteManager implements Listener {
	
	private final List<MeteoriteRegion> regions = new ArrayList<MeteoriteRegion>();
	private final List<Item> items = new ArrayList<Item>();
	
	public MeteoriteManager(){ }
	
	public void init(){
		MeteoriteConfig.load(this);
	}
	
	public void removeAll(){
		for(MeteoriteRegion region:regions)
			region.removeAll();
		
		regions.clear();
	}
	
	public List<MeteoriteRegion> getRegions(){
		return this.regions;
	}
	
	public List<Item> getItems(){
		return this.items;
	}
	
	public @Nullable MeteoriteRegion getRegionByID(int id){
		for(MeteoriteRegion region:regions){
			if(region.getID() == id)
				return region;
		}
		
		return null;
	}
	
	public @Nullable MeteoriteRegion getRegionByLocation(Location loc){
		for(MeteoriteRegion region:regions){
			if(MathUtil.isInside(loc, region.getLocMin(), region.getLocMax()))
				return region;
		}
		
		return null;
	}
	
	public int getNextRegionID(){
		int id = 0;
		
		while(getRegionByID(id) != null)
			id++;
		
		return id;
	}
	
	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event){
		for(MeteoriteRegion region:Container.get(MeteoriteManager.class).getRegions()){
			for(Meteorite m:region.getMeteorites()){
				for(org.bukkit.entity.Item item:m.getDroppedItems()){
					if(item.equals(event.getItem()))
						m.removeSlowly();
				}
			}
		}
	}
}
