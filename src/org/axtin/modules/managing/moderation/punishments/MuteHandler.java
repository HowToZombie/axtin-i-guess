package org.axtin.modules.managing.moderation.punishments;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matthew on 26/03/2017.
 */
public class MuteHandler {

    private static MuteHandler instance = null;

    public static MuteHandler getInstance(){
        if(instance==null){
            instance = new MuteHandler();
        }
        return instance;
    }

    private MuteHandler(){
        this.mutedPlayers = new HashSet<>();
    }

    private Set<String> mutedPlayers;

    public boolean isMuted(Player player){
        String playerName = player.getName();
        return mutedPlayers.contains(playerName);
    }

    public void mute(Player player, CommandSender sender){
        this.mutedPlayers.add(player.getName());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou have been muted!"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou have muted " + player.getName()));
    }

    public void unMute(Player player, CommandSender sender){
        this.mutedPlayers.remove(player.getName());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou have been unmuted!"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou have unmuted " + player.getName()));
    }


}
