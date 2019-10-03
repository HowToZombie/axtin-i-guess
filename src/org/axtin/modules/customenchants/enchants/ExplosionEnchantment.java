package org.axtin.modules.customenchants.enchants;

import org.axtin.modules.customenchants.CustomEnchantment;
import org.axtin.modules.customenchants.CustomEnchantmentType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/11/2017
 */
public class ExplosionEnchantment extends CustomEnchantment {

	public ExplosionEnchantment(){
		super(CustomEnchantmentType.Explosion);
	}

	@Override
	public void onUse(Player player, ItemStack item){
		
	}

	@Override
	public void onTake(Player player, ItemStack item){ }

	@Override
	public void onPutAway(Player player, ItemStack item){ }
}