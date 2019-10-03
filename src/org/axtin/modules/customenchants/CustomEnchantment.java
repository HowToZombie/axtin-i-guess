package org.axtin.modules.customenchants;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/11/2017
 */
public abstract class CustomEnchantment {
	
	public final CustomEnchantmentType type;
	
	public CustomEnchantment(CustomEnchantmentType type){
		this.type = type;
	}
	
	public abstract void onUse(Player player, ItemStack item);
	
	public abstract void onTake(Player player, ItemStack item);
	
	public abstract void onPutAway(Player player, ItemStack item);
}