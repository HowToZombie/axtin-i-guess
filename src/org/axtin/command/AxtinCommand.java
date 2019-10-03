package org.axtin.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public abstract class AxtinCommand extends BukkitCommand {
    public AxtinCommand(String name) {
        super(name);
    }

    public AxtinCommand(String name, String description, String usage, List<String> aliases) {
        super(name, description, usage, aliases);
    }
    public AxtinCommand(String name, String description, String usage, String... aliases) {
        super(name, description, usage, Arrays.asList(aliases));
    }
    public void sendMessage(Player p, String m){
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',m));
    }
    public Player search(String name) {
        Player found = null;
        String lower = name.toLowerCase();

        {
            int delta = 2147483647;

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(lower)) {
                    int current = player.getName().length() - lower.length();

                    if (current < delta) {
                        found = player;
                        delta = current;
                    }

                    if (current == 0) {
                        break;
                    }
                }
            }
        }

        return found;
    }

    public boolean getArguments(Object[] args, int number) {
        return args.length >= number + 1;
    }

    public String getStringFromArguments(String[] args, int start) {
        StringBuilder builder = new StringBuilder();

        for (int i = start; i < args.length; i++) {
            if (i != start) {
                builder.append(" ");
            }

            builder.append(args[i]);
        }

        return builder.toString();
    }
}
