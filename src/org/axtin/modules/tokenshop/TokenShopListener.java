package org.axtin.modules.tokenshop;

import org.axtin.container.facade.Container;
import org.axtin.util.ActionBarUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

/**
 * Created by Jo on 3/10/2016.
 */
public class TokenShopListener implements Listener {

    public static ArrayList<Player> inshop = new ArrayList<>();
    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (e.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
                    return;
                }

            }
            if (!e.getPlayer().getItemInHand().hasItemMeta()) {
                return;
            }
            if(!e.getPlayer().getItemInHand().getItemMeta().hasDisplayName()){
                return;
            }
            if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains(ChatColor.GOLD + "Tier")) {

                Player p = e.getPlayer();
                ItemMeta meta = p.getItemInHand().getItemMeta();
                String name = meta.getDisplayName();
                String[] split = name.split(" ");
                World w = p.getWorld();
                ItemStack newItem = new ItemStack(Material.PAPER, p.getItemInHand().getAmount() - 1);
                newItem.setItemMeta(meta);

                switch (Integer.valueOf(split[1])) {
                    case 1:
                        p.setItemInHand(newItem);
                        Location loc = new Location(w, 344.0, 126.0, 1283.0);
                        loc.setPitch(0);
                        loc.setYaw(90);
                        p.teleport(loc);
                        break;
                    case 2:
                        p.setItemInHand(newItem);
                        Location loc2 = new Location(w, 328.0, 126.0, 1283.0);
                        loc2.setPitch(0);
                        loc2.setYaw(-90);
                        p.teleport(loc2);
                        break;
                    case 3:
                        p.setItemInHand(newItem);
                        Location loc3 = new Location(w, 344.0, 118.0, 1283.0);
                        loc3.setPitch(0);
                        loc3.setYaw(90);
                        p.teleport(loc3);
                        break;
                }
                inshop.add(p);
                StartTimer(p);


            }
        }
    }
    public void StartTimer(Player p){
        new BukkitRunnable(){
            int min = 6;
            int sec = 0;
            String prefix = ChatColor.BLUE +""+ ChatColor.BOLD + "[TokenShop] "+ChatColor.RED +""+ChatColor.BOLD ;
            @Override
            public void run() {
                if(!inshop.contains(p)){
                    this.cancel();
                }
                if(sec == 0) {
                if(!(min ==0)){
                    sec = 60;
                    min --;

                }else{
                    Location loc = p.getWorld().getSpawnLocation();
                    loc.setPitch(0);
                    loc.setYaw(-90);
                    p.teleport(loc);


                    this.cancel();
                }
                }else{
                    sec--;
                    if(sec < 10){
                        ActionBarUtil.sendActionBar(p, prefix + min + ":0" + sec);

                    }else {


                        ActionBarUtil.sendActionBar(p, prefix + min + ":" + sec);

                    }}




            }

        }.runTaskTimer(Container.get(Plugin.class), 0L, 20L);
    }


}