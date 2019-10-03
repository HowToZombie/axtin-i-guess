package org.axtin.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

public class SerializableItemMeta {

	private String name = null;
	private Map<Enchantment, Integer> enchantments = null;
	private List<String> lore;
	private Map<String, Object> otherValues = new HashMap<>();
	private boolean unbreakable;
	
	public SerializableItemMeta(ItemMeta im) {
		if(im.hasDisplayName())
			name = im.getDisplayName();
		if(!im.getEnchants().isEmpty())
			enchantments = im.getEnchants();
		if(im.hasLore())
			this.lore = im.getLore();
		this.unbreakable = im.isUnbreakable();
	}
	
	public void save(YamlConfiguration config, String path) {
		if(name != null)
			config.set(path + ".Name", this.name);
		if(enchantments != null)
			config.createSection(path + ".Enchantments", enchantmentsToSerializableMap(this.enchantments));
		if(this.lore != null)
			config.set(path + ".Lore", this.lore);
		//We check if it's not the default value, we don't need to save it if it's the default value.
		if(unbreakable)
			config.set(path + ".Unbreakable", unbreakable);
		//Now to custom user defined values
		for(Entry<String, Object> entry : otherValues.entrySet()) {
			if(entry.getValue() instanceof Map<?, ?>) 
				config.createSection(path + ".Values." + entry.getKey(), (Map<?, ?>) entry.getValue());
			else
				config.set(path + ".Values." + entry.getKey(), entry.getValue());
		}
	}
	
	public SerializableItemMeta(YamlConfiguration config, String path) {
		if(config.isString(path + ".Name"))
			this.name = config.getString(path + ".Name");
		if(config.isConfigurationSection(path + ".Enchantments"))
			this.enchantments = SerializableMapToEnchantments(config.getConfigurationSection(path + ".Enchantments").getValues(false));
		if(config.isList(path + ".Lore"))
			this.lore = config.getStringList(path + ".Lore");
		//If it's there we know it's true, so no need to get the value
		if(config.isBoolean(path + ".Unbreakable"))
			this.unbreakable = true;
		if(config.isConfigurationSection(path + ".Values")) {
			
			for(String key : config.getConfigurationSection(path + ".Values").getKeys(false)) {
				String keyPath = path + ".Values." + path;
				this.otherValues.put(key, config.get(keyPath));
			}
			
		}
		
	}
	
	
	public Map<String, Object> enchantmentsToSerializableMap(Map<Enchantment,Integer> map) {
        Map<String, Object> newMap = new HashMap<>();
        map.entrySet().forEach((entry) -> {
            newMap.put(entry.getKey().getName(), entry.getValue());
        });
       return newMap;
}
    
    public Map<Enchantment, Integer> SerializableMapToEnchantments(Map<String, Object> map) {
        Map<Enchantment, Integer> newMap = new HashMap<>();
        map.entrySet().forEach((entry) -> {
            newMap.put(Enchantment.getByName(entry.getKey()), (Integer) entry.getValue());
        });
        return newMap;
    }
	
    public Map<String, Object> getValues() {
    	return this.otherValues;
    }

	public void toItemMeta(ItemMeta im) {
		if(this.name != null)
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.name));
		if(this.lore != null)
			im.setLore(translateColorCodes(this.lore));
		if(this.enchantments != null) {
			if(!this.enchantments.isEmpty()) {
				this.enchantments.entrySet().forEach((entry) -> {
					im.addEnchant(entry.getKey(), entry.getValue(), true);
				});
			}
		}
		if(unbreakable)
			im.setUnbreakable(unbreakable);	
	}
	
	private List<String> translateColorCodes(List<String> listToTranslate) {
		List<String> list = new ArrayList<>();
		listToTranslate.forEach((str) -> {
			list.add(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', str));
		});
		return list;
	}
	
}
