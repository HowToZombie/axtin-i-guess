package org.axtin.modules.asteroid;


import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;

public class ChangeDetect implements Listener {

    public static ArrayList<Integer> falling = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockChange(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof FallingBlock) {

            FallingBlock target = (FallingBlock) e.getEntity();
            if (falling.contains(target.getEntityId())) {
                if(e.getEntity().hasMetadata("explode")) {
                    customEffect(e.getEntity().getLocation());
                    e.getBlock().setType(Material.AIR);
                    e.setCancelled(true);
                    // e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 10);
                }else {
                    e.getBlock().setType(Material.AIR);
                    e.setCancelled(true);
                }

                //TODO Cancel explosion event and add our own custom effect for the "astroid" hitting the ground
                //Removing not working, causing an error
                // falling.remove(target.getEntityId());
                //Bukkit.broadcastMessage(falling.toString());
            }

        }

    }

    private void customEffect(Location l){
        Particle p = Particle.DRAGON_BREATH;
        for(int i = 20; i > 0 ; i-- ){
            l.getWorld().playEffect(l,Effect.ENDERDRAGON_GROWL,100);
            l.getWorld().spawnParticle(p,l,100);
            l.getWorld().playEffect(l, Effect.SMOKE,100);
        }
    }
}
