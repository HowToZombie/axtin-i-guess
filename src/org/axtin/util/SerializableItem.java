package org.axtin.util;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SerializableItem {

	private SerializableItemMeta meta;
	private int amount;
	private short durability;
	private Material material;
	
	public SerializableItem(ItemStack is) {
		this.meta = new SerializableItemMeta(is.getItemMeta());
		this.amount = is.getAmount();
		this.durability = is.getDurability();
		this.material = is.getType();
	}
	
	public SerializableItem(YamlConfiguration config, String path) {
		this.amount = config.getInt(path + ".Amount");
		this.durability = Short.valueOf(String.valueOf(config.getInt(path + ".Durability")));
		this.material = Material.valueOf(config.getString(path + ".Material"));
		this.meta = new SerializableItemMeta(config, path + ".ItemMeta");
	}
	
	public void save(YamlConfiguration config, String path) {
		config.set(path + ".Amount", this.amount);
		config.set(path + ".Durability", this.durability);
		config.set(path + ".Material", this.material.toString());
		this.meta.save(config, path + ".ItemMeta");
	}
	
	@Deprecated
	public Object getValue(String key) {
		for(Entry<String, Object> entry : meta.getValues().entrySet()) {
			if(entry.getKey().equals(key))
				return entry.getValue();
		}
		return null;
	}
	
	public void setValue(String key, Object value) {
		this.meta.getValues().put(key, value);
	}

	public ItemStack toItemStack() {
		ItemStack is = new ItemStack(material, amount, durability);
		ItemMeta im = is.getItemMeta();
		this.meta.toItemMeta(im);
		is.setItemMeta(im);
		is.setDurability(durability);
		return is;
	}
	
}
