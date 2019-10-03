package org.axtin.modules.spawnwarping;

import org.axtin.container.facade.Container;
import org.axtin.modules.launch.LaunchCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class GUIListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getView().getTopInventory() == e.getClickedInventory()) {
			if(e.getInventory().getName().contains("Warps")) {
				if(e.getCurrentItem() != null) {
					Player player = (Player) e.getWhoClicked();
					WarpHandler wh = Container.get(WarpHandler.class);
					String warpName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
					Location warpLoc = wh.getWarp(warpName);
					wh.toTeleport.put((Player) e.getWhoClicked(), warpLoc);
					e.getWhoClicked().teleport(e.getWhoClicked().getEyeLocation().add(0, 6, 0));
					e.getWhoClicked().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 400, 1));
					FlameEffect effect = new FlameEffect((Player) e.getWhoClicked(), wh.warpTrigger);
					wh.effects.put(player, effect);
					effect.start();
					BukkitRunnable br = new BukkitRunnable() {

						int seconds = 5;
						
						@Override
						public void run() {
							
							e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Launching in &6" + seconds + "&7..."));
							player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1f);

							if(seconds == 0) {
								Launch((Player) e.getWhoClicked(), wh.warpTrigger);
								e.getWhoClicked().getLocation().getWorld().playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 10F, 1F);
								e.getWhoClicked().removePotionEffect(PotionEffectType.LEVITATION);
								this.cancel();
							}
							seconds--;
						}
						
						
					};
					br.runTaskTimer(Container.get(Plugin.class), 0, 20);
				}
				e.setCancelled(true);
			}
		}
	}
	
	public void Launch(Player p, Location secLoc) {
        Vector v = LaunchCommand.calculateVelocity(p.getLocation().toVector(), secLoc.toVector(), 20);
        double horiz = v.getX();
        v.setX(horiz);
        p.setVelocity(v);
    }
	
//	private void moveToward(Player player, Location to, double speed){
//        Location loc = player.getLocation();
//        double x = loc.getX() - to.getX();
//        double y = loc.getY() - to.getY();
//        double z = loc.getZ() - to.getZ();
//        Vector velocity = new Vector(x, y, z).normalize().multiply(-speed);
//        player.setVelocity(velocity);   
//    }
	
	@EventHandler
	public void moveEvent(PlayerMoveEvent e) {
		WarpHandler wh = Container.get(WarpHandler.class);
		if(!wh.isReady())
			return;
		
		if(wh.toTeleport.containsKey(e.getPlayer())) {
			if(e.getPlayer().getLocation().distance(wh.warpTrigger) < 5) {
				e.getPlayer().teleport(wh.toTeleport.get(e.getPlayer()));
				FlameEffect effect = wh.effects.get(e.getPlayer());
				if(effect.started)
					effect.stop();
				wh.effects.remove(e.getPlayer());
			}
		}
		
		if(e.getPlayer().getLocation().distance(wh.floatTriggerLoc) < 1) {
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 400, 1));
			wh.addToClearing(e.getPlayer());
		}
		
//		if(e.getPlayer().getLocation().distance(wh.endFloatPushTriggerLoc) < 1.0) {
//			moveToward(e.getPlayer(), wh.whereToPush, 1);
//		}
		if(wh.potionShouldBeRemoved(e.getPlayer())) {
			if(distance2D(e.getPlayer().getLocation(), wh.endFloatPushTriggerLoc) > 2) {
				e.getPlayer().removePotionEffect(PotionEffectType.LEVITATION);
				wh.removeFromClearing(e.getPlayer());
			}
		}
	
		if(e.getPlayer().getLocation().distance(wh.guiTriggerLoc) < 1.0) {
			new WarpGUI(e.getPlayer());
		}
		
	}
	
	private double distance2D(Location loc1, Location loc2) {
		double x1 = loc1.getX();
		double x2 = loc2.getX();
		double z1 = loc1.getZ();
		double z2 = loc2.getZ();
		double xRes = (x1 - x2) * (x1 - x2);
		double zRes = (z1 - z2) * (z1 - z2);
		return Math.sqrt(xRes + zRes);
	}
	
}
