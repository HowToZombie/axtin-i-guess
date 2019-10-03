package org.axtin.deprecated.modules.customenchants;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.axtin.container.facade.Container;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

/**
 * Created by Zombie on 5/13/2017.
 */
public class Enchantment {
    private String name;
    private int maxLevel;
    private String identification;
    public static ArrayList<Enchantment> enchantments = new ArrayList<>();

    public Enchantment(String name, int maxLevel, String identification) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.identification = identification;
        enchantments.add(this);
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public String getIdentification() {
        return this.identification;
    }

    public boolean detectEnchantment(ItemStack i) {
        ItemMeta m = i.getItemMeta();
        ArrayList<String> lore;
        try {
            lore = (ArrayList<String>) m.getLore();
            for (String s : lore) {
                if (s.contains(this.name)) {
                    return true;
                }
            }
        } catch(NullPointerException e) {
            return false;
        }
        return false;
    }

    public int getLevel(ItemStack i) {
        ItemMeta m = i.getItemMeta();
        ArrayList<String> lore;
        lore = (ArrayList<String>) m.getLore();
        try {
            for (String s : lore) {
                if (s.contains(this.name)) {
                    return Integer.parseInt(s.split("\\s")[2]);
                }
            }
        } catch(NullPointerException e) {
            return 0;
        }
        return 0;
    }

    public ItemStack addEnchantment(ItemStack inHand, int level) {
        ItemMeta handMeta = inHand.getItemMeta();
        ArrayList<String> lore;
        lore = (ArrayList<String>) handMeta.getLore();
        if (maxLevel == 0) {
            try {
                for (String s : lore) {
                    if (s.contains(this.name)) {
                        if (this.name.equalsIgnoreCase("efficiency")) {
                            handMeta.addEnchant(org.bukkit.enchantments.Enchantment.DIG_SPEED, level, true);
                            lore.set(lore.indexOf(s), ChatColor.GRAY + "Efficiency Level " + level);
                            handMeta.setLore(lore);
                            inHand.setItemMeta(handMeta);
                            return inHand;
                        } else {
                            lore.set(lore.indexOf(s), ChatColor.GRAY + this.name + " Level " + level);
                            handMeta.setLore(lore);
                            handMeta.addEnchant(org.bukkit.enchantments.Enchantment.LURE, 1, true);
                            inHand.setItemMeta(handMeta);
                            return inHand;
                        }
                    }
                }
            } catch(NullPointerException e) {
                ArrayList<String> newLore = new ArrayList<>();
                if (this.name.equalsIgnoreCase("efficiency")) {
                    handMeta.addEnchant(org.bukkit.enchantments.Enchantment.DIG_SPEED, level, true);
                    newLore.add(ChatColor.GRAY + "Efficiency Level " + level);
                    handMeta.setLore(newLore);
                    inHand.setItemMeta(handMeta);
                    return inHand;
                } else {
                    newLore.add(ChatColor.GRAY + this.name + " Level " + level);
                    handMeta.setLore(newLore);
                    handMeta.addEnchant(org.bukkit.enchantments.Enchantment.LURE, 1, true);
                    inHand.setItemMeta(handMeta);
                    return inHand;
                }
            }
            if (this.name.equalsIgnoreCase("efficiency")) {
                handMeta.addEnchant(org.bukkit.enchantments.Enchantment.DIG_SPEED, level, true);
                lore.add(ChatColor.GRAY + "Efficiency Level " + level);
                handMeta.setLore(lore);
                inHand.setItemMeta(handMeta);
                return inHand;
            } else {
                lore.add(ChatColor.GRAY + this.name + " Level " + level);
                handMeta.setLore(lore);
                handMeta.addEnchant(org.bukkit.enchantments.Enchantment.LURE, 1, true);
                inHand.setItemMeta(handMeta);
                return inHand;
            }
        } else if (!(level > this.maxLevel)) {
            try {
                for (String s : lore) {
                    if (s.contains(this.name)) {
                        if (this.name.equalsIgnoreCase("efficiency")) {
                            handMeta.addEnchant(org.bukkit.enchantments.Enchantment.DIG_SPEED, level, true);
                            lore.set(lore.indexOf(s), ChatColor.GRAY + "Efficiency Level " + level);
                            handMeta.setLore(lore);
                            inHand.setItemMeta(handMeta);
                            return inHand;
                        } else {
                            lore.set(lore.indexOf(s), ChatColor.GRAY + this.name + " Level " + level);
                            handMeta.setLore(lore);
                            handMeta.addEnchant(org.bukkit.enchantments.Enchantment.LURE, 1, true);
                            inHand.setItemMeta(handMeta);
                            return inHand;
                        }
                    }
                }
            } catch(NullPointerException e) {
                ArrayList<String> newLore = new ArrayList<>();
                if (this.name.equalsIgnoreCase("efficiency")) {
                    handMeta.addEnchant(org.bukkit.enchantments.Enchantment.DIG_SPEED, level, true);
                    newLore.add(ChatColor.GRAY + "Efficiency Level " + level);
                    handMeta.setLore(newLore);
                    inHand.setItemMeta(handMeta);
                    return inHand;
                } else {
                    newLore.add(ChatColor.GRAY + this.name + " Level " + level);
                    handMeta.setLore(newLore);
                    handMeta.addEnchant(org.bukkit.enchantments.Enchantment.LURE, 1, true);
                    inHand.setItemMeta(handMeta);
                    return inHand;
                }
            }
            if (this.name.equalsIgnoreCase("efficiency")) {
                handMeta.addEnchant(org.bukkit.enchantments.Enchantment.DIG_SPEED, level, true);
                lore.add(ChatColor.GRAY + "Efficiency Level " + level);
                handMeta.setLore(lore);
                inHand.setItemMeta(handMeta);
                return inHand;
            } else {
                lore.add(ChatColor.GRAY + this.name + " Level " + level);
                handMeta.setLore(lore);
                handMeta.addEnchant(org.bukkit.enchantments.Enchantment.LURE, 1, true);
                inHand.setItemMeta(handMeta);
                return inHand;
            }
        }
        return inHand;
    }

    public ItemStack removeEnchantment(ItemStack inHand) {
        ItemMeta handMeta = inHand.getItemMeta();
        ArrayList<String> lore;
        lore = (ArrayList<String>) handMeta.getLore();
        try {
            for (String s : lore) {
                if (s.contains(this.name)) {
                    lore.remove(lore.indexOf(s));
                    if (this.name.equalsIgnoreCase("efficiency")) {
                        handMeta.removeEnchant(org.bukkit.enchantments.Enchantment.DIG_SPEED);
                    } else {
                        if (lore.size() == 4) {
                            handMeta.removeEnchant(org.bukkit.enchantments.Enchantment.LURE);
                        }
                    }
                }
            }
        } catch(NullPointerException | CommandException e) {
            ArrayList<String> newLore = new ArrayList<>();
            handMeta.setLore(newLore);
            inHand.setItemMeta(handMeta);
            return inHand;
        }
        handMeta.setLore(lore);
        inHand.setItemMeta(handMeta);
        return inHand;
    }
}
