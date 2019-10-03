package org.axtin.modules.managing;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.user.UserRepository;
import org.axtin.util.FileStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class MaintenanceCommand extends AxtinCommand {

    public MaintenanceCommand() {
        super("maintenance", "Shut down the server for non-staff.", "/maintenance on/off/toggle {reason}", "stopjoining", "youcantjoinnow", "goaway", "disable");
    }

    public static boolean maintenance;
    public static String errorMessage;
    public static HashSet<UUID> whitelisted = new HashSet<>();

    @Override
    public boolean execute(CommandSender sender, String string, String[] args) {
        FileStorage f = new FileStorage("maintenance", "");
        if (sender instanceof ConsoleCommandSender) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cIncorrect usage: /maintenance on/off/toggle {reason}"));
                return true;
            }
            if (args[0].equalsIgnoreCase("on")) {
                if (maintenance) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cMaintenance is already active!"));
                } else {
                    maintenance = true;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aTurned on maintenance mode!"));
                    StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        if (i < args.length - 1) {
                            builder.append(args[i]);
                            builder.append(" ");
                        } else {
                            builder.append(args[i]);
                        }

                    }
                    errorMessage = builder.toString();

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (Container.get(UserRepository.class).getUser(p.getUniqueId()).getData().getStaffRole().getIdentifier() < 1) {
                            p.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                                    errorMessage));
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("off")) {
                if (!maintenance) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cMaintenance is already disabled!"));
                } else {
                    maintenance = false;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aTurned off maintenance mode!"));
                }
            } else if (args[0].equalsIgnoreCase("toggle")) {
                if (maintenance) {
                    maintenance = false;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aTurned off maintenance mode!"));
                } else {
                    maintenance = true;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aTurned on maintenance mode!"));
                    StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        if (i < args.length - 1) {
                            builder.append(args[i]);
                            builder.append(" ");
                        } else {
                            builder.append(args[i]);
                        }

                    }
                    errorMessage = builder.toString();

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (Container.get(UserRepository.class).getUser(p.getUniqueId()).getData().getStaffRole().getIdentifier() < 1) {
                            p.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                                    errorMessage));
                        }
                    }
                }
            }
        } else if (sender instanceof Player) {
            Player p = (Player) sender;
            if (Container.get(UserRepository.class).getUser(p.getUniqueId()).getData().getStaffRole().getIdentifier() < 21) {
                p.sendMessage(CommandErrors.NO_PERMISSION.toString());
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cIncorrect usage: /maintenance on/off/toggle {reason}"));
                return true;
            }
            if (args[0].equalsIgnoreCase("on")) {
                if (maintenance) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cMaintenance is already active!"));
                } else {
                    maintenance = true;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aTurned on maintenance mode!"));
                    StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        if (i < args.length - 1) {
                            builder.append(args[i]);
                            builder.append(" ");
                        } else {
                            builder.append(args[i]);
                        }

                    }
                    errorMessage = builder.toString();

                    for (Player p1 : Bukkit.getOnlinePlayers()) {
                        if (Container.get(UserRepository.class).getUser(p1.getUniqueId()).getData().getStaffRole().getIdentifier() < 1) {
                            p1.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                                    errorMessage));
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("off")) {
                if (!maintenance) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cMaintenance is already disabled!"));
                } else {
                    maintenance = false;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aTurned off maintenance mode!"));
                }
            } else if (args[0].equalsIgnoreCase("toggle")) {
                if (maintenance) {
                    maintenance = false;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aTurned off maintenance mode!"));
                } else {
                    maintenance = true;
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&aTurned on maintenance mode!"));
                    StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        if (i < args.length - 1) {
                            builder.append(args[i]);
                            builder.append(" ");
                        } else {
                            builder.append(args[i]);
                        }

                    }
                    errorMessage = builder.toString();

                    for (Player p1 : Bukkit.getOnlinePlayers()) {
                        if (Container.get(UserRepository.class).getUser(p1.getUniqueId()).getData().getStaffRole().getIdentifier() < 1) {
                            p1.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                                    errorMessage));
                        }
                    }
                }
            }
        }

        f.getConfig().set("Maintenance", maintenance);
        f.getConfig().set("ErrorMessage", errorMessage);
        f.saveConfig();

        return true;
    }

    public static void loadMaintenance() {
        FileStorage f = new FileStorage("maintenance", "");
        maintenance = f.getConfig().getBoolean("Maintenance");
        errorMessage = f.getConfig().getString("ErrorMessage");
        ArrayList<String> players = (ArrayList<String>) f.getConfig().getStringList("Whitelisted");
        players.forEach(s -> whitelisted.add(UUID.fromString(s)));
    }
}
