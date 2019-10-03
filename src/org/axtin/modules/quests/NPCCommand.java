package org.axtin.modules.quests;

import java.util.ArrayList;
import java.util.List;

import org.axtin.command.AxtinCommand;
import org.axtin.util.npc.NPCBuilder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class NPCCommand extends AxtinCommand {

	public static List<NPCBuilder> builders = new ArrayList<>();
	
	public NPCCommand() {
		super("npc");
	}

	@Override
	public boolean execute(CommandSender cs, String Label, String[] args) {
		
		if(cs instanceof Player) {
			Player player = (Player) cs;
			
			if(args.length < 1) {
				
			} else {
				if(args[0].equalsIgnoreCase("create")) {
					if(!player.hasPermission("npc.admin")) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInsufficent permissions."));
						return true;
					}
					if(args.length < 3) {
						
						return true;
					}
					NPCBuilder builder = new NPCBuilder(player);
					Location initloc = player.getLocation();
					builder.x(initloc.getX());
					builder.y(initloc.getY());
					builder.z(initloc.getZ());
					builder.yaw(initloc.getYaw());
					builder.pitch(initloc.getPitch());
					builder.name(args[1]);
					builder.skin(args[2]);
					builder.create();
					builders.add(builder);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Created &6" + args[1]));
					
					
				} else if(args[0].equalsIgnoreCase("remove")) {
					if(!player.hasPermission("npc.admin")) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInsufficent permissions."));
						return true;
					}
					if(args.length < 2) {
						
						return true;
					}
					for(NPCBuilder builder : builders) {
						if(builder.getName().equalsIgnoreCase(args[1]))
							builder.destroy();
					}
					
				}
				
				
				
			}
			
			
			
		}
		
		return true;
	}
	
	

	
	
}
