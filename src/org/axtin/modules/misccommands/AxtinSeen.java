package org.axtin.modules.misccommands;


import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.util.FormatDate;
import org.axtin.util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinSeen extends AxtinCommand {
    public AxtinSeen() {
        super("seen");
    }

    private String usage = "&c/seen <player>";

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(strings.length==0){
            commandSender.sendMessage(message(this.usage));
            return true;
        }

        String targetPlayerName = strings[0];

        //TODO: Improve this, looks ugly as hell
        Bukkit.getScheduler().runTaskAsynchronously(Container.get(Plugin.class), new Runnable() {
            @Override
            public void run() {
                try {
                    UUID uuid = UUIDFetcher.getUUIDOf(targetPlayerName);
                    if(uuid==null) throw new Exception(); //I know this will be caught and the error message is the same

                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    String playerName = player.getName();
                    long lastSeen = System.currentTimeMillis() - player.getLastPlayed();

                    String message = FormatDate.formatDate(lastSeen);
                    commandSender.sendMessage(message("&6" + playerName + "&7 was last seen: &6" + message + " &7ago"));

                } catch (Exception e) {
                    commandSender.sendMessage(CommandErrors.INVALID_TARGET.toString());
                    //e.printStackTrace();
                }

            }
        });
        return true;

    }

    private String message(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
