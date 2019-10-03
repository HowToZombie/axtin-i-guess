package org.axtin.modules.managing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.axtin.container.facade.Container;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Gravity implements Listener {
	
	public boolean enabled = true;
	public List<Player> playersNotAllowedToJump = new ArrayList<Player>();
	
	private final Plugin plugin;
	private final List<Player> players = new ArrayList<Player>();
	private final HashMap<Player, BukkitTask> jumpBlacklist = new HashMap<Player, BukkitTask>();
	
	public Gravity(){
		this(Container.get(Plugin.class));
	}
	
	public Gravity(Plugin plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event){
		final Player player = event.getPlayer();
		final Location from = event.getFrom(), to = event.getTo();
		
		if(jumpBlacklist.containsKey(player)){
			to.setY(from.getY());
			event.setTo(to);
			
			return;
		}
		
		if(enabled && !playersNotAllowedToJump.contains(player) && !players.contains(player) &&
				!player.isFlying() && to.getY() > from.getY() && distance(from.getY(), to.getY()) >= 0.4 && player.getFallDistance() == 0){
			players.add(player);
			
			update(player, player.isSneaking() ? 2 : 5, new Runnable(){
				public void run(){
					players.remove(player);
					player.removePotionEffect(PotionEffectType.LEVITATION);
				}
			});
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event){
		final Player player = event.getPlayer();
		
		// clear cache
		playersNotAllowedToJump.remove(player);
		players.remove(player);
		jumpBlacklist.remove(player);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onEntityDamageEvent(EntityDamageEvent event){
		if(event.getEntityType() == EntityType.PLAYER){
			final Player player = (Player) event.getEntity();
			
			player.removePotionEffect(PotionEffectType.LEVITATION);
			
			if(jumpBlacklist.containsKey(player))
				jumpBlacklist.get(player).cancel();
			
			jumpBlacklist.put(player, new BukkitRunnable(){
				public void run(){
					jumpBlacklist.remove(player);
				}
			}.runTaskLater(plugin, 5));
		}
	}
	
	private void update(Player player, int state, Runnable end){
		update(player, state, state, end);
	}
	
	private void update(final Player player, final int state, final int start, final Runnable end){
		player.setFallDistance(0); // no damage
		
		if(player.isOnline() && !jumpBlacklist.containsKey(player) && (state == start || !groundIsSolid(player))){
			playEffect(player, new PotionEffect(PotionEffectType.LEVITATION, 4, state > 0 ? state : state+254), new Runnable(){
				public void run(){
					update(player, state >= -10 ? state-1 : state, start, end);
				}
			});
		}else
			end.run();
	}
	
	private void playEffect(Player player, PotionEffect effect, final Runnable end){
		player.removePotionEffect(PotionEffectType.LEVITATION);
		player.addPotionEffect(effect);
		
		new BukkitRunnable(){
			public void run(){
				end.run();
			}
		}.runTaskLater(plugin, effect.getDuration()-2);
	}
	
	private double distance(double d, double e){
		return d > e ? d - e : e - d;
	}
	
	private boolean groundIsSolid(Player player){
		final Location loc = player.getLocation().clone();
		
		return loc.add(0, -0.5, 0).getBlock().getType().isSolid() /*||
			   loc.add(0.4, 0, 0.4).getBlock().getType().isSolid()*/;
	}
}
