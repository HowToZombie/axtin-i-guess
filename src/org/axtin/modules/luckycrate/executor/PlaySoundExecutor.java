package org.axtin.modules.luckycrate.executor;

import java.util.Map;
import java.util.Map.Entry;

import org.axtin.modules.luckycrate.Prize;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/9/2017
 */
public class PlaySoundExecutor extends PrizeExecutor {
	
	private Sound input_type;
	private float input_volume = 1F, input_pitch = 1F;
	private boolean input_only_executor = false;
	
	public PlaySoundExecutor(Prize prize, Map<String, Object> input){
		super(prize, input);
		
		for(Entry<String, Object> e:input.entrySet()){
			switch(e.getKey()){
			case "type":
				this.input_type = (Sound) e.getValue();
				break;
				
			case "volume":
				this.input_volume = (float) e.getValue();
				break;
				
			case "pitch":
				this.input_pitch = (float) e.getValue();
				break;
			}
		}
	}

	@Override
	public void execute(Player player, Location loc){
		if(!input_only_executor)
			loc.getWorld().playSound(loc, input_type, input_volume, input_pitch);
		else
			player.playSound(loc, input_type, input_volume, input_pitch);
	}

	@Override
	public void reset(){ }
}
