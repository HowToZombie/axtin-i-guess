package org.axtin.modules.managing.moderation.punishments;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Matthew on 26/03/2017.
 */
public class ChatEvent implements Listener {

    private final MuteHandler muteHandler;

    public ChatEvent(){
        this.muteHandler = MuteHandler.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEvent(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        if(!muteHandler.isMuted(player)) return;
        event.setCancelled(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou are currently muted and unable to managing!"));
    }
}
