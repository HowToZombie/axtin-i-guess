package org.axtin.modules.customenchants;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.axtin.modules.customenchants.enchants.*;
import org.bukkit.Material;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/11/2017
 */
public enum CustomEnchantmentType {
	
	Earthquake("Earthquake", new EarthquakeEnchantment(), 15, EnchantableSlot.TOOL_PICKAXE),
	Laser("Laser", new LaserEnchantment(), 10, EnchantableSlot.TOOL_PICKAXE),
	Companion("Astronaut Companion", new CompanionEnchantment(), -1, EnchantableSlot.TOOL_PICKAXE),
	Implosion("Implosion", new ImplosionEnchantment(), 7, EnchantableSlot.TOOL_PICKAXE),
	Explosion("Explosion", new ExplosionEnchantment(), 10, EnchantableSlot.TOOL_PICKAXE);
	
	public final String name;
	public final CustomEnchantment obj;
	public final int cooldownTime; // in seconds, -1 = no cooldown
	public final EnchantableSlot[] slots;
	
	private CustomEnchantmentType(String name, CustomEnchantment obj, int cooldownTime, EnchantableSlot... slots){
		this.name = name;
		this.cooldownTime = cooldownTime;
		this.obj = obj;
		this.slots = slots;
	}
	
	public boolean isMaterial(Material mat){
		for(EnchantableSlot slot:slots){
			for(Material m:slot.materials)
				if(m == mat)
					return true;
		}
		
		return false;
	}
	
	public static @Nullable CustomEnchantmentType getByName(String name){
		for(CustomEnchantmentType type:values()){
			if(type.name().equalsIgnoreCase(name) || type.name.equalsIgnoreCase(name))
				return type;
		}
		
		return null;
	}
	
	public static CustomEnchantmentType[] getTypes(EnchantableSlot slot){
		final List<CustomEnchantmentType> list = new ArrayList<>();
		
		for(CustomEnchantmentType type:values()){
			for(EnchantableSlot s:type.slots){
				if(s == slot)
					list.add(type);
			}
		}
		
		return list.toArray(new CustomEnchantmentType[list.size()]);
	}
}