package org.axtin.modules.companion;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/12/2017
 */
public class CompanionStyle {
	
	private final ItemStack helmet, chestplate, leggings, boots;
	
	public CompanionStyle(@Nullable ItemStack helmet, @Nullable ItemStack chestplate, @Nullable ItemStack leggings, @Nullable ItemStack boots){
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
	}
	
	public @Nullable ItemStack getHelmet(){
		return this.helmet;
	}
	
	public @Nullable ItemStack getChestplate(){
		return this.chestplate;
	}
	
	public @Nullable ItemStack getLeggings(){
		return this.leggings;
	}
	
	public @Nullable ItemStack getBoots(){
		return this.boots;
	}
}
