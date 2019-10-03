package org.axtin.modules.oxygen;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Joseph on 4/14/2017.
 */
public enum Helmet {
    BASIC(Material.GLASS,"&5Basic Helmet",0,100),
    IRON(Material.IRON_BLOCK, "&7Iron Helmet",1,200);

    private Material item;
    private String name;
    private int identity;
    private int maxOxygen;

     Helmet(Material m, String s, int i, int o){
         this.item = m;
         this.name = ChatColor.translateAlternateColorCodes('&',s);
         this.identity = i;
         this.maxOxygen = o;
    }

    public static Helmet getHelmet(int i){
        for(Helmet h : Helmet.values()){
            if(h.getIdentity() == i){
                return h;
            }
        }
        return Helmet.BASIC;
    }

    public static int getOxygen(ItemStack i) {
        for (Helmet h : Helmet.values()) {
            if (h.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(i.getItemMeta().getDisplayName())) {
                ArrayList<String> lore;
                lore = (ArrayList<String>) i.getItemMeta().getLore();
                String oxygen = ChatColor.stripColor(lore.get(0)).split("\\s")[1].split("/")[0];
                return Integer.parseInt(oxygen);
            }
        }
        return 0;
    }

    public int getIdentity(){
        return identity;
    }

    public ItemStack getItem(int oxygen){
        ItemStack helm = new ItemStack(this.item, 1);
        ItemMeta meta = helm.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        if (oxygen > this.maxOxygen * 2 / 3) {
            lore.add(ChatColor.translateAlternateColorCodes('&',
                    "&7Oxygen: &a" + oxygen + "&6/" + maxOxygen));
        } else if (oxygen > this.maxOxygen / 3) {
            lore.add(ChatColor.translateAlternateColorCodes('&',
                    "&7Oxygen: &e" + oxygen + "&6/" + maxOxygen));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&',
                    "&7Oxygen: &c" + oxygen + "&6/" + maxOxygen));
        }
        meta.setDisplayName(this.name);
        meta.setLore(lore);
        helm.setItemMeta(meta);
        return helm;
    }

    public ItemStack getItem(){
        ItemStack helm = new ItemStack(this.item, 1);
        ItemMeta meta = helm.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',
                "&7Oxygen: &a" + maxOxygen + "&6/" + maxOxygen));
        meta.setDisplayName(this.name);
        meta.setLore(lore);
        helm.setItemMeta(meta);
        return helm;
    }

    public String getName(){
        return name;
    }

    public int getmaxOxygen() {
        return maxOxygen;
    }

}
