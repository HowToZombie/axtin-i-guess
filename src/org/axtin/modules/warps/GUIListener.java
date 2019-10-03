package org.axtin.modules.warps;

import org.axtin.container.facade.Container;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked().getOpenInventory().getTopInventory().getTitle().contains("Warps")) {
			if(e.getCurrentItem() != null) {
				ItemStack is = e.getCurrentItem();
				WarpManager manager = Container.get(WarpManager.class);
				Player player = (Player) e.getWhoClicked();
				String warpName = ChatColor.stripColor(is.getItemMeta().getDisplayName());
				if(manager.warpExists(warpName)) {
					Warp warp = manager.getWarp(warpName);
					if(manager.canWarp(player, warp)) {
						player.teleport(warp.getTarget());
						player.closeInventory();
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Swoosh..."));
					}
				}
			}
			e.setCancelled(true);
		}
	}

}
