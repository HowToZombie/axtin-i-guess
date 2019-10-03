package org.axtin.modules.meteorite;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 10/6/2017
 */
public class Item {
	
	private final ItemStack is;
	private final int rarity;
	
	public Item(ItemStack is, int rarity){
		this.is = is;
		this.rarity = rarity;
	}
	
	public ItemStack getItemStack(){
		return this.is;
	}
	
	public int getRarity(){
		return this.rarity;
	}
	
	public boolean gamble(){
		return new Random().nextInt(100) >= 100-rarity;
	}
	
	public org.bukkit.entity.Item drop(Location loc){
		final org.bukkit.entity.Item item = loc.getWorld().dropItem(loc.clone().add(0.5, 0, 0.5), is);
		item.setVelocity(new Vector(0, 0, 0));
		
		return item;
	}
}