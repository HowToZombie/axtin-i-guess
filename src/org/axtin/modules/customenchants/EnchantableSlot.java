package org.axtin.modules.customenchants;

import javax.annotation.Nullable;

import org.bukkit.Material;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/11/2017
 */
public enum EnchantableSlot {
	
	TOOL_SWORD(Material.DIAMOND_SWORD, Material.GOLD_SWORD, Material.IRON_SWORD, Material.STONE_SWORD, Material.WOOD_SWORD),
	TOOL_SHOVEL(Material.DIAMOND_SPADE, Material.GOLD_SPADE, Material.IRON_SPADE, Material.STONE_SPADE, Material.WOOD_SPADE),
	TOOL_HOE(Material.DIAMOND_HOE, Material.GOLD_HOE, Material.IRON_HOE, Material.STONE_HOE, Material.WOOD_HOE),
	TOOL_PICKAXE(Material.DIAMOND_PICKAXE, Material.GOLD_PICKAXE, Material.IRON_PICKAXE, Material.STONE_PICKAXE, Material.WOOD_PICKAXE),
	TOOL_AXE(Material.DIAMOND_AXE, Material.GOLD_AXE, Material.IRON_AXE, Material.STONE_AXE, Material.WOOD_AXE),
	
	ARMOR_HELMET(Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET, Material.GOLD_HELMET, Material.IRON_HELMET, Material.LEATHER_HELMET),
	ARMOR_CHESTPLATE(Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.GOLD_CHESTPLATE, Material.IRON_CHESTPLATE, Material.LEATHER_CHESTPLATE),
	ARMOR_LEGGINGS(Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.GOLD_LEGGINGS, Material.IRON_LEGGINGS, Material.LEATHER_LEGGINGS),
	ARMOR_BOOTS(Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS, Material.GOLD_BOOTS, Material.IRON_BOOTS, Material.LEATHER_BOOTS);
	
	public final Material[] materials;
	
	private EnchantableSlot(Material... materials){
		this.materials = materials;
	}
	
	public static @Nullable EnchantableSlot getByMaterial(Material mat){
		for(EnchantableSlot slot:values()){
			for(Material m:slot.materials){
				if(mat == m)
					return slot;
			}
		}
		
		return null;
	}
}
