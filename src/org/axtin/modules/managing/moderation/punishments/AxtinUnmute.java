package org.axtin.modules.managing.moderation.punishments;

import org.axtin.command.CommandErrors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinUnmute extends PunishmentManager{

    private final MuteHandler muteHandler;

    public AxtinUnmute() {
        super("unmute", "Unmutes a player", "/unmute <player>", "speakagainsweetprince");
        this.muteHandler = MuteHandler.getInstance();
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        if(!this.doChecks(commandSender, strings, 1, "axtin.unmute")) return true;

        Player player = Bukkit.getPlayer(strings[0]);
        if(player==null){
            commandSender.sendMessage(CommandErrors.INVALID_TARGET.toString());
            return true;
        }

        if(!muteHandler.isMuted(player)){
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cThat player is not currently muted!"));
            return true;
        }

        muteHandler.unMute(player,commandSender);

        return true;
    }
}
