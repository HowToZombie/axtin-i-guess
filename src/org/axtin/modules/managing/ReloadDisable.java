package org.axtin.modules.managing;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by zombi on 7/3/2017.
 */
public class ReloadDisable implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)  {
        if(event.getMessage().equalsIgnoreCase("/reload") || event.getMessage().equalsIgnoreCase("/rl")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou cannot use the reload command!"));
        }
    }

}
