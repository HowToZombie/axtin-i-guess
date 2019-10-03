package org.axtin.modules.spawnwarping;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand extends AxtinCommand {

	public SetCommand() {
		super("setloc");
	}

	@Override
	public boolean execute(CommandSender sender, String Label, String[] args) {

		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.hasPermission("axtin.admin")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInsufficent permissions."));
				return true;
			}
			if(args.length < 1) {
				player.sendMessage("/setloc gui | levitate | endlevitate | launch");
				return true;
			} else {
				WarpHandler wh = Container.get(WarpHandler.class);
				if(args[0].equalsIgnoreCase("gui")) {
					wh.guiTriggerLoc = ((Player) sender).getLocation();
				} else if(args[0].equalsIgnoreCase("levitate")) {
					wh.floatTriggerLoc = ((Player) sender).getLocation();
				} else if(args[0].equalsIgnoreCase("endlevitate")) {
					wh.endFloatPushTriggerLoc = ((Player) sender).getLocation();
				}  else if(args[0].equalsIgnoreCase("launch")) {
					wh.warpTrigger = ((Player) sender).getLocation();
				}
				
			}
		}
		

		return true;
	}
	
	
	
	
}
