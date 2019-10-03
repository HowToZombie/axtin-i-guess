package org.axtin.modules.managing;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Created by Joseph on 4/16/2017.
 */
public class HungerListener implements Listener {

    @EventHandler
    public void cancelHunger(FoodLevelChangeEvent e){
        if(e.getFoodLevel() != 20){
            e.setFoodLevel(20);
        }

    }
}
