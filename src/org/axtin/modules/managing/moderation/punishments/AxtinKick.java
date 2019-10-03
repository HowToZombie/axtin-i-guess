package org.axtin.modules.managing.moderation.punishments;

import org.axtin.command.CommandErrors;
import org.axtin.util.ArrayToMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinKick extends PunishmentManager {

    public AxtinKick() {
        super ("kick", "Kicks a player", "/kick <player> <reason>");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        if(!this.doChecks(commandSender,strings, 2, "axtin.kick")) return true;

        Player player = Bukkit.getPlayer(strings[0]);

        if(player==null){
            commandSender.sendMessage(CommandErrors.INVALID_TARGET.toString());
            return true;
        }

        String kickMessage = "&c" + ArrayToMessage.formatStringsStatic(Arrays.copyOfRange(strings, 1, strings.length));

        player.kickPlayer(ChatColor.translateAlternateColorCodes('&',kickMessage));

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&c" + player.getName() + " has been kicked for " + kickMessage + "by " + commandSender.getName()));

        return true;


    }
}
