package org.axtin.modules.managing.moderation.punishments;

import org.axtin.command.CommandErrors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinMute extends PunishmentManager {

    private final MuteHandler muteHandler;

    public AxtinMute() {
        super("mute", "Mutes a player", "/mute <player>", "shutupyourmouth","m");
        this.muteHandler = MuteHandler.getInstance();
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(!this.doChecks(commandSender,strings,1,"axtin.mute")) return true;

        Player player = Bukkit.getPlayer(strings[0]);
        if(player==null){
            commandSender.sendMessage(CommandErrors.INVALID_TARGET.toString());
            return true;
        }

        if(muteHandler.isMuted(player)){
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cThat player is already muted!"));
            return true;
        }
        muteHandler.mute(player, commandSender);
        return true;
    }
}
