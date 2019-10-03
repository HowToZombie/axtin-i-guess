package org.axtin.util.skyfall;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class FallingChest {

	public static List<FallingChest> fallingChests = new ArrayList<>();
	
	private boolean landed = false;
	private Block block = null;
	private FallingBlock fb = null;
	private ChestOpenHandler openHandler = null;
	private ChestCloseHandler closeHandler = null;
	private Location target;
	private int id;
	private Inventory inventory;
	
	
	public static FallingChest getById(int id) {
		for(FallingChest fc : fallingChests) {
			if(fc.getId() == id)
				return fc;
		}
		return null;
	}
	
	public static FallingChest getByInventory(Inventory inventory) {
		for(FallingChest fc : fallingChests) {
			if(fc.getInventory().equals(inventory))
				return fc;
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public FallingChest(Location target, int dropHeight, String invName) {
		this.target = target;
		Location spawn = target.getBlock().getLocation().add(0.0, dropHeight, 0.0);
		fb = target.getWorld().spawnFallingBlock(spawn, Material.CHEST, (byte) 0);
		fb.setMetadata("fallingchest", new FixedMetadataValue(Container.get(Plugin.class), Boolean.valueOf(true)));
		fb.setMetadata("id", new FixedMetadataValue(Container.get(Plugin.class), Integer.valueOf(id)));
		fallingChests.add(this);
		Random rnd = new Random();
		id = 100000 + rnd.nextInt(900000);
		inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', invName));
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public boolean hasLanded() {
		return landed;
	}
	
	public FallingBlock getFallingBlock() {
		return fb;
	}
	
	public void setLanded(Block block) {
		this.landed = true;
		this.block = block;
		this.fb = null;
	}
	
	public Block getBlock() {
		return this.block;
	}
	
	public Chest getChest() {
		if(landed) {
			if(block.getType() == Material.CHEST) {
				return (Chest) block.getState(); 
			}
		} else {
			if(fb != null) {
				if(!fb.isDead()) {
					fb.remove();
					this.target.getBlock().setType(Material.CHEST);
					return (Chest) this.target.getBlock().getState();
				}
			}
		}
		return null;
	}
	
	public void remove() {
		if(landed) {
			for(HumanEntity he : inventory.getViewers()) {
				closeChest((Player) he);
			}
			block.setType(Material.AIR);
		} else {
			if(fb != null) {
				if(!fb.isDead())
					fb.remove();
			}
		}
	}
	
	public int getId() {
		return id;
	}
	
	public void setChestOpenHandler(ChestOpenHandler openHandler) {
		this.openHandler = openHandler;
	}
	
	public void openChest(Player player) {
		if(landed) {
			changeChestState(block.getLocation(), true);
			player.openInventory(inventory);
			if(hasChestOpenHandler()) {
				
				getChestOpenHandler().handle(player, inventory);
				
			}
		}
	}
	
	public void closeChest(Player player) {
		if(landed) {
			changeChestState(block.getLocation(), false);
			player.closeInventory();
			
			if(hasChestCloseHandler()) {
				
				getChestCloseHandler().handle(player, inventory);
				
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void changeChestState(Location loc, boolean open) {
	    for (Player p : loc.getWorld().getPlayers()) {
	        p.playNote(loc, (byte) 1, (byte) (open ? 1 : 0));
	    }
	}
	
	public boolean hasChestOpenHandler() {
		return openHandler != null;
	}
	
	public boolean hasChestCloseHandler() {
		return closeHandler != null;
	}
	
	public ChestOpenHandler getChestOpenHandler() {
		return this.openHandler;
	}

	public ChestCloseHandler getChestCloseHandler() {
		return closeHandler;
	}

	public void setChestCloseHandler(ChestCloseHandler closeHandler) {
		this.closeHandler = closeHandler;
	}
	
}
