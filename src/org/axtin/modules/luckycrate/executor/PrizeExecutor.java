package org.axtin.modules.luckycrate.executor;

import java.util.Map;
import java.util.Map.Entry;

import org.axtin.modules.luckycrate.Prize;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/9/2017
 */
public abstract class PrizeExecutor {
	
	public static InputPair[] INPUTS = new InputPair[]{
		new InputPair("amount-min", InputType.INTEGER, false),
		new InputPair("amount-max", InputType.INTEGER, false),
		new InputPair("chance", InputType.INTEGER, true)
	};
	
	protected Prize prize;
	protected Map<String, Object> input;
	
	public int input_amount_min = 1, input_amount_max = 1, input_chance = 1;
	
	public PrizeExecutor(Prize prize, Map<String, Object> input){
		this.prize = prize;
		this.input = input;
		
		// default inputs
		for(Entry<String, Object> e:input.entrySet()){
			switch(e.getKey()){
			case "amount-min":
				this.input_amount_min = (int) e.getValue();
				
				if(this.input_amount_min > input_amount_max)
					this.input_amount_max = this.input_amount_min;
				
				break;
				
			case "amount-max":
				this.input_amount_max = (int) e.getValue();
				
				break;
				
			case "chance":
				this.input_chance = (int) e.getValue();
				break;
			}
		}
	}
	
	public abstract void execute(Player player, Location loc);
	
	public abstract void reset();
}