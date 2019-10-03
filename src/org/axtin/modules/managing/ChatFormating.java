package org.axtin.modules.managing;

import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Joseph on 4/15/2017.
 */
public class ChatFormating implements Listener {

    @EventHandler
    public void prefixChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        User user = Container.get(UserRepository.class).getUser(player.getUniqueId());
        String prisonRank = user.getData().getPrisonRole().getName().toUpperCase();
        String staffRank = user.getData().getStaffRole().getDisplay();
        String staffColor = user.getData().getStaffRole().getColor();
        if (prisonRank.equalsIgnoreCase("starter")) {
            if (staffRank.equalsIgnoreCase("member")) {
                e.setFormat(ChatColor.GRAY + "%s :" + ChatColor.WHITE + " %s");

            } else {
                e.setFormat(ChatColor.translateAlternateColorCodes('&',
                        staffColor + "[" + staffRank + "]" + ChatColor.GRAY + "%s :" + ChatColor.WHITE + " %s"));

            }
        } else {
            if (staffRank.equalsIgnoreCase("member")) {
                e.setFormat(ChatColor.RED + "[" + prisonRank + "]" + ChatColor.GRAY + "%s :" + ChatColor.WHITE + " %s");

            } else {
                e.setFormat(ChatColor.translateAlternateColorCodes('&',
                        staffColor + "[" + staffRank + "]" + ChatColor.RED + "[" + prisonRank + "]" + ChatColor.GRAY + "%s :" + ChatColor.WHITE + " %s"));

            }
        }

        e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));

    }
}
