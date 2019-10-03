package org.axtin.modules.tokenshop;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * Created by Jo on 3/3/2016.
 */
public class SignPlace implements Listener {

    @EventHandler
    public void PlaceSign(SignChangeEvent e){

            if(e.getLine(0).contains("TokenShop")){
                e.setLine(0, ChatColor.GREEN + "[TokenShop]");
                switch (e.getLine(1)){
                    case "1":
                        e.setLine(1, ChatColor.GOLD + "Tier 1");
                        e.setLine(2, ChatColor.BLUE + "20 Tokens");
                        break;
                    case "2":
                        e.setLine(1, ChatColor.GOLD + "Tier 2");
                        e.setLine(2, ChatColor.BLUE + "40 Tokens");
                        break;
                    case "3":
                        e.setLine(1, ChatColor.GOLD + "Tier 3");
                        e.setLine(2, ChatColor.BLUE + "60 Tokens");
                        break;
                    default:
                        break;

                }
                e.setLine(3, ChatColor.RED + "Right Click!");
            }else{
                return;
            }

    }
}
