package org.axtin.modules.mines;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

/**
 * Created by Joseph on 3/14/2017.
 */
public class HitListener implements Listener {

    HashMap<String, String> second = new HashMap<String,String>();


    @EventHandler
    public void HitPlugin(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action a = e.getAction();
        if(a.equals(Action.LEFT_CLICK_BLOCK)) {
            Block b = e.getClickedBlock();
            Location loc = b.getLocation();


            if (SetCosts.temp.containsKey(p.getName()) && (!second.containsKey(p.getName()))) {
                RankConfig config = new RankConfig(SetCosts.temp.get(p.getName()));


                config.getConfig().set("Region.First.X", loc.getX());
                config.getConfig().set("Region.First.Y", loc.getY());
                config.getConfig().set("Region.First.Z", loc.getZ());


                RankConfig.saveConfig(SetCosts.temp.get(p.getName()));
                second.put(p.getName(), SetCosts.temp.get(p.getName()));
                p.sendMessage(ChatColor.GREEN + "Please hit the second block");
                SetCosts.temp.remove(p.getName());
                e.setCancelled(true);
                return;
            }
            if (second.containsKey(p.getName())){
                e.setCancelled(true);

                RankConfig config = new RankConfig(second.get(p.getName()));




                config.getConfig().set("Region.Second.X", loc.getX());
                config.getConfig().set("Region.Second.Y", loc.getY());
                config.getConfig().set("Region.Second.Z", loc.getZ());
                RankConfig.saveConfig(second.get(p.getName()));
                p.sendMessage(ChatColor.GREEN + "Region set");
                second.remove(p.getName());
                new MineLoad().loadRankUps();


                return;

            }
        }
    }
}
