package org.axtin.util;

import java.util.Random;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

public class Util {
	public static final Random RAND = new Random();
	
	public static boolean isInteger(String str){
		try{
			Integer.valueOf(str);
			return true;
		}catch(IllegalArgumentException e){
			return false;
		}
	}
	
	public static boolean isDouble(String str){
		try{
			Double.valueOf(str);
			return true;
		}catch(IllegalArgumentException e){
			return false;
		}
	}
	
	public static boolean isBoolean(String str){
		return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
	}
	
	@SuppressWarnings("deprecation")
	public static @Nullable PotionEffectType getPotionType(String name){
		for(PotionEffectType type:PotionEffectType.values()){
			if(type.getName().equalsIgnoreCase(name) ||
			   (Util.isInteger(name) && type.getId() == Integer.valueOf(name)) ||
			   type.getName().replace("_", "").equalsIgnoreCase(name) ||
			   type.getName().replace("_", " ").equalsIgnoreCase(name))
				return type;
		}
		
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static @Nullable EntityType getEntityType(String name){
		for(EntityType type:EntityType.values()){
			if(type.name().equalsIgnoreCase(name) ||
			   type.name().replace("_", "").equalsIgnoreCase(name) ||
			   (isInteger(name) && Integer.valueOf(name) == type.getTypeId()))
				return type;
		}
		
		return null;
	}
	
	public static String stringToChatColor(String str){
		for(ChatColor c:ChatColor.values())
			str = str.replace("&" + c.getChar(), "" + c);
		
		return str;
	}
	
	public static String removeChatColor(String str){
		String s = str;
		
		for(ChatColor color:ChatColor.values())
			s = s.replace("" + color, "");
		
		return s;
	}
}