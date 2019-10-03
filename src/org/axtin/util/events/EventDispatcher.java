package org.axtin.util.events;

import org.axtin.container.facade.Container;
import org.axtin.util.skyfall.ChestCloseHandler;
import org.axtin.util.skyfall.ChestLandEvent;
import org.axtin.util.skyfall.ChestOpenHandler;
import org.axtin.util.skyfall.FallingChest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class EventDispatcher implements Listener {
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent e) {
		
		if(e.getEntity() instanceof FallingBlock) {
			
			FallingBlock fb = (FallingBlock) e.getEntity();
			
			if(fb.getMaterial() == Material.CHEST) {
				
				if(fb.hasMetadata("fallingchest")) {
					
					for(FallingChest fc : FallingChest.fallingChests) {
						
						if(fc.getFallingBlock() == fb) {
							
							fc.setLanded(e.getBlock());
							
							e.getBlock().setMetadata("id", new FixedMetadataValue(Container.get(Plugin.class), Integer.valueOf(fc.getId())));
							e.getBlock().setMetadata("fallingchest", new FixedMetadataValue(Container.get(Plugin.class), Boolean.valueOf(true)));
							
							Bukkit.getPluginManager().callEvent(new ChestLandEvent(fc));
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onInteract(PlayerInteractEvent e) {
		
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if(e.getClickedBlock().getType() == Material.CHEST) {
				
				if(e.getClickedBlock().hasMetadata("fallingchest")) {
					
					Block block = e.getClickedBlock();
					int id = block.getMetadata("id").get(0).asInt();
					
					FallingChest fc = FallingChest.getById(id);
					e.setCancelled(true);
					
					if(fc != null) {
						
						fc.openChest(e.getPlayer());
						
						
					}
					
					
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onClose(InventoryCloseEvent e) {
		
		FallingChest fc = FallingChest.getByInventory(e.getInventory());
		
		if(fc != null) {
			
			fc.closeChest((Player) e.getPlayer());
			
		}
		
	}
	
	//Example of listening to click event
	public void onClickExample(InventoryClickEvent e) {
		
		Inventory top = e.getView().getTopInventory();
		FallingChest fc = FallingChest.getByInventory(top);
		
		if(fc != null) {
			
			Inventory bottom = e.getView().getBottomInventory();
			
			if(e.getClickedInventory() == top) {
				e.setCancelled(true);
			}
			else if(e.getClickedInventory() == bottom) {
				e.setCancelled(true);
			}
			
		}
		
	}
	
	public void createFallingChest(Location target) {
		FallingChest fc = new FallingChest(target, 5, "&cCustom chest");
		fc.setChestOpenHandler(new ChestOpenHandler() {
			
			@Override
			public void handle(Player player, Inventory inventory) {
				player.sendMessage("test");
				
			}
		});
		fc.setChestCloseHandler(new ChestCloseHandler() {
			
			@Override
			public void handle(Player player, Inventory inventory) {
				player.sendMessage("test close");
				
			}
		});
	}
	
}
