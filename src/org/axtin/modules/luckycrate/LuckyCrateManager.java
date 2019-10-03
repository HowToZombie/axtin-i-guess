package org.axtin.modules.luckycrate;

import java.util.ArrayList;
import java.util.List;


import javax.annotation.Nullable;

import org.axtin.modules.luckycrate.executor.PrizeExecutor;
import org.axtin.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/9/2017
 */
public class LuckyCrateManager implements Listener {
	
	public List<Prize> prizes = new ArrayList<Prize>();
	
	public LuckyCrateManager(){
		LuckyCrateConfig.load(this);
	}
	
	public @Nullable Prize spawn(Location loc, Player player){
		if(prizes.size() == 0) return null;
		
		Prize prize = null;
		
		while(prize == null){
			prize = prizes.get(Util.RAND.nextInt(prizes.size()));
			
			if(prize.chance <= 1 || Util.RAND.nextInt(prize.chance) == 0) prize = null;
		}
		
		for(PrizeExecutor exec:prize.executors){
			if(exec.input_chance <= 1 || Util.RAND.nextInt(prize.chance) == 0){
				final int amount = exec.input_amount_min == exec.input_amount_max ? exec.input_amount_min : Util.RAND.nextInt(exec.input_amount_max-exec.input_amount_min);
				
				for(int i=0; i<amount; i++)
					exec.execute(player, loc);
			}
		}
		
		return prize;
	}
	
	public void reset(){
		for(Prize prize:prizes){
			for(PrizeExecutor exec:prize.executors)
				exec.reset();
		}
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event){
		if(event.isCancelled()) return;
		
		if(Util.RAND.nextInt(20) == 0)
			spawn(event.getBlock().getLocation(), event.getPlayer());
	}
}
