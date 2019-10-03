package org.axtin.util.gui.paging;

import java.util.List;

import org.bukkit.inventory.Inventory;

public class Page {

	private final Inventory inventory;
	private final int index;
	private final PageContainer container;
	
	public Page(PageContainer container, int number) {
		this.inventory = PageUtil.createPage("Page: " + number);
		this.index = number - 1;
		this.container = container;
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public void setItem(int slot, PageItem item) {
		//this.inventory.setItem(slot, item.getItem());
		item.setNewLocation(index, slot);
	}
	
	public PageItem getPageItem(int slot) {
		for(PageItem item : container.items) {
			if(item.getPage() == index) {
				if(item.getSlot() == slot) {
					return item;
				}
			}
		}
		return null;
	}
	
	public int convertToGlobalSlot(int slot) {
		return container.convertToGlobalSlot(index, slot);
	}
	
	public boolean hasPreviousPage() {
		return this.index > 0;
	}
	
	public boolean hasNextPage() {
		return this.index < container.pages.size() - 1;
	}
	
	public Page getNextPage() {
		if(hasNextPage())
			return container.pages.get(index + 1);
		return null;
	}
	
	public Page getPreviousPage() {
		if(hasPreviousPage())
			return container.pages.get(index - 1);
		return null;
	}
	
	protected List<PageItem> getItems() {
		return this.container.getItemsFor(this);
	}
	
}
