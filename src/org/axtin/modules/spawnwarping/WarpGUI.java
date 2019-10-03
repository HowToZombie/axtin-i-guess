package org.axtin.modules.spawnwarping;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class WarpGUI {

	protected Inventory inventory;
	
	public WarpGUI(Player player) {
		int amount = Container.get(WarpHandler.class).getAvailableWarpsAmount(player);
		int size = appropiateSize(amount);
		inventory = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', "&6Warps"));
		inventory.setContents(Container.get(WarpHandler.class).getAvailableWarpsAsItemStacks(player));
		player.openInventory(inventory);
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
	
	
}
