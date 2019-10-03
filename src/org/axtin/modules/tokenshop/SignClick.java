package org.axtin.modules.tokenshop;

import org.axtin.container.facade.Container;
import org.axtin.user.UserData;
import org.axtin.user.UserRepository;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jo on 3/2/2016.
 */
public class SignClick implements Listener {

    @EventHandler
    public void RightClick(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(e.getPlayer().getItemInHand().hasItemMeta() && e.getPlayer().getItemInHand().getItemMeta().hasDisplayName()){

                if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains(ChatColor.GOLD + "Tier")){
                    e.getPlayer().sendMessage(ChatColor.RED + "Do not right click me with that item!");
                    return;
                }
            }
            if(e.getClickedBlock().getType().equals(Material.WALL_SIGN)){
                Sign s = (Sign) e.getClickedBlock().getState();
                if(ChatColor.stripColor(s.getLine(0)).contains("[TokenShop]")) {
                    String[] split = ChatColor.stripColor(s.getLine(2)).split(" ");
                    Integer cost = Integer.valueOf(split[0]);
                    if (removeToken(e.getPlayer().getUniqueId(), cost)) {
                        ItemStack ticket = new ItemStack(Material.PAPER);
                        ItemMeta meta = ticket.getItemMeta();
                        ArrayList temp = new ArrayList();
                        temp.add(0, ChatColor.BLUE + "Right click to use.");
                        temp.add(1, ChatColor.RED + "You will have 6 minutes inside the shop.");
                        meta.setLore(temp);
                    switch (ChatColor.stripColor(s.getLine(1))) {
                        case "Tier 1":
                            meta.setDisplayName(ChatColor.GOLD + "Tier 1");
                            e.getPlayer().sendMessage(ChatColor.GOLD + "You have purchased a ticket for the"+ChatColor.BLUE+" Tier 1 " +ChatColor.GOLD+"shop! Right click it when ready.");
                            break;
                        case "Tier 2":
                            meta.setDisplayName(ChatColor.GOLD + "Tier 2");
                            e.getPlayer().sendMessage(ChatColor.GOLD + "You have purchased a ticket for the"+ChatColor.BLUE+" Tier 2 " +ChatColor.GOLD+"shop! Right click it when ready.");

                            break;
                        case "Tier 3":
                            meta.setDisplayName(ChatColor.GOLD + "Tier 3");
                            e.getPlayer().sendMessage(ChatColor.GOLD + "You have purchased a ticket for the"+ChatColor.BLUE+" Tier 3 " +ChatColor.GOLD+"shop! Right click it when ready.");

                            break;
                        default:
                            break;
                    }
                        e.getPlayer().sendMessage(ChatColor.GREEN + "You have payed " + cost+ " tokens");
                        ticket.setItemMeta(meta);
                        e.getPlayer().getInventory().addItem(ticket);

                }else{
                        e.getPlayer().sendMessage(ChatColor.DARK_RED + "You do not have enough tokens for that!" + ChatColor.BLUE + " Remember to vote using /vote to get tokens!");
                    }


                }
            }
        }
    }

    private boolean removeToken(UUID uuid, int i){
        UserData data = Container.get(UserRepository.class).getUser(uuid).getData();
        if(data.getTokens() >= i){
            data.setTokens(data.getTokens() - i);
            return true;
        }else{
            return false;
        }
    }
}
