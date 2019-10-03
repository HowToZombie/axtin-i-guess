package org.axtin.modules.spawnwarping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.axtin.container.facade.Container;
import org.axtin.modules.warps.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class WarpHandler {

	public Location guiTriggerLoc;
	public Location floatTriggerLoc;
	public Location endFloatPushTriggerLoc;
	public Location warpTrigger;
	private File dir = new File(Container.get(Plugin.class).getDataFolder() + "/Static Locations/");
	private File configFile;
	private YamlConfiguration config;
	private List<Player> shouldBeCleared;
	
	public HashMap<Player, Location> toTeleport = new HashMap<>();
	public HashMap<Player, FlameEffect> effects = new HashMap<>();
	
	public WarpHandler() {
		shouldBeCleared = new ArrayList<>();
		boolean shouldRead = dir.exists();
		if(!shouldRead) {
			dir.mkdirs();
		}
		 configFile = new File(dir, "Locations.yml");
		 config = YamlConfiguration.loadConfiguration(configFile);
		 if(shouldRead) {
			 if(config.getConfigurationSection("Locations") != null) {
				 if(config.isString("Locations.GUITrigger"))
					 guiTriggerLoc = stringToLocation(config.getString("Locations.GUITrigger"));
				 if(config.isString("Locations.LevitationTrigger"))
					 floatTriggerLoc = stringToLocation(config.getString("Locations.LevitationTrigger"));
				 if(config.isString("Locations.EndOfLevitationTrigger"))
					 endFloatPushTriggerLoc = stringToLocation(config.getString("Locations.EndOfLevitationTrigger"));
				 if(config.isString("Locations.PortalTrigger"))
					 warpTrigger = stringToLocation(config.getString("Locations.PortalTrigger"));
			 }
			 
		 }
	}
	
	public void removeFromClearing(Player player) {
		if(shouldBeCleared.contains(player))
			shouldBeCleared.remove(player);
	}
	
	public void addToClearing(Player player) {
		if(!shouldBeCleared.contains(player))
			shouldBeCleared.add(player);
	}
	
	public boolean potionShouldBeRemoved(Player player) {
		return shouldBeCleared.contains(player);
	}
	
	public void shutDown() {
		if(guiTriggerLoc != null) {
			config.set("Locations.GUITrigger", locationToString(guiTriggerLoc));
		}
		if(floatTriggerLoc != null) {
			config.set("Locations.LevitationTrigger", locationToString(floatTriggerLoc));
		}
		if(endFloatPushTriggerLoc != null) {
			config.set("Locations.EndOfLevitationTrigger", locationToString(endFloatPushTriggerLoc));
		}
		if(warpTrigger != null) {
			config.set("Locations.PortalTrigger", locationToString(warpTrigger));
		}
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String locationToString(Location loc) {
		return String.format("%d:%d:%d:%s", loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName());
	}
	
	public Location stringToLocation(String str) {
		String[] values = str.split(":");
		double x = Double.valueOf(values[0]);
		double y = Double.valueOf(values[1]);
		double z = Double.valueOf(values[2]);
		World w = Bukkit.getWorld(values[3]);
		return new Location(w, x, y, z);
	}
	
	public int getAvailableWarpsAmount(Player player) {
		return getAvailableWarps(player).size();
	}
	
	public boolean isReady() {
		return guiTriggerLoc != null && floatTriggerLoc != null && endFloatPushTriggerLoc != null && warpTrigger != null;
	}
	
	public List<String> getAvailableWarps(Player player) {
		List<String> list = new ArrayList<>();
		for(Entry<String, Location> entry : WarpManager.warps.entrySet()) {
			if(player.hasPermission("warp." + entry.getKey()))
				list.add(entry.getKey());
		}
		return list;
	}
	
	public Location getWarp(String name) {
		for(Entry<String, Location> entry : WarpManager.warps.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(name))
				return entry.getValue();
		}
		return null;
	}
	
	public ItemStack[] getAvailableWarpsAsItemStacks(Player player) {
		List<ItemStack> list = new ArrayList<>();
		for(String str : getAvailableWarps(player)) {
			list.add(createItemStack(str));
		}
		return list.toArray(new ItemStack[list.size()]);
	}
	
	private ItemStack createItemStack(String name) {
		ItemStack is = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + name));
		is.setItemMeta(im);
		return is;
	}
	
}
