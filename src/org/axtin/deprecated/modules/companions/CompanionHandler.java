package org.axtin.deprecated.modules.companions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.axtin.container.facade.Container;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.EvictingQueue;

public class CompanionHandler {

	public List<Companion> companions;
	private Map<Player, EvictingQueue<Location>> trails;
	public final File dir;
	private File configurationFile;
	private YamlConfiguration config;
	
	public CompanionHandler() {
		companions = new ArrayList<>();
		trails = new HashMap<>();
		dir = new File(Container.get(Plugin.class).getDataFolder().getAbsolutePath() + "/Companions/");
		if(!dir.exists())
			dir.mkdir();
		this.configurationFile = new File(dir, "Data.yml");
		config = YamlConfiguration.loadConfiguration(configurationFile);
	}
	
	public boolean hasActiveCompanion(Player player) {
		if(hasCompanion(player)) {
			Companion comp = getCompanion(player);
			return comp.spawned;
		} else {
			return false;
		}
	}
	
	public void disable() {
		if(!companions.isEmpty()) {
			for(Companion comp : companions) {
				if(comp.isSpawned()) {
					comp.hide();
				}
			}
		}
	}
	
	public Companion getCompanion(ArmorStand as) {
		return companions.stream().filter(com -> com.as.equals(as)).findFirst().get();
	}
	
	public boolean hasCompanionInConfiguration(Player player) {
		return config.getConfigurationSection("Companions." + player.getUniqueId().toString() + ".Settings") 
				!= null;
	}
	
	public Companion getCompanionFromConfiguration(Player player) {
		if(hasCompanionInConfiguration(player)) {
			Companion companion = new Companion(player.getUniqueId(), config);
			companions.add(companion);
			return companion;
		}
		return null;
	}
	
	public Companion getCompanion(Player player) {
		return companions.stream().filter(com -> com.owner.equals(player.getUniqueId())).findFirst().get();
	}
	
	public boolean hasCompanion(Player player) {
		return companions.stream().anyMatch(com -> com.owner.equals(player.getUniqueId()));
	}
	
	public boolean hasTrail(Player player) {
		return trails.containsKey(player);
	}
	
	public EvictingQueue<Location> getTrail(Player player) {
		if(hasTrail(player)) 
			return trails.get(player);
		else
			return null;
	}
	
	public void addToQueue(Player player, Location loc) {
		if(!hasTrail(player)) 
			trails.put(player, EvictingQueue.create(100));
		Queue<Location> trail = trails.get(player);
		
		trail.offer(loc);
		
	}
	
	public Companion createCompanion(Player player) {
		Companion comp = new Companion(player);
		comp.spawn(player, "&c128198", CmpnType.BLUE);
		companions.add(comp);
		return comp;
	}
	
	public Companion getAbsoluteCompanion(Player player) {
		if(hasCompanion(player)) {
			return getCompanion(player);
		} else if(hasCompanionInConfiguration(player)) {
			return getCompanionFromConfiguration(player);
		} else {
			Companion companion = new Companion(player);
			companions.add(companion);
			return companion;
		}
	}
	
	public void stop() {
		for(Companion companion : companions) {
			companion.save("Companions.", config);
		}
		try {
			config.save(configurationFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
