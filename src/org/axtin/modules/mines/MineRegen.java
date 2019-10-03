package org.axtin.modules.mines;

import org.axtin.container.facade.Container;
import org.axtin.modules.warps.WarpCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

/**
 * Created by Jo on 11/11/2015.
 */
public class MineRegen {
    public MineRegen(){

        int taskID = Bukkit.getScheduler().runTaskTimer(Container.get(Plugin.class), new Runnable() {
            @Override
            public void run() {
                for (RankHolder h : RankHolder.data.values()) {
                    if (!h.getRank().equalsIgnoreCase("tutorial3")) {
                        for (Block b : h.getCube().getBlocks()) {
                            if (!b.getType().equals(Material.HARD_CLAY)) {
                                b.setType(Material.HARD_CLAY);
                            }
                        }
                    }
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (RankHolder h : RankHolder.data.values()) {
                        if (!h.getRank().equalsIgnoreCase("tutorial3")) {
                            Location minusX = new Location(player.getLocation().getWorld(), player.getLocation().getX() - 0.75, player.getLocation().getY(), player.getLocation().getZ());
                            Location minusZ = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() - 0.75);
                            Location plusX = new Location(player.getLocation().getWorld(), player.getLocation().getX() + 0.75, player.getLocation().getY(), player.getLocation().getZ());
                            Location plusZ = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() + 0.75);
                            if (h.getCube().containsLocation(player.getLocation()) || h.getCube().containsLocation(minusX) || h.getCube().containsLocation(minusZ) ||
                                    h.getCube().containsLocation(plusX) || h.getCube().containsLocation(plusZ)) {
                                double highest = h.getCube().getUpperY();

                                // while (current < player.getLocation().getWorld().getMaxHeight()) {
                                //     int lastCurrent = current;
                                //     current++;
                                //     Location loc = new Location(player.getLocation().getWorld(), player.getLocation().getX(), lastCurrent, player.getLocation().getZ());
                                //
                                //     if (loc.getBlock() != null && loc.getBlock().getType() != null && !loc.getBlock().getType().equals(Material.AIR)) {
                                //         highest = lastCurrent;
                                //     }
                                // }

                                Location loc2 = new Location(player.getLocation().getWorld(), player.getLocation().getX(), highest, player.getLocation().getZ());
                                loc2.setPitch(player.getLocation().getPitch());
                                loc2.setYaw(player.getLocation().getYaw());

                                loc2.add(0, 3, 0);
                                // TODO: Use LocationUtil once added back
                                player.teleport(loc2, PlayerTeleportEvent.TeleportCause.COMMAND);

                            }
                        }

                    }
                }

                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&f&l&kI&6&l[&5&lMines have been reset!&6&l]&f&l&kI"));

            }
        }, 10 * 20L, 5 * 60 * 20L).getTaskId();



        int taskID2 = Bukkit.getScheduler().runTaskTimer(Container.get(Plugin.class), new Runnable() {
            @Override
            public void run() {
                final RankHolder h = RankHolder.data.get("tutorial3");
                
                if(h == null) return;
                
                for (Block b : h.getCube().getBlocks()) {
                    if (!b.getType().equals(Material.HARD_CLAY)) {
                        b.setType(Material.HARD_CLAY);
                    }
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    Location minusX = new Location(player.getLocation().getWorld(), player.getLocation().getX() - 0.75, player.getLocation().getY(), player.getLocation().getZ());
                    Location minusZ = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() - 0.75);
                    Location plusX = new Location(player.getLocation().getWorld(), player.getLocation().getX() + 0.75, player.getLocation().getY(), player.getLocation().getZ());
                    Location plusZ = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ() + 0.75);
                    if (h.getCube().containsLocation(player.getLocation()) || h.getCube().containsLocation(minusX) || h.getCube().containsLocation(minusZ) ||
                            h.getCube().containsLocation(plusX) || h.getCube().containsLocation(plusZ)) {
                        double highest = h.getCube().getUpperY();

                        // while (current < player.getLocation().getWorld().getMaxHeight()) {
                        //     int lastCurrent = current;
                        //     current++;
                        //     Location loc = new Location(player.getLocation().getWorld(), player.getLocation().getX(), lastCurrent, player.getLocation().getZ());
                        //
                        //     if (loc.getBlock() != null && loc.getBlock().getType() != null && !loc.getBlock().getType().equals(Material.AIR)) {
                        //         highest = lastCurrent;
                        //     }
                        // }

                        Location loc2 = new Location(player.getLocation().getWorld(), player.getLocation().getX(), highest, player.getLocation().getZ());
                        loc2.setPitch(player.getLocation().getPitch());
                        loc2.setYaw(player.getLocation().getYaw());

                        loc2.add(0, 3, 0);
                        // TODO: Use LocationUtil once added back
                        player.teleport(loc2, PlayerTeleportEvent.TeleportCause.COMMAND);

                    }

                }
            }
        }, 10 * 20L, 10 * 20L).getTaskId();

    }




}
