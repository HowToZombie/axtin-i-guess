package org.axtin.modules.luckycrate.executor;

import java.util.Map;
import java.util.Map.Entry;

import org.axtin.modules.luckycrate.Prize;
import org.axtin.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/9/2017
 */
public class SendMessageExecutor extends PrizeExecutor {
	
	private String input_message;
	private boolean input_broadcast = false;
	
	public SendMessageExecutor(Prize prize, Map<String, Object> input){
		super(prize, input);
		
		for(Entry<String, Object> e:input.entrySet()){
			switch(e.getKey()){
			case "message":
				this.input_message = Util.stringToChatColor((String) e.getValue());
				break;
				
			case "broadcast":
				this.input_broadcast = (boolean) e.getValue();
				break;
			}
		}
	}

	@Override
	public void execute(Player player, Location loc){
		if(input_broadcast){
			for(Player p:Bukkit.getOnlinePlayers())
				p.sendMessage(input_message);
		
		}else
			player.sendMessage(input_message);
	}

	@Override
	public void reset(){ }
}
