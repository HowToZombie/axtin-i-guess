package org.axtin.modules.shulkercrates;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShulkerInventory implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -696014948319397112L;
	private LinkedHashMap<Integer, ItemStack> items = new LinkedHashMap<Integer, ItemStack>();
	private LinkedHashMap<Integer, Map<String, Object>> serializableItems = new LinkedHashMap<Integer, Map<String, Object>>();
	
	
	public void readyForSerialization() {
		//TODO IMPORTANT, add this back to last class hasOpened = false;
		if(!items.isEmpty()) {
			for(Entry<Integer, ItemStack> e : items.entrySet()) 
				serializableItems.put(e.getKey(), ItemUtils.serialize(e.getValue()));
			items.clear();

		}
	}
	
	
	public void update(Inventory inventory) {
		items.clear();
		for(int i = 0; i < 27; i++) {
			try {
				inventory.getItem(i).getType();
			} catch(Exception e) {
				continue;
			}
				items.put(i, inventory.getItem(i));
		}
	}
	
	public void setContents(Inventory inventory) {
		if(!items.isEmpty())
			for(Entry<Integer, ItemStack> e : items.entrySet()) {
				inventory.setItem(e.getKey(), e.getValue());

			}
	}
	
	public void deserializeEverything() {
		if(!serializableItems.isEmpty()) {
			for(Entry<Integer, Map<String, Object>> e : serializableItems.entrySet()) {
				try {
					items.put(e.getKey(), ItemUtils.deserialize(e.getValue()));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					e1.printStackTrace();
				}
			}
			serializableItems.clear();
		}

	}
	
	
}
