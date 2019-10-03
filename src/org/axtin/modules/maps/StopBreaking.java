package org.axtin.modules.maps;

import org.axtin.container.facade.Container;
import org.axtin.modules.ships.Storage;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.axtin.util.FileStorage;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Created by Joseph on 7/8/2017.
 */
public class StopBreaking implements Listener {

    @EventHandler
    public void stopBreaking(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof ItemFrame && e.getDamager() instanceof Player) {
            if (!e.getDamager().isOp() || !((Player) e.getDamager()).isSneaking()) {
                e.setCancelled(true);
            } else {
                String name = e.getEntity().getCustomName();
                for (Entity en : e.getEntity().getWorld().getEntities()) {
                    if (en instanceof ItemFrame && en.getName().equalsIgnoreCase(name)) {
                        en.remove();
                    }
                }
                FileStorage map = new FileStorage(name,"maps");
                map.deleteConfig();
            }
        }
    }

    @EventHandler
    public void rotateFrame(PlayerInteractEntityEvent e){
        if (e.getRightClicked() instanceof ItemFrame) {
            if (!e.getPlayer().isOp() || !e.getPlayer().isSneaking()) {
                e.setCancelled(true);
                User u = Container.get(UserRepository.class).getUser(e.getPlayer().getUniqueId());
                if (e.getRightClicked().getCustomName() != null) {
                    if (e.getRightClicked().getCustomName().equalsIgnoreCase("cargo")) {
                        Storage.openStorage(u, e.getPlayer(), 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void placeFrame2(HangingPlaceEvent e){
        if(e.getEntity() instanceof ItemFrame){
            if (e.getPlayer().getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL)) {
                if (e.getPlayer().getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 1) {
                    if (MapCommand.tempFrames.containsKey(e.getPlayer())) {
                        e.setCancelled(true);
                        MapCommand.tempFrames.get(e.getPlayer()).generate(e.getEntity().getLocation(), e.getBlockFace(), e.getBlock(), e.getPlayer());
                    }
                }
            }
        }
    }

}
