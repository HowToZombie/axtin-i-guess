/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.axtin.modules.kitsystem;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.axtin.container.facade.Container;
import org.axtin.modules.shulkercrates.ParticleColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

/**
 *
 * @author Alan Tavakoli
 */
public class KitHandler implements Listener {

    List<Kit> kits;
    File directory;
    File configFile;
    File dataFile;
    YamlConfiguration dataConfig;
    YamlConfiguration config;
    Map<Block, Player> shulkers;
    Map<Block, Integer> runnables;
    
    public KitHandler() {
        Bukkit.getPluginManager().registerEvents(this, Container.get(Plugin.class));
        directory = new File(Container.get(Plugin.class).getDataFolder().getAbsolutePath() + "/Kits/");
        configFile = new File(directory, "Kits.yml");
        dataFile = new File(directory, "Data.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        config = YamlConfiguration.loadConfiguration(configFile);
        shulkers = new HashMap<>();
        runnables = new HashMap<>();
        try {
            config.save(configFile);
            dataConfig.save(dataFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void init() {
        kits = Kit.getKits(config);
    }
    
    public void reload() {
    	dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        config = YamlConfiguration.loadConfiguration(configFile);
        kits = Kit.getKits(config);
    }
    
    
    boolean kitExists(String kitName) {
        return kits.stream().anyMatch((kit) -> (kit.getName().equalsIgnoreCase(kitName)));
    }
    
    
    Kit getKit(String kitName) {
        for(Kit kit : kits) 
            if(kit.getName().equalsIgnoreCase(kitName))
                return kit;
        return null;
    }
    
    public void addShulker(Block block, Player recipent) {
        shulkers.put(block, recipent);
        BukkitRunnable br = new BukkitRunnable() {
            @Override
            public void run() {
                if(shulkers.containsKey(block)) {
                	ParticleColor color = ParticleColor.getAppropiateColor(block);
                    block.setType(Material.AIR);
                    color.playEffect(block.getLocation());
                    shulkers.remove(block);
                    runnables.remove(block);
                }
            }
            
        };
        br.runTaskLater(Container.get(Plugin.class), 600);
        runnables.put(block, br.getTaskId());
    }
    
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
    	if(shulkers.containsKey(e.getBlock())) {
    		e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot break that."));
    	}
    }
    
    public void removeShulker(Block block) {
    	if(shulkers.containsKey(block)) {
    		
    		if(block.getType() != Material.AIR) {
    			ParticleColor color = ParticleColor.getAppropiateColor(block);
    			block.setType(Material.AIR);
    			color.playEffect(block.getLocation());
    		}
    		shulkers.remove(block);
    		runnables.remove(block);
    	}
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
    	if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
    		if(shulkers.containsKey(e.getClickedBlock())) {
    			if(shulkers.get(e.getClickedBlock()) != e.getPlayer())
    				e.setCancelled(true);
    			else 
    	    		Bukkit.getScheduler().cancelTask(runnables.get(e.getClickedBlock()));
    		}
    	}
    }
    
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
    	if(e.getInventory().getHolder() instanceof ShulkerBox) {
    		ShulkerBox block = (ShulkerBox) e.getInventory().getHolder();
    		if(shulkers.containsKey(block.getBlock())) {
    			removeShulker(block.getBlock());
    		}
    	}
    }
    
    public void shutDown() {
        config = YamlConfiguration.loadConfiguration(configFile);
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        this.kits.forEach((kit) -> {
            kit.saveCooldowns(dataConfig);
            //kit.save(config);
        });
        try {
            //config.save(configFile);
            dataConfig.save(dataFile);
        } catch (IOException ex) {
            Logger.getLogger(KitHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
