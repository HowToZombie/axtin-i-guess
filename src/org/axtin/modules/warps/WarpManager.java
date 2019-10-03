package org.axtin.modules.warps;

import org.axtin.container.facade.Container;
import org.axtin.user.UserData;
import org.axtin.user.UserRepository;
import org.axtin.util.FileStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Joseph on 3/20/2017.
 * Revisited by Ritolika
 */
public class WarpManager {
	
    public static HashMap<String,Location> warps = new HashMap<>();
    public ConfigHandler config;
    private List<Warp> warps2;
    
    public WarpManager() {
    	config = new ConfigHandler("Warps/Config", "Warps.yml");
    	this.warps2 = Warp.getWarps(Container.get(Plugin.class).getDataFolder().getAbsolutePath() + "/Warps/");
    }
    
    public void addWarp(String name, int rank, Location loc, WarpType type) {
    	if(!warpExists(name))
    		warps2.add(new Warp(name, loc, rank, type));
    }
    
    public void removeWarp(String name) {
    	getWarp(name).deleteFile();
    	warps2.removeIf(warp -> warp.name.equalsIgnoreCase(name));
    }
    
    public void editWarp(String name, int rank, Location loc, WarpType type) {
    	Warp edited = getWarp(name);
    	edited.name = name;
    	edited.reqRank = rank;
    	edited.target = locationToString(loc);
    	edited.type = type;
    }
    
    private String locationToString(Location loc) {
		return loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getWorld().getName();
	}
    
    public boolean warpExists(String name) {
    	return warps2.stream().anyMatch((warp) -> warp.name.equalsIgnoreCase(name));
    }
    
    public Warp getWarp(String name) {
    	if(warpExists(name))
    		return warps2.stream().
    				filter((warp) -> warp.name.equalsIgnoreCase(name)).
    				findFirst().
    				get();
		return null;
    }
    
    public void save() {
    	warps2.forEach((warp) -> warp.save());
    }
    
    public List<Warp> getWarpsFor8(Player player) {
    	UserData data = Container.get(UserRepository.class).getUser(player.getUniqueId()).getData();
    	List<Warp> list = warps2.stream().
    			filter(warp -> data.getPrisonRole().getIdentifier() >= warp.reqRank).
    			collect(Collectors.toList());
    	return list;
    }
    
    public boolean canWarp(Player player, Warp to) {
    	UserData data = Container.get(UserRepository.class).getUser(player.getUniqueId()).getData();
    	return data.getPrisonRole().getIdentifier() >= to.reqRank;
    }
    
    public List<Warp> getWarpsFor7(Player player) {
    	List<Warp> list = new ArrayList<Warp>();
    	UserData data = Container.get(UserRepository.class).getUser(player.getUniqueId()).getData();
    	for(Warp warp : warps2) {
    		if(data.getPrisonRole().getIdentifier() >= warp.reqRank)
    			list.add(warp);
    	}
    	return list;
    }
    
    public Inventory getGUIFor(Player player) {
    	Inventory inv = Bukkit.createInventory(null, appropiateSize(getWarpsFor8(player).size()), ChatColor.translateAlternateColorCodes('&', "&6Warps"));
    	inv.setContents(getWarpsFor8(player).stream().map(Warp::getItemRep).toArray(size -> new ItemStack[size]));;
    	return inv;
    }
    
    private int appropiateSize(int amount) {
		int i = 9;
		if(i > 9 && i <= 18)
			return 18;
		else if(i > 18 && i <= 27)
			return 27;
		else if(i > 27 && i <= 36)
			return 36;
		else if(i > 36 && i <= 45)
			return 45;
		else if(i > 45 && i <= 54)
			return 54;
		return i;
	}
	
    

    public void load(){
        File dir = new File(Container.get(Plugin.class).getDataFolder() + "/warps/");
        for(File file : dir.listFiles()) {
            FileStorage fs = new FileStorage(file.getName().replace(".yml", ""),"warps");
            YamlConfiguration config = fs.getConfig();
            Location to = new Location(Bukkit.getWorld(config.getString("Location.world")),config.getInt("Location.x"),config.getInt("Location.y"),config.getInt("Location.z"),config.getInt("Location.yaw"),config.getInt("Location.pitch"));
            WarpManager.warps.put(file.getName().replace(".yml",""),to);
        }
    }
}
