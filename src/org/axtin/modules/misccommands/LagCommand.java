package org.axtin.modules.misccommands;

import net.minecraft.server.v1_12_R1.MinecraftServer;
import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class LagCommand extends AxtinCommand {
    public LagCommand() {
        super("lag");

        this.setAliases(Arrays.asList("ram", "tps", "mem", "memory"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Player player = this.search(sender.getName());

        if (Container.get(UserRepository.class).getUser(player.getUniqueId()).getData().getStaffRole().getIdentifier() < 21) {
            player.sendMessage(CommandErrors.NO_PERMISSION.toString());
            return true;
        }
        // TODO: Find out why UserRepository#getUser is null
        // Use UserRepository#offsetGet for a fix
        User user = Container.get(UserRepository.class).getUser(player.getUniqueId());

        if (user.getData().getStaffRole().getIdentifier() < 21) {
            player.sendMessage(message("&cYou don't have permission to use this command."));
            return true;
        }

        player.sendMessage(message("&7======================[ &eStatus &7]======================"));
        player.sendMessage(message(String.format("&7Players: " + player(Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()) + "%s &7/ %s", Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers())));
        double tps = Math.min(Math.round(MinecraftServer.getServer().recentTps[0] * 100.0D) / 100.0D, 20.0D);
        player.sendMessage(message("&7TPS: " + tps(tps) + tps));
        int used = Math.round(((Runtime.getRuntime().totalMemory() / 1048576L) - (Runtime.getRuntime().freeMemory() / 1048576L)));
        int total = Math.round(Runtime.getRuntime().maxMemory() / 1048576L);
        player.sendMessage(message("&7Memory usage: " + memory(used, total) + used + " MB &7/ " + total + " MB"));

        return true;
    }

    private String message(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private ChatColor tps(double tps) {
        if (tps >= 18.5D) {
            return ChatColor.GREEN;
        }
        if (tps >= 17.0D) {
            return ChatColor.DARK_GREEN;
        }
        if (tps >= 15.0D) {
            return ChatColor.GOLD;
        }
        if (tps >= 12.0D) {
            return ChatColor.RED;
        }
        if (tps >= 9.0D) {
            return ChatColor.DARK_RED;
        }
        return ChatColor.DARK_RED;
    }

    private ChatColor memory(int usedRam, int totalRam) {
        int freeRam = totalRam - usedRam;
        if (freeRam <= 1024) {
            return ChatColor.GREEN;
        }
        if (freeRam <= 512) {
            return ChatColor.DARK_GREEN;
        }
        if (freeRam <= 256) {
            return ChatColor.GOLD;
        }
        if (freeRam <= 128) {
            return ChatColor.RED;
        }
        if (freeRam <= 64) {
            return ChatColor.DARK_RED;
        }
        return ChatColor.GREEN;
    }

    private ChatColor player(int online, int max) {
        int free = max - online;

        // TODO: Figure out the logic for online

        return ChatColor.GREEN;
    }
}
