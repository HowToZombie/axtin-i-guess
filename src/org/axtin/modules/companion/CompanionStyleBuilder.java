package org.axtin.modules.companion;

import org.axtin.util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/12/2017
 */
public class CompanionStyleBuilder {
	
	private ItemStack helmet = null, chestplate = null, leggings = null, boots = null;
	
	public CompanionStyleBuilder(){ }
	
	public CompanionStyleBuilder setHelmet(ItemStack is){
		this.helmet = is;
		
		return this;
	}
	
	public CompanionStyleBuilder setHelmet_SkullOwner(String owner){
		final ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		final SkullMeta sm = (SkullMeta) is.getItemMeta();
		
		sm.setOwner(owner);
		is.setItemMeta(sm);
		this.helmet = is;
		
		return this;
	}
	
	public CompanionStyleBuilder setHelmet_SkullTexture(String texture){
		final ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		final SkullMeta sm = (SkullMeta) is.getItemMeta();
		
		ItemStackUtil.setCustomSkullTexture(sm, texture);
		is.setItemMeta(sm);
		this.helmet = is;
		
		return this;
	}
	
	public CompanionStyleBuilder setChestplate(ItemStack is){
		this.chestplate = is;
		
		return this;
	}
	
	public CompanionStyleBuilder setLeggings(ItemStack is){
		this.leggings = is;
		
		return this;
	}
	
	public CompanionStyleBuilder setBoots(ItemStack is){
		this.boots = is;
		
		return this;
	}
	
	public CompanionStyle build(){
		return new CompanionStyle(helmet, chestplate, leggings, boots);
	}
}
