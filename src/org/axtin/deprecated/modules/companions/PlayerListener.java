package org.axtin.deprecated.modules.companions;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked() instanceof ArmorStand) {
			ArmorStand as = (ArmorStand) e.getRightClicked();
			if(as.hasMetadata("companion_holder")) {
				e.setCancelled(true);
				Bukkit.getPluginManager().callEvent(new CompanionInteractEvent(e.getPlayer(), Container.get(CompanionHandler.class).getCompanion(as)));
			}
			
			as.setVelocity(new Vector(1, 1, 1));
		} 
	}
	
	@EventHandler
	public void onCompanionInteract(CompanionInteractEvent e) {
		Player player = e.getPlayer();
		Companion companion = e.getCompanion();
		if(player.getUniqueId() == companion.owner) {
			companion.sendMessage(player, "Hello! I'm your personal companion.");
		} else {
			companion.sendMessage(player, "Hello! I'm " + Bukkit.getOfflinePlayer(companion.owner).getName() + "'s companion.");
		}
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		CompanionHandler handler = Container.get(CompanionHandler.class);
		Companion companion = handler.getAbsoluteCompanion(e.getPlayer());
		companion.spawn(e.getPlayer(), e.getPlayer().getName() + "'s companion", CmpnType.BLUE);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		CompanionHandler handler = Container.get(CompanionHandler.class);
		Companion companion = handler.getAbsoluteCompanion(e.getPlayer());
		if(companion.isSpawned())
			companion.hide();
	}
	
}
