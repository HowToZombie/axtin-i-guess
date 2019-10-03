package org.axtin.modules.kitsystem;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public final class ItemMeta {

	private String name = "";
	private List<String> lore = null;
	private Map<Enchantment, Integer> enchantments = null;
	boolean unbreakable;
	
	
	public ItemMeta(ItemStack itemstack) {
		if(itemstack.hasItemMeta()) {
			org.bukkit.inventory.meta.ItemMeta im = itemstack.getItemMeta();
			if(im.hasDisplayName())
				name = im.getDisplayName();
			if(im.hasLore())
				lore = im.getLore();
			if(im.hasEnchants())
				enchantments = im.getEnchants();
			unbreakable = im.isUnbreakable();
		}
	}
	
	public ItemMeta(String path, YamlConfiguration config) {
            String name2 = config.getString(path + ".DisplayName");
            if(name2 != null)
                this.name = name2;
            List<String> lore2 = new ArrayList<>();
            for(String str : config.getStringList(path + ".Lore"))
            	lore2.add(ChatColor.translateAlternateColorCodes('&', str));
            if(!lore2.isEmpty())
                this.lore = lore2;
            if(config.getConfigurationSection(path + ".Enchantments") != null) {
                Map<Enchantment, Integer> enchantments2 = SerializableMapToEnchantments(config.getConfigurationSection(path + ".Enchantments").getValues(false));
                if(enchantments2 != null)
                    this.enchantments =  enchantments2;
            }
            
	}
	
	public void save(String path, YamlConfiguration config) {
		if(!name.isEmpty()) 
			config.set(path + ".DisplayName", name);
		if(lore != null)
			config.set(path + ".Lore", lore);
		if(enchantments != null)
			config.createSection(path + ".Enchantments", enchantmentsToSerializableMap(this.enchantments));
		config.set(path + ".Unbreakable", unbreakable);
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
        
        public void set(org.bukkit.inventory.meta.ItemMeta im) {
            if(!name.isEmpty())
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            if(lore != null)
                im.setLore(lore);
            if(enchantments != null)
                enchantments.entrySet().forEach((entry) -> {
                    im.addEnchant(entry.getKey(), entry.getValue(), true);
                });
            im.setUnbreakable(this.unbreakable);
        }
	
}
