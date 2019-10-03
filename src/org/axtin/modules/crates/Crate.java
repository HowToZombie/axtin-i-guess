package org.axtin.modules.crates;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.axtin.container.facade.Container;
import org.axtin.util.RandomCollection;
import org.axtin.util.SerializableItem;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Crate {

	private String identifier;
	private RandomCollection<CrateItem> items;
	private int itemAmount;
	private int permission;
	private ItemStack displayItem;
	private ItemStack keyItem;
	
	public static Crate getCrateFromFile(File file) {
		String extension = FilenameUtils.getExtension(file.getAbsolutePath());
		
		if(extension.equals("yml")) {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			if(config.getConfigurationSection("Crate") != null) {
				return new Crate(config);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public Crate(YamlConfiguration config) {
		this.items = new RandomCollection<>();
		this.itemAmount = config.getInt("Crate.RouletteAmount");
		this.permission = config.getInt("Crate.DonorPermission");
		this.displayItem = new SerializableItem(config, "Crate.DisplayItem").toItemStack();
		this.keyItem = new SerializableItem(config, "Crate.KeyItem").toItemStack();
		init(config.getConfigurationSection("Crate.Items").getValues(false));
	}
	
	public void init(Map<String, Object> map) {
		CrateHandler manager = Container.get(CrateHandler.class);
		for(Entry<String, Object> entry : map.entrySet()) {
			if(manager.itemExists(entry.getKey())) {
				items.add((double) entry.getValue(), manager.getItem(entry.getKey()));
			}
		}
	}
	
	public List<CrateItem> getItems(int amount) {
		List<CrateItem> list = new ArrayList<>();
		for(int i = 0; i < amount ; i++) {
			list.add(items.next());
		}
		return list;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	public int getItemAmount() {
		return this.itemAmount;
	}
	
	public int getDonorRank() {
		return this.permission;
	}
	
	public ItemStack getCrateItem(int amount) {
		ItemStack item = displayItem.clone();
		item.setAmount(amount);
		return item;
	}
	
	public ItemStack getKey(int amount) {
		ItemStack item = keyItem.clone();
		item.setAmount(amount);
		return item;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
