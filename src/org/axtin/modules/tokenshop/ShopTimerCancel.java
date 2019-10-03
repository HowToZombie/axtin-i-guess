package org.axtin.modules.tokenshop;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Jo on 3/11/2016.
 */
public class ShopTimerCancel implements Listener {

    @EventHandler
    public void teleport(PlayerCommandPreprocessEvent e){
        if(e.getMessage().equalsIgnoreCase("/spawn")){
            if(TokenShopListener.inshop.contains(e.getPlayer())){
                TokenShopListener.inshop.remove(e.getPlayer());
            }
        }
    }
    @EventHandler
    public void logoff(PlayerQuitEvent e){
        if(TokenShopListener.inshop.contains(e.getPlayer())){
            TokenShopListener.inshop.remove(e.getPlayer());
        }
    }

}
