package org.axtin.util.gui.paging;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

public class PageContainer implements Listener {
	
//	protected List<Inventory> pages;
	protected List<Page> pages;
	protected List<PageItem> items;
	private int update;
	private BukkitRunnable updater;
	
	public PageContainer(int update) {
		Bukkit.getPluginManager().registerEvents(this, Container.get(Plugin.class));
		this.pages = new ArrayList<>();
		this.items = new ArrayList<>();
		this.update = update;
		
		this.updater = new BukkitRunnable() {
			
			@Override
			public void run() {
				refresh();		
			}
		};
		
		this.updater.runTaskTimerAsynchronously(Container.get(Plugin.class), 20, 20 * this.update);
		
	}
	
	public void createPages(List<PageItem> items) {
	
		this.pages = PageUtil.getPages(items.size(), this);
		
		if(this.pages.isEmpty())
			this.pages.add(new Page(this, this.pages.size() + 1));
		
		this.items = items;
	}
	
	public void fillItems() {
		
		List<List<PageItem>> partitions = Lists.partition(items, 45);
		
		if(partitions.isEmpty())
			return;
		
		int page = 0;
		int slot = 0;
		
		for(List<PageItem> partition : partitions) {
			Page active = pages.get(page);
			
			for(PageItem item : partition) {
				active.setItem(slot, item);
				slot++;
			}
			
			page++;
			slot = 0;
		}
	}
	
	public void open(Player player) {
		player.openInventory(pages.get(0).getInventory());
	}
	
	public void setItem(int gSlot, PageItem item) {
		/*Tuple<Integer, Integer> pageAndSlot = convertToPageAndSlot(gSlot);
		Inventory inv = pages.get(pageAndSlot._x).getInventory();
		inv.setItem(pageAndSlot._y, item.getItem());*/
	}
	
	public void setItem(int gSlot, ItemStack item) {
		/*Tuple<Integer, Integer> pageAndSlot = convertToPageAndSlot(gSlot);
		Inventory inv = pages.get(pageAndSlot._x).getInventory();
		inv.setItem(pageAndSlot._y, item);*/
	}
	
	public ItemStack getItem(int gSlot) {
		Tuple<Integer, Integer> pageAndSlot = convertToPageAndSlot(gSlot);
		Inventory inv = pages.get(pageAndSlot._x).getInventory();
		return inv.getItem(pageAndSlot._y);
	}
	
	public PageItem getPageItem(int gSlot) {
		for(PageItem item : items) {
			if(item.getGlobalSlot() == gSlot) {
				return item;
			}
		}
		return null;
	}
	
	public PageItem getPageItem(int page, int slot) {
		for(PageItem item : items) {
			if(item.getSlot() == slot && item.getPage() == page)
				return item;
		}
		return null;
	}
	
	public void refresh() {
		List<PageItem> toRemove = new ArrayList<>();
		
		for(PageItem item : items) {
			if(item.shouldDelete()) {
				item.setItem(null);
				setItem(item.getGlobalSlot(), item);
				toRemove.add(item);
			}
		}
		
		items.removeAll(toRemove);
		
		for(int i = 0; i <= totalSlots(); i++) {
			PageItem item = getPageItem(i);
			if(item != null) {
				item.update();
				
				if(i > 0) {
					int previous = i - 1;
					PageItem previousItem = getPageItem(previous);
					
					if(previousItem == null) {
						//we're filling in blank spaces
						Tuple<Integer, Integer> pageAndSlot = convertToPageAndSlot(previous);
						
						item.setNewLocation(pageAndSlot._x, pageAndSlot._y);
						//remove the old and making space for the next one
						setItem(i, new ItemStack(Material.AIR));
						//set the new
						setItem(previous, item);
						
						continue;
					}
					
				}
				
				setItem(i, item);
			}
		}
		
		for(PageItem item : items) {
			if(!item.isInitialized()) {
				

				int globalLastSlot = getLastValidGlobalSlot();
				int next = globalLastSlot + 1;
				
				Bukkit.broadcastMessage("Last global: " + globalLastSlot);
				
				if(!inside(next)) {
					this.pages.add(new Page(this, this.pages.size() + 1));
				}
				
				Tuple<Integer, Integer> pageAndSlot = convertToPageAndSlot(next);
				
				item.setNewLocation(pageAndSlot._x, pageAndSlot._y);
				setItem(next, item);
				
			}
		}
		
	}
	
	public int getLastValidGlobalSlot() {
		if(items.size() == 0) {
			return -1;
		}
		
		PageItem lastValid = null;
		
		for(int i = 0; i < items.size(); i++) {
			if(lastValid == null) {
				lastValid = items.get(i);
			} else {
				PageItem current = items.get(i);
				if(current.isInitialized())
					lastValid = current;
			}
		}
		
		return lastValid.getGlobalSlot();
		
	}
	
	public boolean inside(int gSlot) {
		return gSlot < totalSlots();
	}
	
	public int totalSlots() {
		return this.pages.size() * 45;
	}
	
	public int convertToGlobalSlot(int page, int slot) {
		return page * 45 + slot;
	}
	
	public Tuple<Integer, Integer> convertToPageAndSlot(int globalSlot) {
		
		int pointer = 0;
		
		int page = 0;
		
		for(int i = 0; i < globalSlot; i++) {
			pointer++;
			
			if(pointer > 44) {
				pointer = 0;
				page++;
			}
			
		}
		
		
		
		return new Tuple<>(page, pointer);
	}
	
	private boolean inventoryIsPage(Inventory inv) {
		for(Page page : pages) {
			if(page.getInventory().equals(inv))
				return true;
		}
		return false;
	}
	
	private Page getPage(Inventory inv) {
		for(Page page : pages) {
			if(page.getInventory().equals(inv))
				return page;
		}
		return null;
	}
	
	List<PageItem> getItemsFor(Page page) {
		return items.stream().filter(item -> item.getPage() == page.getIndex()).collect(Collectors.toList());
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(inventoryIsPage(e.getClickedInventory())) {
						
			Page page = getPage(e.getClickedInventory());
			
			int slot = e.getSlot();
			
			PageItem item = page.getPageItem(slot);
			
			Player player = (Player)e.getWhoClicked();
			
			if(slot == 48) {
				//previous page
				if(page.hasPreviousPage())
					player.openInventory(page.getPreviousPage().getInventory());
				
			} else if(slot == 50) {
				//next page
				if(page.hasNextPage())
					player.openInventory(page.getNextPage().getInventory());
				
			} else if(slot == 53) {
				//TODO remove test function
				this.addItem(PageUtil.getTestPageItem());
			}
	
			if(item != null) {
				item.onClick((Player) e.getWhoClicked(), e.getClickedInventory(), item.getItem());
			}
			
			
			e.setCancelled(true);
	
			
			
		}
	}

	public void addItem(PageItem item) {
		this.items.add(item);
	}

	public Page getPage(int page) {
		return pages.stream().filter(oPage -> oPage.getIndex() == page).findFirst().orElse(null);
	}
	
	
	
}
