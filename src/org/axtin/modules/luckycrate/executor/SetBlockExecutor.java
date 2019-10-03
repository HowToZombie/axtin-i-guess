package org.axtin.modules.luckycrate.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.axtin.data.BlockData;
import org.axtin.modules.luckycrate.Prize;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/9/2017
 */
public class SetBlockExecutor extends PrizeExecutor {
	
	/**
	 * Making it non static can cause issues!
	 */
	private static final List<BlockData> CHANGEDBLOCKS = new ArrayList<>();
	
	private Material input_type;
	private int input_x, input_y, input_z;
	
	public SetBlockExecutor(Prize prize, Map<String, Object> input){
		super(prize, input);
		
		for(Entry<String, Object> e:input.entrySet()){
			switch(e.getKey()){
			case "type":
				this.input_type = (Material) e.getValue();
				break;
				
			case "x":
				this.input_x = (int) e.getValue();
				break;
				
			case "y":
				this.input_y = (int) e.getValue();
				break;
				
			case "z":
				this.input_z = (int) e.getValue();
				break;
			}
		}
	}

	@Override
	public void execute(Player player, Location loc){
		loc = loc.clone().add(input_x, input_y, input_z);
		final BlockData data = new BlockData(loc.getBlock());
		
		data.read();
		data.getBlock().setType(input_type);
		CHANGEDBLOCKS.add(data);
	}

	@Override
	public void reset(){
		if(CHANGEDBLOCKS.size() == 0) return;
		
		for(BlockData data:CHANGEDBLOCKS)
			data.write();
		
		CHANGEDBLOCKS.clear();
	}
}