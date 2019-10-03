package org.axtin.modules.managing;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.modules.managing.MaintenanceCommand;
import org.axtin.user.UserRepository;
import org.axtin.util.FileStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class WhitelistCommand extends AxtinCommand {

    public WhitelistCommand() {
        super("whitelist");
    }

    @Override
    public boolean execute(CommandSender sender, String string, String[] args) {
        FileStorage f = new FileStorage("maintenance", "");
        if (sender instanceof ConsoleCommandSender) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cIncorrect usage: /whitelist add/remove {player}"));
                return true;
            }
            if (args[0].equalsIgnoreCase("add")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().equalsIgnoreCase(args[1])) {
                        MaintenanceCommand.whitelisted.add(p.getUniqueId());
                        ArrayList<String> strings = MaintenanceCommand.whitelisted.stream().map(UUID::toString).collect(Collectors.toCollection(ArrayList::new));
                        f.getConfig().set("Whitelisted", strings);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&aAdded player " + args[1]));
                    }
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().equalsIgnoreCase(args[1])) {
                        MaintenanceCommand.whitelisted.remove(p.getUniqueId());
                        ArrayList<String> strings = MaintenanceCommand.whitelisted.stream().map(UUID::toString).collect(Collectors.toCollection(ArrayList::new));
                        f.getConfig().set("Whitelisted", strings);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cRemoved player " + args[1]));
                    }
                }
            }
        } else if (sender instanceof Player) {
            Player p = (Player) sender;
            if (Container.get(UserRepository.class).getUser(p.getUniqueId()).getData().getStaffRole().getIdentifier() < 21) {
                p.sendMessage(CommandErrors.NO_PERMISSION.toString());
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cIncorrect usage: /whitelist add/remove {player}"));
                return true;
            }
            if (args[0].equalsIgnoreCase("add")) {
                for (Player p1 : Bukkit.getOnlinePlayers()) {
                    if (p1.getName().equalsIgnoreCase(args[1])) {
                        MaintenanceCommand.whitelisted.add(p1.getUniqueId());
                        ArrayList<String> strings = MaintenanceCommand.whitelisted.stream().map(UUID::toString).collect(Collectors.toCollection(ArrayList::new));
                        f.getConfig().set("Whitelisted", strings);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&aAdded player " + args[1]));
                    }
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                for (Player p1 : Bukkit.getOnlinePlayers()) {
                    if (p1.getName().equalsIgnoreCase(args[1])) {
                        MaintenanceCommand.whitelisted.remove(p1.getUniqueId());
                        ArrayList<String> strings = MaintenanceCommand.whitelisted.stream().map(UUID::toString).collect(Collectors.toCollection(ArrayList::new));
                        f.getConfig().set("Whitelisted", strings);
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cRemoved player " + args[1]));
                    }
                }
            }
        }

        f.saveConfig();

        return true;
    }

}
