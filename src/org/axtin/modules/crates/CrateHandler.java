package org.axtin.modules.crates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class CrateHandler {

	private List<Crate> crates;
	private List<CrateItem> items;
	
	public CrateHandler() {
		crates = new ArrayList<>();
		items = new ArrayList<>();
	}
	
	public boolean itemExists(String identifier) {
		return items.stream().anyMatch(item -> item.getIdentifier().equals(identifier));
	}

	public CrateItem getItem(String key) {
		return items.stream().filter(item -> item.getIdentifier().equals(key)).findAny().get();
	}
	
	public CrateItem getItem(ItemStack item) {
		return items.stream().filter(local -> local.getDisplayItem().isSimilar(item)).findAny().get();
	}
	
}
