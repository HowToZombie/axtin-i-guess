package org.axtin.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_12_R1.*;
import org.axtin.Axtin;
import org.axtin.container.facade.Container;
import org.axtin.modules.ships.EnterShipEvent;
import org.axtin.modules.ships.ExitShipEvent;
import org.axtin.modules.ships.ShipListener;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.axtin.user.role.PrisonRole;
import org.axtin.user.role.StaffRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by zombi on 6/22/2017.
 */
public class TabList implements Listener {

    static Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

    @EventHandler
    public void setTabPrefix(PlayerJoinEvent e) {

        Player joined = e.getPlayer();

        for (Player p : Bukkit.getOnlinePlayers()) {

            User user = Container.get(UserRepository.class).getUser(p.getUniqueId());
            StaffRole staffRole = user.getData().getStaffRole();
            PrisonRole prisonRole = user.getData().getPrisonRole();

            String tab;
            String tabPrefix;
            String tabName;
            String tabSuffix;

            if (staffRole.equals(StaffRole.NONE)) {
                tab = ChatColor.translateAlternateColorCodes('&',
                        /*add prison role color here*/"&7[" + prisonRole.getName() + "] " /*add player color here*/ + "&7");
                if (tab.length() <= 16) {
                    tabPrefix = tab;
                    tabName = p.getName();
                    tabSuffix = "";
                } else {
                    tabPrefix = tab.substring(0, 15);
                    tabName = tab.substring(16, tab.length() - 1);
                    tabSuffix = p.getDisplayName();
                }
            } else {
                tab = ChatColor.translateAlternateColorCodes('&',
                        staffRole.getColor() + "&l[" + staffRole.getDisplay() + "] " /*add player color here*/ + "&7");
                if (tab.length() <= 16) {
                    tabPrefix = tab;
                    tabName = p.getName();
                    tabSuffix = "";
                } else {
                    tabPrefix = tab.substring(0, 15);
                    tabName = tab.substring(16, tab.length() - 1);
                    tabSuffix = p.getDisplayName();
                }
            }

            if (board.getTeam(p.getName()) == null) {
                board.registerNewTeam(p.getName()).setPrefix(tabPrefix);
                board.getTeam(p.getName()).setSuffix(tabSuffix);
                p.setDisplayName(tabName);
            }
            if (!board.getTeam(p.getName()).hasEntry(p.getName())) {
                board.getTeam(p.getName()).addEntry(p.getName());
                p.setDisplayName(tabName);
            }

            p.setScoreboard(board);

        }

        String header = ChatColor.translateAlternateColorCodes('&',
                "&5Axtin B.1 &6discord.io/axtin");

        IChatBaseComponent headerMSG = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, headerMSG);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}"));
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        ((CraftPlayer) joined).getHandle().playerConnection.sendPacket(packet);

    }

    public String getName(Player p) {
        User user = Container.get(UserRepository.class).getUser(p.getUniqueId());
        StaffRole staffRole = user.getData().getStaffRole();

        String tabPlayer;

        if (staffRole.equals(StaffRole.NONE)) {
            tabPlayer = ChatColor.translateAlternateColorCodes('&',
                    "&7");
        } else {
            tabPlayer = ChatColor.translateAlternateColorCodes('&',
                    staffRole.getColor() + "[" + staffRole.getDisplay() + "] ");
        }

        return tabPlayer + p.getName();

    }

    @EventHandler
    public void enterShip(EnterShipEvent e) {
        System.out.println(e.getPlayer().getName() + " has entered the ship");
        for (Player p : Bukkit.getWorld("ShipWorld").getPlayers()) {
            p.hidePlayer(e.getPlayer());
            e.getPlayer().hidePlayer(p);
        }
    }

    @EventHandler
    public void exitShip(ExitShipEvent e) {
        System.out.println(e.getPlayer().getName() + " has exited the ship");

        if (e.getShip().getTime() >= 0) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou are currently traveling! Wait " + e.getShip().getTime() + " seconds until you reach " + e.getShip().getDestinationName() + "!"));
            return;
        }

        for (Player p : Bukkit.getWorld("ShipWorld").getPlayers()) {
            p.showPlayer(e.getPlayer());
            e.getPlayer().showPlayer(p);
        }
    }

}
