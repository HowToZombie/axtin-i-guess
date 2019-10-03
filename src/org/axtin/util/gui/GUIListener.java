package org.axtin.util.gui;

import org.axtin.container.facade.Container;
import org.axtin.util.ItemStackUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GUIListener implements Listener {
	
	public void closeAll(){
		for(Player player:GUI.openInventories.keySet())
			player.closeInventory();
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event){
		final Player player = (Player) event.getWhoClicked();
		
		// clicking on GUIItem & cancel if
		if(GUI.openInventories.containsKey(player)){
			final SimpleGUI gui = GUI.openInventories.get(player);
			
			if(event.getClickedInventory() != null && event.getClickedInventory().getType() != InventoryType.PLAYER && gui.isCancellable()){
				event.setCancelled(true);
				
				if(gui instanceof Clickable){
					final Clickable cgui = (Clickable) gui;
					final GUIItem item = cgui.getItemAt(event.getSlot());
					
					if(item != null)
						item.onClick(player, event.isLeftClick(), event.isShiftClick());
				
				}else if(gui instanceof AnvilGUI && event.getSlot() == 2){
					final AnvilGUI agui = (AnvilGUI) gui;
					
					if(agui.getWroteListener() != null){
						final ItemStack is = event.getInventory().getItem(2);
						
						if(is != null){
							final String msg = ItemStackUtil.getName(is);
							
							if(msg.length() >= 1){
								player.closeInventory();
								agui.getWroteListener().run(msg);
							}
						}
					}
				}
			}
			
		}
	}
	
	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent event){
		final Player player = (Player) event.getWhoClicked();
		
		// cancel if clicking on GUI
		if(GUI.openInventories.containsKey(player) && event.getInventory().getType() != InventoryType.PLAYER && GUI.openInventories.get(player).isCancellable())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event){
		final Player player = (Player) event.getPlayer();
		
		// remove player from openInventories if he's inside
		if(GUI.openInventories.containsKey(player)){
			if(GUI.cachePlayers.contains(player))
				GUI.cachePlayers.remove(player);
			else
				GUI.openInventories.get(player).onClose(player);
			
			GUI.openInventories.remove(player);
			
			if(GUI.openInventoriesDelayed.containsKey(player)){
				new BukkitRunnable(){
					public void run(){
						GUI.openInventoriesDelayed.remove(player);
					}
				}.runTaskLater(Container.get(Plugin.class), 1);
			}
		}
	}
}
