package org.axtin.util;

import java.lang.reflect.Field;
import java.util.UUID;

import javax.annotation.Nullable;

import org.axtin.modules.customenchants.EnchantableSlot;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class ItemStackUtil {
	
	public static ItemStack rename(ItemStack is, String name){
		final ItemMeta im = is.getItemMeta();
		
		if(im != null){
			im.setDisplayName(name);
			is.setItemMeta(im);
			
			return is;
		}else
			return is;
	}
	
	public static @Nullable String getName(ItemStack is){
		final ItemMeta im = is.getItemMeta();
		
		return im != null ? im.getDisplayName() : null;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack toItemStack(String name){
		final String[] strs = name.split("\\:");
		final String n = strs[0];
		
		ItemStack is = new ItemStack(Material.STONE, 1);
		
		// first the type
		if(Util.isInteger(n)){
			final Material m = Material.getMaterial(Integer.valueOf(n));
			
			if(m != null)
				is.setType(m);
			else
				return null;
		}else{
			final Material m = getMaterial(n);
			
			if(m != null)
				is.setType(m);
			else
				return null;
		}
		
		// then the durability (like potion, mobspawner...)
		if(strs.length >= 2){
			if(strs.length >= 4 && is.getType() == Material.POTION && Util.isInteger(strs[2]) && Util.isInteger(strs[3])){
				final PotionMeta meta = (PotionMeta) is.getItemMeta();
				PotionEffectType type = Util.getPotionType(strs[1]);
				
				if(type != null)
					meta.setMainEffect(type);
				else
					return is;
				
				meta.addCustomEffect(type.createEffect(Integer.valueOf(strs[3]), Integer.valueOf(strs[2])), true);
				
				return is;
			}
			
			if(Util.isInteger(strs[1]))
				
				is.setDurability((short) (int)Integer.valueOf(strs[1]));
			else{
				if(is.getType() == Material.SKULL_ITEM && strs[1].length() >= 3){
					is.setDurability((short) 3);
					final SkullMeta sm = (SkullMeta) is.getItemMeta();
					sm.setOwner(strs[1]);
					is.setItemMeta(sm);
				}else{
					for(EntityType e:EntityType.values()){
						if(e.name().equalsIgnoreCase(strs[1]) ||
						   e.name().replace("_", "").equalsIgnoreCase(strs[1])){
							
							is = addEntityTypeToItemStack(is, e);
							
							break;
						}
					}
				}
			}
		}
		
		return is;
	}
	
	public static ItemStack addEntityTypeToItemStack(ItemStack is, EntityType type){
		final net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(is);
		NBTTagCompound tag = nmsStack.getTag();
		if(tag == null) tag = new NBTTagCompound();
		final NBTTagCompound tag1 = new NBTTagCompound();
		
		tag1.setString("id", type.name().charAt(0) + type.name().toLowerCase().substring(1));
		tag.set("EntityTag", tag1);
		nmsStack.setTag(tag);
		is = CraftItemStack.asBukkitCopy(nmsStack);
		
		return is;
	}
	
	public static EntityType getEntityType(ItemStack is){
		final net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(is);
		final NBTTagCompound tag = nmsStack.getTag();
		
		if(tag != null){
			final NBTTagCompound tag1 = (NBTTagCompound) tag.get("EntityTag");
			
			if(tag1 == null)
				return null;
			
			final String value = tag1.getString("id");
			if(value != null)
				return Util.getEntityType(value.replace("minecraft:", ""));
		}
		
    	return null;
    }
	
	public static Material getMaterial(String str){
		if(str.equalsIgnoreCase("dye")) return Material.INK_SACK;
		
		for(Material m:Material.values()){
			if(m.name().equalsIgnoreCase(str) ||
			   m.name().replace("_", "").equalsIgnoreCase(str) ||
			   m.name().replace("_item", "").equalsIgnoreCase(str) ||
			   m.name().replace("_item", "").replace("_", "").equalsIgnoreCase(str))
				return m;
		}
		
		return null;
	}
	
	public static String itemstackToString(ItemStack is){
		if(is == null)
			return "null";
		
		if(is.getType() == Material.MONSTER_EGG && getEntityType(is) != null)
			return is.getType().name().toLowerCase() + ":" + getEntityType(is).name().toLowerCase();
		else if(is.getType() == Material.SKULL_ITEM && is.getDurability() == (short) 3)
			return is.getType().name().toLowerCase() + ":" + ((SkullMeta) is.getItemMeta()).getOwner();
		else if(is.getType() == Material.POTION){
			final PotionMeta meta = (PotionMeta) is.getItemMeta();
			
			if(meta.getCustomEffects().size() == 1){
				final PotionEffect effect = meta.getCustomEffects().get(0);
				
				return is.getType().name().toLowerCase() + ":" + effect.getType().getName().toLowerCase() + ":" + effect.getAmplifier() + ":" + effect.getDuration();
			}else
				return is.getType().name().toLowerCase() + ":" + is.getDurability();
		}else
			return is.getType().name().toLowerCase() + ":" + is.getDurability();
	}
	
	public static Material[] getHelmets(){
		return EnchantableSlot.ARMOR_HELMET.materials;
	}
	
	public static Material[] getChestplates(){
		return EnchantableSlot.ARMOR_CHESTPLATE.materials;
	}
	
	public static Material[] getLeggings(){
		return EnchantableSlot.ARMOR_LEGGINGS.materials;
	}
	
	public static Material[] getBoots(){
		return EnchantableSlot.ARMOR_BOOTS.materials;
	}
	
	public static Material[] getSwords(){
		return EnchantableSlot.TOOL_SWORD.materials;
	}
	
	public static void setCustomSkullTexture(SkullMeta sm, String texture){
		final GameProfile gp = new GameProfile(UUID.randomUUID(), null);
		
		gp.getProperties().put("textures", new Property("textures", texture));
		
		try{
			final Field f = sm.getClass().getDeclaredField("profile");
			
			f.setAccessible(true);
			f.set(sm, gp);
		}catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
		}
	}
}
