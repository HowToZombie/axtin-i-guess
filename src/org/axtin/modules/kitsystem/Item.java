package org.axtin.modules.kitsystem;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class Item {

	private final int amount;
        private final short durability;
        private final Material material;
        private final ItemMeta itemMeta;
	
	public Item(ItemStack itemstack) {
		this.amount = itemstack.getAmount();
                this.durability = itemstack.getDurability();
                this.material = itemstack.getType();
                itemMeta = new ItemMeta(itemstack);
	}
	
	public Item(String path, YamlConfiguration config) {
		amount = config.getInt(path + ".Amount");
                durability = Short.valueOf(String.valueOf(config.getInt(path + ".Durability")));
                material = Material.valueOf(config.getString(path + ".Material"));
                itemMeta = new ItemMeta(path + ".ItemMeta", config);
	}
	
	public void save(String path, YamlConfiguration config) {
		config.set(path + ".Material", material.toString());
                config.set(path + ".Durability", durability);
                config.set(path + ".Amount", amount);
                itemMeta.save(path + ".ItemMeta", config);
	}
        
        public ItemStack toItemStack() {
            ItemStack itemStack = new ItemStack(material, amount, durability);
            org.bukkit.inventory.meta.ItemMeta im = itemStack.getItemMeta();
            this.itemMeta.set(im);
            itemStack.setItemMeta(im);
            return itemStack;
        }
	
	
	
}
