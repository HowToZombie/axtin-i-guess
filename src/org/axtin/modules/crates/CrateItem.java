package org.axtin.modules.crates;

import org.axtin.util.SerializableItem;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class CrateItem {
	
	private String identifier;
	private ItemType type;
	private SerializableItem display;
	
	public CrateItem(String path, YamlConfiguration config) {
		this.identifier = config.getString(path + ".Identifier");
		this.type = ItemType.valueOf(config.getString(path + ".Type").toUpperCase());
		this.display = new SerializableItem(config, path + ".DisplayItem");
	}
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	public ItemStack getDisplayItem() {
		return this.display.toItemStack();
	}
	
	public ItemType getType() {
		return type;
	}
	
}
