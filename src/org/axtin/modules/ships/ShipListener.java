package org.axtin.modules.ships;

import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.axtin.user.role.PrisonRole;
import org.axtin.util.ActionBarUtil;
import org.axtin.util.FileStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;


import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ShipListener implements Listener {

    public static HashMap<User, BukkitTask> timers = new HashMap<>();

    @EventHandler
    public void sneakEvent(PlayerToggleSneakEvent e) {

        Player p = e.getPlayer();

        for (Destinations d : Destinations.values()) {

            Location tempTP = d.getTP().clone();

            ArrayList<Block> blocks = new ArrayList<>();
            blocks.add(d.getPad().getBlock());
            blocks.add(tempTP.getBlock());
            blocks.add(tempTP.add(0, -1, 0).getBlock());

            if (p.isSneaking() && blocks.contains(p.getLocation().getBlock())) {

                EnterShipEvent event = new EnterShipEvent(p);
                Bukkit.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {

                    User u = Container.get(UserRepository.class).getUser(p.getUniqueId());

                    if (!Ship.ships.containsKey(u)) {
                        new Ship(u);
                    }

                    Ship ship = Ship.ships.get(u);
                    p.teleport(ship.getTeleport());

                    return;

                }

            }

        }

        User u = Container.get(UserRepository.class).getUser(p.getUniqueId());

        if (!Ship.ships.containsKey(u)) {
            new Ship(u);
        }

        Ship ship = Ship.ships.get(u);

        Location tempTP = ship.getTeleport().clone();

        ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(ship.getPad().getBlock());
        blocks.add(tempTP.getBlock());
        blocks.add(tempTP.add(0, -1, 0).getBlock());

        if (blocks.contains(p.getLocation().getBlock()) && p.isSneaking()) {

            ExitShipEvent event = new ExitShipEvent(p);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {

                p.teleport(ship.getLocation().getTP());

            }

        }

    }

    @EventHandler
    public void planetSelector(PlayerInteractEvent e) {
        User u = Container.get(UserRepository.class).getUser(e.getPlayer().getUniqueId());
        if (!Ship.ships.containsKey(u)) {
            new Ship(u);
        }
        Ship ship = Ship.ships.get(u);
        if (e.getClickedBlock() != null) {
            if (ship.getSelector().containsLocation(e.getClickedBlock().getLocation())) {
                openTravelGUI(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent e) {
        if (e.getPlayer().getWorld().getName().equalsIgnoreCase("ShipWorld")) {
            e.setCancelled(true);
        }
    }

    public void setDestination(Player p, Destinations d) {

        User u = Container.get(UserRepository.class).getUser(p.getUniqueId());

        Ship ship = Ship.ships.get(u);

        ship.setDestination(d);
        if (d.getDelay() != -1) {
            if (ship.getLocation().getDegrees() == d.getDegrees()) {
                ship.setTime(Math.max(ship.getLocation().getDelay(), d.getDelay()) - Math.min(ship.getLocation().getDelay(), d.getDelay()));
                startTimer(u, Math.max(ship.getLocation().getDelay(), d.getDelay()) - Math.min(ship.getLocation().getDelay(), d.getDelay()), ship.getLocation().getName(), ship.getDestination().getName());
            } else {
                ship.setTime(d.getDelay());
                startTimer(u, d.getDelay(), ship.getLocation().getName(), ship.getDestination().getName());
            }
        } else {
            Ship.ships.get(u).setTime(ship.getLocation().getDelay());
            startTimer(u, ship.getLocation().getDelay(), ship.getLocation().getName(), ship.getDestination().getName());
        }

    }

    public void startTimer(Player p) {
        User u = Container.get(UserRepository.class).getUser(p.getUniqueId());

        Ship ship = Ship.ships.get(u);

        startTimer(u, ship.getTime(), ship.getLocation().getName(), ship.getDestination().getName());
    }

    public void startTimer(User u, int delay, String start, String end) {

        timers.put(u, Bukkit.getScheduler().runTaskTimer(Container.get(Plugin.class), new Runnable() {

            Double time = 0.0;

            @Override
            public void run() {

                Ship ship = Ship.ships.get(u);

                if (time != delay) {
                    double percent = time / delay;
                    if (percent < 0.1) {
                        ActionBarUtil.sendActionBar(u.getPlayer(), "&3" + start + " &6[&4==========&6] &3" + end + " &9[" + time.intValue() + "/" + delay + "]");
                    } else if (percent < 0.2) {
                        ActionBarUtil.sendActionBar(u.getPlayer(), "&3" + start + " &6[&2=&4=========&6] &3" + end + " &9[" + time.intValue() + "/" + delay + "]");
                    } else if (percent < 0.3) {
                        ActionBarUtil.sendActionBar(u.getPlayer(), "&3" + start + " &6[&2==&4========&6] &3" + end + " &9[" + time.intValue() + "/" + delay + "]");
                    } else if (percent < 0.4) {
                        ActionBarUtil.sendActionBar(u.getPlayer(), "&3" + start + " &6[&2===&4=======&6] &3" + end + " &9[" + time.intValue() + "/" + delay + "]");
                    } else if (percent < 0.5) {
                        ActionBarUtil.sendActionBar(u.getPlayer(), "&3" + start + " &6[&2====&4======&6] &3" + end + " &9[" + time.intValue() + "/" + delay + "]");
                    } else if (percent < 0.6) {
                        ActionBarUtil.sendActionBar(u.getPlayer(), "&3" + start + " &6[&2=====&4=====&6] &3" + end + " &9[" + time.intValue() + "/" + delay + "]");
                    } else if (percent < 0.7) {
                        ActionBarUtil.sendActionBar(u.getPlayer(), "&3" + start + " &6[&2======&4====&6] &3" + end + " &9[" + time.intValue() + "/" + delay + "]");
                    } else if (percent < 0.8) {
                        ActionBarUtil.sendActionBar(u.getPlayer(), "&3" + start + " &6[&2=======&4===&6] &3" + end + " &9[" + time.intValue() + "/" + delay + "]");
                    } else if (percent < 0.9) {
                        ActionBarUtil.sendActionBar(u.getPlayer(), "&3" + start + " &6[&2========&4==&6] &3" + end + " &9[" + time.intValue() + "/" + delay + "]");
                    } else if (percent < 1.0) {
                        ActionBarUtil.sendActionBar(u.getPlayer(), "&3" + start + " &6[&2=========&4=&6] &3" + end + " &9[" + time.intValue() + "/" + delay + "]");
                    }
                    time++;

                } else {
                    ActionBarUtil.sendActionBar(u.getPlayer(), "&3" + start + " &6[&2==========&6] &3" + end + " &9[" + time.intValue() + "/" + delay + "]");
                    ship.setTime(-1);
                    ship.setLocation(Destinations.getDestination(end));
                    u.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aYou have arrived at " + end + "!"));
                    BukkitTask t = timers.get(u);
                    timers.remove(u);
                    t.cancel();
                }

            }
        }, 0L, 20L));
    }

    private ItemStack mercury = getItemWithData(Material.CONCRETE, 1, 0, "&f&lMercury", "&c&lLOCKED");
    private ItemStack venus = getItemWithData(Material.STAINED_CLAY, 1, 1, "&6&lVenus", "&c&lLOCKED");
    private ItemStack earth = getItemWithData(Material.GRASS, 1, 0, "&a&lEarth", "&c&lLOCKED");
    private ItemStack mars = getItemWithData(Material.SAND, 1, 1, "&c&lMars", "&7The red planet. Contains tons of iron,", "&7with a few broken devices.", "&5A Rank");
    private ItemStack jupiter = getItemWithData(Material.STAINED_CLAY, 1, 4, "&e&lJupiter", "&c&lLOCKED");
    private ItemStack saturn = getItemWithData(Material.SAND, 1, 0, "&e&lSaturn", "&c&lLOCKED");
    private ItemStack uranus = getItemWithData(Material.PACKED_ICE, 1, 0, "&b&lUranus", "&c&lLOCKED");
    private ItemStack neptune = getItemWithData(Material.LAPIS_BLOCK, 1, 0, "&1&lNeptune", "&c&lLOCKED");
    private ItemStack pluto = getItemWithData(Material.STONE, 1, 5, "&7&lPluto", "&7A prime example of the realities of", "&7life - cruelty, unfairness, and no recognition.", "&5&kβ Rank");

    private ItemStack moon = getItemWithData(Material.ENDER_STONE, 1, 0, "&7&lThe Moon", "&c&lLOCKED");
    private ItemStack station = getItemWithData(Material.EYE_OF_ENDER, 1, 0, "&5&lAxtin Station", "&7The main hub. Full of fun,", "&7games, and secret government agents.", "&5A Rank");
    private ItemStack pvp = getItemWithData(Material.STONE, 1, 0, "&7&lAsteroid Belt", "&7A prison was located in these parts long ago, but has since ceased contact.", "&c&lWARNING: &cThis is a pvp zone. You will drop items here.", "&5Rank B");


    public void openTravelGUI(Player p) {

        Inventory travel = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&',
                "&6&lPlanet Selector"));

        travel.setItem(9, mercury);
        travel.setItem(10, venus);
        travel.setItem(11, earth);
        travel.setItem(12, mars);
        travel.setItem(13, jupiter);
        travel.setItem(14, saturn);
        travel.setItem(15, uranus);
        travel.setItem(16, neptune);
        travel.setItem(17, pluto);

        travel.setItem(2, moon);
        travel.setItem(20, station);
        travel.setItem(3, pvp);

        p.openInventory(travel);

    }

    public ItemStack getItemWithData(Material m, int amount, int durability, String name, String... lore) {

        ItemStack i = new ItemStack(m, amount);
        i.setDurability((short) durability);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        ArrayList<String> lore1 = new ArrayList<>();
        for (String s : lore) {
            lore1.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        meta.setLore(lore1);
        i.setItemMeta(meta);
        return i;

    }

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().getName() != null) {
                if (ChatColor.stripColor(e.getClickedInventory().getName()).equalsIgnoreCase("planet selector")) {
                    e.setCancelled(true);
                    User u = Container.get(UserRepository.class).getUser(e.getWhoClicked().getUniqueId());
                    if (!Ship.ships.containsKey(u)) {
                        new Ship(u);
                    }
                    if (e.getCurrentItem().equals(mars)) {
                        if (!Ship.ships.get(u).getLocation().equals(Destinations.MARS)) {
                            if (Ship.ships.get(u).getTime() != -1) {
                                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&cYou are already travelling to " + Ship.ships.get(u).getDestination().getName() + "!"));
                            } else {
                                setDestination((Player) e.getWhoClicked(), Destinations.MARS);
                                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&9En route to Mars."));
                            }
                        } else {
                            e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cYou are already at Mars!"));
                        }
                    } else if (e.getCurrentItem().equals(station)) {
                        if (!Ship.ships.get(u).getLocation().equals(Destinations.SPAWN)) {
                            if (Ship.ships.get(u).getTime() != -1) {
                                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&cYou are already travelling to " + Ship.ships.get(u).getDestination().getName() + "!"));
                            } else {
                                setDestination((Player) e.getWhoClicked(), Destinations.SPAWN);
                                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        "&9En route to the Axtin Station."));
                            }
                        } else {
                            e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cYou are already at the Axtin Station!"));
                        }
                    } /*else if (e.getCurrentItem().equals(pluto)) {
                if (!Ship.ships.get(u).getLocation().equals(Destinations.PLUTO)) {
                    if (Ship.ships.get(u).getTime() != -1) {
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cYou are already travelling to " + Ship.ships.get(u).getDestination().getName() + "!"));
                    } else {
                        setDestination((Player) e.getWhoClicked(), Destinations.PLUTO);
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&a{need a good message for picking where you go} Pluto."));
                    }
                } else {
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou are already at Pluto!"));
                }
            }*/ /*else if (e.getCurrentItem().equals(pvp)) {
                if (!Ship.ships.get(u).getLocation().equals(Destinations.PVP)) {
                    if (u.getData().getPrisonRole().getIdentifier() < 2) {

                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cYour prison rank is not permitted to go to this destination!"));
                        return;

                    }
                    if (Ship.ships.get(u).getTime() != -1) {
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cYou are already travelling to " + Ship.ships.get(u).getDestination().getName() + "!"));
                    } else {
                        setDestination((Player) e.getWhoClicked(), Destinations.PVP);
                        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&9En route to the Asteroid Belt."));
                    }
                } else {
                    e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cYou are already at the Asteroid Belt!"));
                }
            }*/
                }
            }
        }
    }

}
