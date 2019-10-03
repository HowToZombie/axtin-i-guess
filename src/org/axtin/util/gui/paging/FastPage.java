package org.axtin.util.gui.paging;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FastPage {
	
	private Inventory inventory;
	private Page parent;
	private List<PageItem> items;
	private ReadWriteLock lock;
	
	public FastPage(Page parent) {
		this.parent = parent;
		this.inventory = Bukkit.createInventory(null, 54, "Fast page test: " + parent.getIndex());
		this.items = parent.getItems();
		this.lock = new ReentrantReadWriteLock();
		this.load();
	}
	
	public void load() {
		BukkitRunnable loader = new BukkitRunnable() {

			int i = 0;
			boolean firstTime = true;
			@Override
			public void run() {
				
				if(firstTime) {
					
					for(int i = 45; i < 54; i++) {
						inventory.setItem(i, getGlassPane(14, "-"));
					}
					
					firstTime = false;
				}
				
				if( i < 45 ) {
					
					lock.writeLock().lock();
					
					try {
						
						PageItem item = getItem(i);
						
						if(item != null)
							inventory.setItem(i, item.getItem());
						
						
					} finally {
						lock.writeLock().unlock();
					}
					
					i++;
					
					int percentage = percentage(i, 44);
					int slotsToFill = getSlotsToFill(percentage);
					Bukkit.broadcastMessage("Percentage: " + percentage + " Slots to fill: " + slotsToFill);
					int i = 0;
					
					for(int slot = 45; slot < 54; slot++) {
						if(i < slotsToFill) {
							inventory.setItem(slot, getGlassPane(5, "-"));
						}
						i++;
					}
					
					
				} else {
					this.cancel();
				}
				
				
				
			}
			
		};
		loader.runTaskTimerAsynchronously(Container.get(Plugin.class), 1, 1);
	}
	
	private int percentage(int n, int v) {
		return (n * 100) / v;
	}
	
	public PageItem getItem(int slot) {
		return items.stream().filter(item -> item.getSlot() == slot).findFirst().orElse(null);
	}
	
	@SuppressWarnings("deprecation")
	private ItemStack getGlassPane(int color, String name) {
		ItemStack is = new ItemStack(160, 1, (short) color);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	
	public synchronized void updateItems() {
		lock.readLock().lock();
		
		try {
			this.items = parent.getItems();
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	private int getSlotsToFill(int percentage) {
		if(percentage > 11 && percentage < 22) {
			return 1;
		} else if (percentage >= 22 && percentage < 33) {
			return 2;
		} else if (percentage >= 33 && percentage < 44) {
			return 3;
		} else if (percentage >= 44 && percentage < 55) {
			return 4;
		} else if (percentage >= 55 && percentage < 66) {
			return 5;
		} else if (percentage >= 66 && percentage < 77) {
			return 6;
		} else if (percentage >= 77 && percentage < 88) {
			return 7;
		} else if (percentage >= 88 && percentage < 99) {
			return 8;
		} else if (percentage >= 99 && percentage < 103) {
			return 9;
		} else {
			return 0;
		}
	}
	
}
