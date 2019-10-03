package org.axtin.modules.oxygen;

import org.axtin.modules.mines.Cuboid;
import org.axtin.modules.ships.ShipListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joseph on 3/24/2017.
 */
public class HelmetListener implements Listener {

    @EventHandler
    public void playerHelmetCheck(PlayerMoveEvent e){
        Player player = e.getPlayer();
        boolean wasInLocks = false;
        ArrayList<String> inLocks = new ArrayList<>();
        if (!player.getWorld().equals(Bukkit.getWorld("ShipWorld"))) {
            for (Cuboid c : Airlock.areas.values()) {
                if (!inLocks.contains(player.getName())) {

                    Location minusX = new Location(player.getLocation().getWorld(), player.getLocation().getX() - 0.75, player.getLocation().getY(), player.getLocation().getZ());
                    Location minusZ = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() - 0.75);
                    Location plusX = new Location(player.getLocation().getWorld(), player.getLocation().getX() + 0.75, player.getLocation().getY(), player.getLocation().getZ());
                    Location plusZ = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() + 0.75);

                    if (c.containsLocation(player.getLocation()) || c.containsLocation(minusX) || c.containsLocation(minusZ) ||
                            c.containsLocation(plusX) || c.containsLocation(plusZ)) {
                        inLocks.add(player.getName());
                        if (player.hasPotionEffect(PotionEffectType.WITHER)) {
                            player.removePotionEffect(PotionEffectType.WITHER);
                        }
                        break;
                    }
                    if (player.getInventory().getHelmet() == null) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1000000, 1));

                    } else if (!player.getInventory().getHelmet().hasItemMeta()) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1000000, 1));
                    } else if (!player.getInventory().getHelmet().getItemMeta().hasDisplayName()) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1000000, 1));
                    } else {
                        boolean hasHelm = false;
                        for (Helmet h : Helmet.values()) {
                            if (!hasHelm) {
                                if (!player.getInventory().getHelmet().getItemMeta().getDisplayName().equalsIgnoreCase(h.getName())) {
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1000000, 1));
                                } else if (Helmet.getOxygen(player.getInventory().getHelmet()) == 0) {
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1000000, 1));
                                } else {
                                    if (player.hasPotionEffect(PotionEffectType.WITHER)) {
                                        player.removePotionEffect(PotionEffectType.WITHER);
                                    }
                                    hasHelm = true;
                                }
                            }
                        }
                    }
                }
            }

            if (inLocks.contains(e.getPlayer().getName())) {
                wasInLocks = true;
            }

            inLocks.clear();

        } else {
            if (player.hasPotionEffect(PotionEffectType.WITHER)) {
                player.removePotionEffect(PotionEffectType.WITHER);
            }
        }
    }

    @EventHandler
    public void helmetMoving(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if(e.getSlotType() == InventoryType.SlotType.ARMOR){
            if(e.getSlot() == 39){
                if (e.getCursor() != null) {
                    if (e.getCursor().hasItemMeta()) {
                        if (e.getCursor().getItemMeta().hasDisplayName()) {
                            for (Helmet h : Helmet.values()) {
                                if (h.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(e.getCursor().getItemMeta().getDisplayName())) {
                                    e.setCancelled(true);
                                    ItemStack oldHelmet = p.getInventory().getHelmet();
                                    p.getInventory().setHelmet(e.getCursor());
                                    p.setItemOnCursor(oldHelmet);
                                    if (p.hasPotionEffect(PotionEffectType.WITHER)) {
                                        p.removePotionEffect(PotionEffectType.WITHER);
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        if(e.getClick().isShiftClick()) {
            if (p.getInventory().getHelmet() == null){
                if (p.getInventory().getItem(e.getSlot()) != null) {
                    if (p.getInventory().getItem(e.getSlot()).hasItemMeta()) {
                        if (p.getInventory().getItem(e.getSlot()).getItemMeta().hasDisplayName()) {
                            for (Helmet h : Helmet.values()) {
                                if (h.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(p.getInventory().getItem(e.getSlot()).getItemMeta().getDisplayName())) {
                                    e.setCancelled(true);
                                    p.getInventory().setHelmet(e.getCurrentItem());
                                    p.getInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
                                    if (p.hasPotionEffect(PotionEffectType.WITHER)) {
                                        p.removePotionEffect(PotionEffectType.WITHER);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void HelmetEquip(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR))
            for (Helmet h : Helmet.values()) {
                if (e.getItem() != null) {
                    if (e.getItem().hasItemMeta()) {
                        if (h.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(e.getItem().getItemMeta().getDisplayName())) {
                            e.setCancelled(true);
                            if (p.getInventory().getHelmet() == null) {
                                p.getInventory().setHelmet(e.getItem());
                                p.getInventory().setItem(p.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
                                if (p.hasPotionEffect(PotionEffectType.WITHER)) {
                                    p.removePotionEffect(PotionEffectType.WITHER);
                                }
                            }
                        }
                    }
                }
            }
    }
}
