package org.axtin.modules.misccommands;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinList extends AxtinCommand {
    public AxtinList() {
        super("list", "Lists the online players", "/list", "who");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        String online = "&6Who is online: &7";
        StringBuilder staff = new StringBuilder();
        StringBuilder donors = new StringBuilder();
        StringBuilder players = new StringBuilder();
        HashSet<Player> staffList = new HashSet<>();
        HashSet<Player> donorList = new HashSet<>();
        HashSet<Player> playerList = new HashSet<>();
        Bukkit.getOnlinePlayers().forEach(p -> {
            User u = Container.get(UserRepository.class).getUser(p.getUniqueId());
            if (u.getData().getStaffRole().getIdentifier() > 0) {
                staffList.add(p);
            } else if (u.getData().getDonatorRole().getIdentifier() > 0) {
                donorList.add(p);
            } else {
                playerList.add(p);
            }
        });

        if (staffList.size() > 0) {
            staff.append("&6Staff: ");
            staffList.forEach(p -> {
                User u = Container.get(UserRepository.class).getUser(p.getUniqueId());
                staff.append(u.getData().getStaffRole().getColor()).append("[").append(u.getData().getStaffRole().getDisplay()).append("] ").append(p.getName()).append("&7, ");
            });
        }

        if (donorList.size() > 0) {
            staff.append("&6Donators: ");
            staffList.forEach(p -> {
                User u = Container.get(UserRepository.class).getUser(p.getUniqueId());
                donors.append(u.getData().getStaffRole().getColor()).append("[").append(u.getData().getStaffRole().getDisplay()).append("] ").append(p.getName()).append("&7, ");
            });
        }

        if (playerList.size() > 0) {
            players.append("&6Players: &7");
            playerList.forEach(p -> {
                User u = Container.get(UserRepository.class).getUser(p.getUniqueId());
                players.append(p.getName()).append(", ");
            });
        }

        //TODO: Adapt this for any future vanish feature or list by rank name
        //Bukkit.getOnlinePlayers().forEach(m -> builder.append(m.getName()).append(", "));

        //There will be a minor problem when this is used from console on an empty server, purely aesthetical bug
        commandSender.sendMessage(message(online));
        if (staffList.size() > 0) {
            String staffMessage = staff.substring(0, staff.length()-2);
            commandSender.sendMessage(message(staffMessage));
        }
        if (donorList.size() > 0) {
            String donorMessage = donors.substring(0, donors.length()-2);
            commandSender.sendMessage(message(donorMessage));
        }
        if (playerList.size() > 0) {
            String playerMessage = players.substring(0, players.length()-2);
            commandSender.sendMessage(message(playerMessage));
        }
        return true;
    }
    private String message(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
