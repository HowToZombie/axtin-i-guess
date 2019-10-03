package org.axtin.modules.crates;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TempCrate {

	private Crate crate;
	private int itemAmount;
	private Player target;
	private Inventory inventory;
	private List<CrateItem> items; 
	int pointer = 0;
	private BukkitRunnable update;
	
	public TempCrate(Crate crate, int itemAmount, Player target) {
		this.crate = crate;
		this.itemAmount = itemAmount;
		this.target = target;
		
		inventory = Bukkit.createInventory(null, 27, color("&9Crate: " + crate.getName()));
		for(int i = 0; i < 9; i++) {
			inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
		}
		for(int i = 18; i < 27; i++) {
			inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
		}
		
		inventory.setItem(4, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3));
		inventory.setItem(22, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3));
		items = crate.getItems(itemAmount);
		this.update = getUpdate(target);
	}
	
	private BukkitRunnable getUpdate(Player player) {
		return new BukkitRunnable() {
			int ticks = 0;
			int modulo = 1;
			@Override
			public void run() {
				if(ticks % modulo == 0) {
					List<CrateItem> tempItems = getItemsFrom(pointer, 9);
					setItems(inventory, 
							tempItems.stream().map(item -> item.getDisplayItem())
							.collect(Collectors.toList())
							);
				}
				
				if(pointer + 2 > items.size())
					pointer = 0;
				else
					pointer++;
				
				modulo = getModulo(ticks);
				ticks++;
				if(ticks > 9*20) {
					CrateHandler handler = Container.get(CrateHandler.class);
					CrateItem item = handler.getItem(inventory.getItem(13));
					//item.give(player);
					this.cancel();
				}
			}
		};
	}
	
	private int getModulo(int ticks) {
		int modulo = 1;
		if(ticks > 5)
			modulo = 3;
		if(ticks > 2*20) 
			modulo = 5;
		if(ticks > 5*20)
			modulo = 10;
		if(ticks > 6*20)
			modulo = 15;
		if(ticks > 7*20)
			modulo = 20;
		if(ticks > 8*20)
			modulo = 40;
		return modulo;
	}
	
	private void setItems(Inventory inv, List<ItemStack> list) {
		int index = 0;
		for(int i = 9 ; i < 18; i++) {
			inv.setItem(i, list.get(index));
			index++;
		}
	}

	private String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	private List<CrateItem> getItemsFrom(int index, int amount) {
		List<CrateItem> temp = new ArrayList<>();
    	int point = index;
    	int left = new Integer(amount);
    	while( point <= items.size() - 1 ) {
    		temp.add(items.get(point));
          	left--;
          	point++;
          	if(left == 0)
          		break;
    	}
    	if(left > 1) {
    		temp.addAll(getItemsFrom(0, left));
    	}
		return temp;
	}
	
	public void open() {
		this.target.openInventory(inventory);
		this.update.runTaskTimer(Container.get(Plugin.class), 2, 1);
	}
	
	
	
}
