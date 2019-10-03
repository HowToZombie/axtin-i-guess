package org.axtin.modules.oxygen;

import org.axtin.container.facade.Container;
import org.axtin.modules.mines.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * Created by zombi on 7/2/2017.
 */
public class OxygenChecker {

    public OxygenChecker() {

        startChecker();

    }

    private void startChecker() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Container.get(Plugin.class), new Runnable() {

            @Override
            public void run() {

                for (Player p : Bukkit.getOnlinePlayers()) {

                    Boolean inLocks = false;

                    for (Cuboid c : Airlock.areas.values()) {

                        Location minusX = new Location(p.getLocation().getWorld(), p.getLocation().getX() - 0.75, p.getLocation().getY(), p.getLocation().getZ());
                        Location minusZ = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ() - 0.75);
                        Location plusX = new Location(p.getLocation().getWorld(), p.getLocation().getX() + 0.75, p.getLocation().getY(), p.getLocation().getZ());
                        Location plusZ = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ() + 0.75);

                        if (c.containsLocation(p.getLocation()) || c.containsLocation(minusX) || c.containsLocation(minusZ) ||
                                c.containsLocation(plusX) || c.containsLocation(plusZ)) {

                            inLocks = true;

                        }

                    }

                    if (p.getLocation().getWorld().getName().equalsIgnoreCase("ShipWorld")) {

                        inLocks = true;

                    }

                    if (inLocks) {
                        break;
                    }

                    if (p.getInventory().getHelmet() != null) {

                        if (p.getInventory().getHelmet().hasItemMeta()) {

                            if (p.getInventory().getHelmet().getItemMeta().hasDisplayName()) {

                                if (p.getInventory().getHelmet().getItemMeta().getDisplayName().equalsIgnoreCase(Helmet.BASIC.getName())) {

                                    int oxygen = Helmet.getOxygen(p.getInventory().getHelmet()) - 1;
                                    if (oxygen >= 0) {
                                        p.getInventory().setHelmet(Helmet.BASIC.getItem(oxygen));
                                    }

                                } else if (p.getInventory().getHelmet().getItemMeta().getDisplayName().equalsIgnoreCase(Helmet.IRON.getName())) {

                                    int oxygen = Helmet.getOxygen(p.getInventory().getHelmet()) - 1;
                                    if (oxygen >= 0) {
                                        p.getInventory().setHelmet(Helmet.IRON.getItem(oxygen));
                                    }

                                }

                            }

                        }

                    }

                }

            }

        }, 0L, 10 * 20L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Container.get(Plugin.class), new Runnable() {

            @Override
            public void run() {

                for (Player p : Bukkit.getOnlinePlayers()) {

                    Boolean inLocks = false;

                    for (Cuboid c : Airlock.areas.values()) {

                        Location minusX = new Location(p.getLocation().getWorld(), p.getLocation().getX() - 0.75, p.getLocation().getY(), p.getLocation().getZ());
                        Location minusZ = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ() - 0.75);
                        Location plusX = new Location(p.getLocation().getWorld(), p.getLocation().getX() + 0.75, p.getLocation().getY(), p.getLocation().getZ());
                        Location plusZ = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ() + 0.75);

                        if (c.containsLocation(p.getLocation()) || c.containsLocation(minusX) || c.containsLocation(minusZ) ||
                                c.containsLocation(plusX) || c.containsLocation(plusZ)) {

                            inLocks = true;

                        }

                    }

                    if (p.getLocation().getWorld().getName().equalsIgnoreCase("ShipWorld")) {

                        inLocks = true;

                    }

                    if (inLocks) {

                        if (p.getInventory().getHelmet() != null) {

                            if (p.getInventory().getHelmet().hasItemMeta()) {

                                if (p.getInventory().getHelmet().getItemMeta().hasDisplayName()) {

                                    if (p.getInventory().getHelmet().getItemMeta().getDisplayName().equalsIgnoreCase(Helmet.BASIC.getName())) {

                                        int oxygen = Helmet.getOxygen(p.getInventory().getHelmet()) + 1;
                                        if (oxygen <= Helmet.BASIC.getmaxOxygen()) {
                                            p.getInventory().setHelmet(Helmet.BASIC.getItem(oxygen));
                                        }

                                    } else if (p.getInventory().getHelmet().getItemMeta().getDisplayName().equalsIgnoreCase(Helmet.IRON.getName())) {

                                        int oxygen = Helmet.getOxygen(p.getInventory().getHelmet()) + 1;
                                        if (oxygen <= Helmet.IRON.getmaxOxygen()) {
                                            p.getInventory().setHelmet(Helmet.IRON.getItem(oxygen));
                                        }

                                    }

                                }

                            }

                        }

                        for (int i = 0; i < p.getInventory().getSize(); i++) {
                            for (Helmet h : Helmet.values()) {
                                if (p.getInventory().getItem(i) != null) {
                                    if (p.getInventory().getItem(i).hasItemMeta()) {
                                        if (p.getInventory().getItem(i).getItemMeta().hasDisplayName()) {
                                            if (p.getInventory().getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase(h.getName())) {
                                                int oxygen = Helmet.getOxygen(p.getInventory().getItem(i)) + 1;
                                                if (oxygen <= h.getmaxOxygen() && !p.getInventory().getHelmet().equals(p.getInventory().getItem(i))) {
                                                    p.getInventory().setItem(i, h.getItem(oxygen));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                }

            }

        }, 0L, 10L);

    }

}
