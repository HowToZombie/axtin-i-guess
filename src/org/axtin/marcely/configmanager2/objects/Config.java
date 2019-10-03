package org.axtin.marcely.configmanager2.objects;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.axtin.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Config {
	
	public static final byte TYPE_TREE = 0x0;
	public static final byte TYPE_CONFIG = 0x1;
	public static final byte TYPE_COMMENT = 0x2;
	public static final byte TYPE_EMPTYLINE = 0x3;
	public static final byte TYPE_DESCRIPTION = 0x4;
	public static final byte TYPE_LISTITEM = 0x5;
	
	private final String name, rawLine;
	private final Tree parent;
	
	private String value;
	
	public Config(String rawLine, String name, Tree parent){
		this(rawLine, name, parent, (String) null);
	}
	
	public Config(String rawLine, String name, Tree parent, String value){
		this.name = name;
		this.rawLine = rawLine;
		this.parent = parent;
		this.value = value;
	}
	
	public Config(String rawLine, String name, Tree parent, Location loc){
		this.name = name;
		this.rawLine = rawLine;
		this.parent = parent;
		setValue(loc);
	}
	
	public byte getType(){
		return TYPE_CONFIG;
	}
	
	public String getAbsolutePath(){
		return parent != null ? parent.getAbsolutePath() + "." + name : "";
	}
	
	public @Nullable Boolean getValueAsBoolean(){
		return null;
	}
	
	// Util
	private static @Nullable Boolean getBoolean(String str){
		return Boolean.valueOf(str);
	}
	
	public static List<String> valuesToString(List<Config> configs){
		final List<String> list = new ArrayList<String>();
		
		for(Config c:configs)
			list.add(c.value);
		
		return list;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Tree getParent(){
		return this.parent;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public @Nullable Location getValueAsLocation(){
		final String[] strs = getValue().split(",");
		
		if(strs.length == 6 && Util.isDouble(strs[0]) &&
		   Util.isDouble(strs[1]) && Util.isDouble(strs[2]) &&
		   Util.isDouble(strs[3]) && Util.isDouble(strs[4])){
			final World world = Bukkit.getWorld(strs[5]);
			
			if(world != null)
				return new Location(world, Double.valueOf(strs[0]), Double.valueOf(strs[1]), Double.valueOf(strs[2]),
						(float) (double) Double.valueOf(strs[3]), (float) (double) Double.valueOf(strs[4]));
			else
				return null;
		}else
			return null;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public void setValue(Location loc){
		this.value = loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch() + "," + loc.getWorld().getName();
	}
	
	public String getRawLine(){
		return this.rawLine;
	}
}