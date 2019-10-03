package org.axtin.modules.warps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Warp implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4485067042298404224L;
	protected String name;
	protected String target;
	protected int reqRank;
	protected WarpType type;
	
	public Warp(String name, Location target, int reqRank, WarpType type) {
		this.target = locationToString(target);
		this.reqRank = reqRank;
		this.name = name;
		this.type = type;
	}
	
	public Warp(YamlConfiguration config, String name) {
		String path = "Warps." + name;
		this.name = name;
		this.target = config.getString(path + ".Location");
		this.reqRank = config.getInt(path + ".Required");
	}
	
	public static Warp getWarp(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Warp result = (Warp) ois.readObject();
			ois.close();
			return result;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void save(YamlConfiguration config) {
		String path = "Warps." + name + ".";
		config.set(path + "Location", this.target);
		config.set(path + "Required", this.reqRank);
	}
	
	private String locationToString(Location loc) {
		return loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getWorld().getName();
	}
	
	public ItemStack getItemRep() {
		ItemStack is = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + this.name));
		im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&7Click to warp!")));
		is.setItemMeta(im);
		return is;
	}
	
	public void save() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(Container.get(Plugin.class).getDataFolder().getAbsolutePath() + "/Warps/" + name + ".warp");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteFile() {
		File file = new File(Container.get(Plugin.class).getDataFolder().getAbsolutePath() + "/Warps/" + name + ".warp");
		if(file.exists())
			file.delete();
	}
	
	public static List<Warp> getWarps(String dir) {
		List<Warp> list = new ArrayList<>();
		File directory = new File(dir);
		if(!directory.exists())
			directory.mkdirs();
		File[] files = directory.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".warp");
			}
			
		});
		
		for(File file : files) 
			list.add(getWarp(file));
		
		return list;
	}
	
	private Location stringToLocation(String str) {
		String[] values = str.split(":");
		double x = Double.valueOf(values[0]);
		double y = Double.valueOf(values[1]);
		double z = Double.valueOf(values[2]);
		World w = Bukkit.getWorld(values[3]);
		return new Location(w, x, y, z);
	}
	
	public Location getTarget() {
		return stringToLocation(target);
	}
	
	public String getName() {
		return this.name;
	}
	
}
