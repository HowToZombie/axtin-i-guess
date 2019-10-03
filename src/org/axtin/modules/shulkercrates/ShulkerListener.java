package org.axtin.modules.shulkercrates;

import org.axtin.container.facade.Container;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import net.md_5.bungee.api.ChatColor;

public class ShulkerListener implements Listener {
	


	
	
	@EventHandler
	public void playerLoginEvent(PlayerLoginEvent e) {
		if(!Container.get(ShulkerHandler.class).shulkers.containsKey(e.getPlayer())) {
			ShulkerInventoryHolder inv = Container.get(ShulkerHandler.class).dataHandler.get(e.getPlayer());
			if(inv == null) 
				inv = new ShulkerInventoryHolder(e.getPlayer().getUniqueId());
			else
				Container.get(ShulkerHandler.class).shulkers.put(inv.getUUID(), inv);
		}
	}
	
	@EventHandler
	public void blockChange(ShulkerPlaceEvent e) {
				Player belongTo = e.getWhoItBelongsTo();
				ShulkerBox sb = e.getShulkerBox();
				ShulkerInventory si = Container.get(ShulkerHandler.class).shulkers.get(belongTo.getUniqueId()).getInv(e.getWhich());
				si.setContents(sb.getInventory());
				Container.get(ShulkerHandler.class).shulkBoxes.put(sb, belongTo);
				Container.get(ShulkerHandler.class).whichBox.put(sb, e.getWhich());
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(e.getBlock().getState() instanceof ShulkerBox) {
			ShulkerBox sb = (ShulkerBox) e.getBlock().getState();
			if(Container.get(ShulkerHandler.class).shulkBoxes.containsKey(sb)) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot break that."));
			}
		}
	}
	
	@EventHandler
	public void playerOpenInventory(InventoryOpenEvent e) {
		if(e.getInventory().getHolder() instanceof ShulkerBox) {
    		ShulkerBox sb = (ShulkerBox) e.getInventory().getHolder();
    		if(Container.get(ShulkerHandler.class).shulkBoxes.containsKey(sb)) {
    			Player player = (Player) e.getPlayer();
    			if(Container.get(ShulkerHandler.class).shulkBoxes.get(sb).equals(player)) {
    				ShulkerInventoryHolder sih = Container.get(ShulkerHandler.class).shulkers.get(player.getUniqueId());
    				sih.hasOpened = true;
    			} else 
    				e.setCancelled(true);
    			
    		}
		}
	}
	
	
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(Container.get(ShulkerHandler.class).shulkBoxes.containsKey(e.getClickedBlock())) {
				if(e.getPlayer() != Container.get(ShulkerHandler.class).shulkBoxes.get(e.getPlayer()))
					e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void closeInv(InventoryCloseEvent e) {
        if(e.getInventory().getHolder() instanceof ShulkerBox){
        	if(Container.get(ShulkerHandler.class).shulkBoxes.containsKey((ShulkerBox) e.getInventory().getHolder())){
        		ShulkerBox sb = (ShulkerBox) e.getInventory().getHolder();
        		if(Container.get(ShulkerHandler.class).shulkBoxes.get(sb) != (Player)e.getPlayer()) 
        			return;
        		int which = Container.get(ShulkerHandler.class).whichBox.get(sb);
        		ShulkerInventory inv = Container.get(ShulkerHandler.class).shulkers.get(e.getPlayer().getUniqueId()).getInv(which);
        		ShulkerInventoryHolder sih = Container.get(ShulkerHandler.class).shulkers.get(e.getPlayer().getUniqueId());
        		inv.update(sb.getInventory());
        		sih.hasOpened = false;
        		Location staticLoc = sb.getLocation();
        		Container.get(ShulkerHandler.class).remove(sb);
        		ParticleColor color = ParticleColor.getAppropiateColor(staticLoc.getBlock());
        		staticLoc.getBlock().setType(Material.AIR);
        		color.playEffect(staticLoc);
        	}
        }

	}
	
	

}
