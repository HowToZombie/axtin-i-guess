package org.axtin.modules.customenchants;

import java.util.HashMap;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerEnchantmentUseData {
	
	public final Player player;
	
	private final HashMap<CustomEnchantmentType, Long> lastTimeUsed = new HashMap<CustomEnchantmentType, Long>();

	private BossBar bar;
	private BukkitTask barScheduler;
	
	public PlayerEnchantmentUseData(Player player){
		this.player = player;
	}
	
	public long getLastTimeUsed(CustomEnchantmentType type){
		return !lastTimeUsed.containsKey(type) ? 0 : lastTimeUsed.get(type);
	}
	
	public void setLastTimeUsed(CustomEnchantmentType type, long time){
		lastTimeUsed.put(type, time);
	}
	
	public double getTimeLeft(CustomEnchantmentType type){
		if(!lastTimeUsed.containsKey(type))
			return 0;
		else
			return (double) (type.cooldownTime-((System.currentTimeMillis()-lastTimeUsed.get(type)) / 1000D));
	}
	
	public void playBossBar(final CustomEnchantmentType type){
		stopBossBar();
		
		if(type.cooldownTime <= 0) return;
		
		bar = Bukkit.createBossBar("Time left before using the enchantment again", BarColor.YELLOW, BarStyle.SOLID);
		bar.setProgress((double) getTimeLeft(type)/type.cooldownTime);
		bar.addPlayer(player);
		
		barScheduler = new BukkitRunnable(){
			public void run(){
				final double timeLeft = getTimeLeft(type);
				
				if(timeLeft <= 0){
					stopBossBar();
					return;
				}
				
				bar.setProgress((double) timeLeft/type.cooldownTime);
			}
		}.runTaskTimer(Container.get(Plugin.class), 0, 2);
	}
	
	public void stopBossBar(){
		if(bar != null){
			bar.removeAll();
			barScheduler.cancel();
			
			bar = null;
			barScheduler = null;
		}
	}
}
