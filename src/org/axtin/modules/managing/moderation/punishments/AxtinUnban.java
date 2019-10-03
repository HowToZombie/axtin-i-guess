package org.axtin.modules.managing.moderation.punishments;

import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.util.UUIDFetcher;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinUnban extends PunishmentManager {

    public AxtinUnban() {
        super("unban", "Unbans a player", "/unban <player>", "pardon", "comebackfromthedeadmate");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(!this.doChecks(commandSender, strings, 1, "axtin.unban")) return true;

        Bukkit.getScheduler().runTaskAsynchronously(Container.get(Plugin.class),
                new Runnable() {
                    @Override
                    public void run() {
                        String playerName = "";

                        try {
                            UUID uuid = UUIDFetcher.getUUIDOf(strings[0]);
                            playerName = Bukkit.getOfflinePlayer(uuid).getName();
                        } catch (Exception e) {
                            commandSender.sendMessage(CommandErrors.NON_EXISTANT_TARGET.toString());
                            return;
                        }

                        Bukkit.getBanList(BanList.Type.NAME).pardon(playerName);
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou have unbanned " + playerName));
                    }
                });
        return true;
    }

}
