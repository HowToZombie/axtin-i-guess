/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.axtin.modules.kitsystem;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 *
 * @author Alan Tavakoli
 */
public class KitsCommand extends AxtinCommand {

    public KitsCommand() {
        super("kits");
    }

    @Override
    public boolean execute(CommandSender cs, String string, String[] args) {
        if(cs instanceof Player) {
            Player player = (Player) cs;
            TextComponent kits = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7Your kits: "));
            User user = Container.get(UserRepository.class).getUser(player.getUniqueId());
            Container.get(KitHandler.class).kits.forEach((kit) -> {
                if (user.getData().getPrisonRole().getIdentifier() >= kit.getRequiredRank()) {
                    String kitStr = kit.canUse(player) ? ("&6" + kit.getName() + "&7, ") : ("&c" + kit.getName() + "&7, ");
                    TextComponent kitComp = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', kitStr));
                    kitComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(kit.canUse(player) ?
                            ChatColor.translateAlternateColorCodes('&', "&6You can use this kit.")
                            : ChatColor.translateAlternateColorCodes('&', "&7Time left: &6" + kit.getTimeLeft(player)))
                            .create()));
                    kits.addExtra(kitComp);
                }
            });
            player.spigot().sendMessage(kits);
        }
        
        return true;
    } 
    
}
