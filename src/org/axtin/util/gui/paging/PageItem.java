package org.axtin.util.gui.paging;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public abstract class PageItem {

	private ItemStack item;
	private int page;
	private int slot;
	private int globalSlot;
	private boolean delete;
	private boolean initialized;
	private ReadWriteLock lock;
	
	public PageItem(ItemStack item) {
		this.item = item;
		this.page = -1;
		this.slot = -1;
		this.globalSlot = -1;
		this.delete = false;
		this.initialized = false;
		this.lock = new ReentrantReadWriteLock();
	}
	
	public void setNewLocation(int page, int slot) {
		this.page = page;
		this.slot = slot;
		this.globalSlot = convertToGlobalSlot(page, slot);
		this.delete = false;
		this.initialized = true;
	}
	
	public abstract void onClick(Player player, Inventory inv, ItemStack item);
	
	public abstract void update();
	
	public synchronized void remove() {
		lock.writeLock().lock();
		try {
	        this.delete = true;
	    } finally {
	        lock.writeLock().unlock();
	    }
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public int getPage() {
		return this.page;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public boolean isValid() {
		return this.page != -1;
	}
	
	public boolean shouldDelete() {
		lock.readLock().lock();
		try {
			return this.delete;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public int getGlobalSlot() {
		return this.globalSlot;
	}
	
	private int convertToGlobalSlot(int page, int slot) {
		return page * 45 + slot;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}
	
	public ItemMeta getItemMeta() {
		return this.item.getItemMeta();
	}
	
	public void setItemMeta(ItemMeta im) {
		this.item.setItemMeta(im);
	}
	
	@SuppressWarnings("unused")
	private List<String> color(List<String> list) {
		return list.stream().map(str -> ChatColor.translateAlternateColorCodes('&', str)).collect(Collectors.toList());
	}
	
	@SuppressWarnings("unused")
	private String color(String str) {
		return org.bukkit.ChatColor.translateAlternateColorCodes('&', str);
	}
	
}
