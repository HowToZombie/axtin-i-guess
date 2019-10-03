package org.axtin.modules.spawnwarping;

import org.axtin.container.facade.Container;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FlameEffect extends BukkitRunnable {

	protected int interval;
	protected Player target;
	protected Location endLocation;
	protected boolean started;
	
	public FlameEffect(Player target, Location endLocation) {
		this.interval = 2; 
		this.target = target;
		this.endLocation = endLocation;
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if(!target.isOnline())
			this.cancel();
		target.getLocation().getWorld().spigot().playEffect(target.getLocation(), Effect.MOBSPAWNER_FLAMES);
		
	}
	
	public void start() {
		started = true;
		this.runTaskTimer(Container.get(Plugin.class), 0, interval);
	}
	
	public void stop() {
		if(started) {
			started = !started;
			this.cancel();
		}
	}
	
	

}
