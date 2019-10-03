package org.axtin.modules.gambling.roulette;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum BidColor {

	RED, GREEN, BLACK;
	
	public Material getMaterial() {
		switch(this) {
		case BLACK:
			return Material.COAL;
		case GREEN:
			return Material.EMERALD;
		case RED:
			return Material.REDSTONE;
		default:
			return Material.EMERALD;
		}
	}
	
	public int getColor() {
		
		switch(this) {
		case BLACK:
			return 15;
		case GREEN:
			return 5;
		case RED:
			return 14;
		default:
			return 14;
		}
		
	}
	
	public ItemStack toGlassPane() {
		switch(this) {
		case BLACK:
			return getGlassPane(15);
		case GREEN:
			return getGlassPane(5);
		case RED:
			return getGlassPane(14);
		default:
			return getGlassPane(5);
		}
	}
	
	public ItemStack toItem() {
		switch(this) {
		case BLACK:
			return getItemStack(BLACK);
		case GREEN:
			return getItemStack(GREEN);
		case RED:
			return getItemStack(RED);
		default:
			return getItemStack(GREEN);
		}
	}
	
	@SuppressWarnings("deprecation")
	private ItemStack getGlassPane(int color) {
		ItemStack is = new ItemStack(160, 1, (short) color);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(this.getName(false));
		is.setItemMeta(im);
		return is;
	}
	
	private ItemStack getItemStack(BidColor color) {
		ItemStack is = new ItemStack(color.getMaterial());
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(color.getName(false));
		is.setItemMeta(im);
		return is;
	}
	
	
	public static BidColor getByItemStack(ItemStack is) {
		switch(is.getType()) {
		case COAL:
			return BidColor.BLACK;
		case EMERALD:
			return BidColor.GREEN;
		case REDSTONE:
			return BidColor.RED;
		default: 
			return BidColor.RED;
		}
	}
	
	public static BidColor getByItemStack2(ItemStack is) {
		switch(is.getDurability()) {
		case (short) 15:
			return BidColor.BLACK;
		case (short) 5:
			return BidColor.GREEN;
		case (short) 14:
			return BidColor.RED;
		default:
			return BidColor.GREEN;
		}
	}
	
	public String getName(boolean caps) {
		String str;
		switch(this) {
		case BLACK:
			str = ChatColor.translateAlternateColorCodes('&', "&8BLACK");
			break;
		case GREEN:
			str = ChatColor.translateAlternateColorCodes('&', "&aGREEN");
			break;
		case RED:
			str = ChatColor.translateAlternateColorCodes('&', "&cRED");
			break;
		default:
			str = ChatColor.translateAlternateColorCodes('&', "&aGREEN");
			break;
		}
		if(!caps) {
			str = str.toLowerCase();
			str = StringUtils.capitalize(str);
		}
		return str;
	}
	
	public static BidColor getByString(String arg0) {
		if(arg0.equalsIgnoreCase("red")) {
			return BidColor.RED;
		} else if(arg0.equalsIgnoreCase("green")) {
			return BidColor.GREEN;
		} else if(arg0.equalsIgnoreCase("black")) {
			return BidColor.BLACK;
		} else {
			return getClosest(arg0);
		}
	}
	
	public static BidColor getClosest(String arg0) {
		List<String> list = Arrays.asList("black", "red", "green");
		List<Character> chars2 = new ArrayList<>();
		
		for(Character c : arg0.toCharArray()) {
			chars2.add(c);
		}
		
		Map<Integer, Integer> index = new HashMap<>();
		for(int i = 0; i < 3; i++) {
			int points = 0;
			List<Character> chars = new ArrayList<>();
			String curr = list.get(i);
			
			for(Character c : curr.toCharArray()) {
				chars.add(c);
			}
			
			for(Character c : chars2) {
				if(chars.contains(c)) {
					points++;
					chars.remove(c);
				}
			}
			
			index.put(i, points);
		}
		
		Entry<Integer, Integer> closestMatch = null;
		
		for(Entry<Integer, Integer> entry : index.entrySet()) {
			if(closestMatch == null){
				closestMatch = entry;
			} else {
				if(entry.getValue() > closestMatch.getValue()) {
					closestMatch = entry;
				}
			}
		}
		String result = list.get(closestMatch.getKey());
		return getByString(result);
		
		
	}
	
	
	
}
