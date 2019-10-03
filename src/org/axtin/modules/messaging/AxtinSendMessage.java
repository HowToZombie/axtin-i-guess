package org.axtin.modules.messaging;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinSendMessage {

    private ReplyProvider replyProvider;

    private static AxtinSendMessage instance = null;

    public static AxtinSendMessage getInstance(){
        if(instance == null){
            instance = new AxtinSendMessage();
        }
        return instance;
    }

    private AxtinSendMessage(){
        this.replyProvider = ReplyProvider.getInstance();
    }

    public void sendMessage(CommandSender sender, Player target, String message){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7(&6You -> " + target.getName() + "&7) &f" + message));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7(&6" + sender.getName() + " -> You&7) &f" + message));
        replyProvider.addPlayers(sender, target);
        Bukkit.getOnlinePlayers().stream().filter(p -> !p.getName().equalsIgnoreCase(target.getName()) && !p.getName().equalsIgnoreCase(sender.getName()) && SocialSpyCommand.socialspy.containsKey(p)).forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(&6" + sender.getName() + " -> " + target.getName() + "&7) &f" + message)));
    }
}
