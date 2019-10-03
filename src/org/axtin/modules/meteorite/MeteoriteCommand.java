package org.axtin.modules.meteorite;

import java.util.List;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.axtin.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 10/6/2017
 */
public class MeteoriteCommand extends AxtinCommand {
	
	public MeteoriteCommand(){
		super("meteorite");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args){
		if(sender instanceof Player){
			final User user = Container.get(UserRepository.class).getUser(((Player) sender).getUniqueId());
			 
			if(user.getData().getStaffRole().getIdentifier() < 80){
				sender.sendMessage(CommandErrors.NO_PERMISSION.toString());
				return true;
			}
		}
		
		if(args.length == 0){
			sendHelp(sender, label);
			return true;
		}
		
		final String arg = args[0].toLowerCase();
		
		if(arg.equals("listregions")){
			final List<MeteoriteRegion> regions = Container.get(MeteoriteManager.class).getRegions();
			String str = "";
			
			for(int i=0; i<regions.size(); i++){
				final MeteoriteRegion region = regions.get(i);
				
				str += region.getLocMin().getWorld().getName() + " (" + region.getID() + ")";
				
				if(i+1 < regions.size())
					str += ", ";
			}
			
			sender.sendMessage(ChatColor.GOLD + "(" + regions.size() + ") " + ChatColor.YELLOW + str);
		
		}else if(arg.equals("addregion")){
			if(sender instanceof Player){
				final Player player = (Player) sender;
				final WorldEditPlugin plugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
				final Selection sel = plugin.getSelection(player);
			
				if(sel != null && sel instanceof CuboidSelection){
					final MeteoriteManager manager = Container.get(MeteoriteManager.class);
					final MeteoriteRegion region = new MeteoriteRegion(manager.getNextRegionID(), sel.getMinimumPoint(), sel.getMaximumPoint());
					
					manager.getRegions().add(region);
					MeteoriteConfig.save(manager);
					
					sender.sendMessage(ChatColor.GREEN + "Added a new region with the ID " + ChatColor.DARK_GREEN + region.getID());
				}else
					sender.sendMessage(ChatColor.RED + "Missing WorldEdit selection");
			}else
				sender.sendMessage(ChatColor.RED + "This works only for players!");
		}else if(arg.equals("removeregion") && args.length >= 2){
			if(Util.isInteger(args[1])){
				final MeteoriteManager manager = Container.get(MeteoriteManager.class);
				final MeteoriteRegion region = manager.getRegionByID(Integer.valueOf(args[1]));
				
				if(region != null){
					
					region.removeAll();
					manager.getRegions().remove(region);
					MeteoriteConfig.save(manager);
					
					sender.sendMessage(ChatColor.GREEN + "Added region with the ID " + ChatColor.DARK_GREEN + region.getID());
				}else
					sender.sendMessage(ChatColor.DARK_RED + "Unkown region with the ID " + ChatColor.DARK_RED + args[1]);
			}else
				sender.sendMessage(ChatColor.DARK_RED + args[1] + ChatColor.RED + " isn't a number");
		}else if(arg.equals("spawn") && args.length >= 3){
			if(Util.isInteger(args[1])){
				if(Util.isInteger(args[1])){
					final MeteoriteManager manager = Container.get(MeteoriteManager.class);
					final MeteoriteRegion region = manager.getRegionByID(Integer.valueOf(args[1]));
					
					if(region != null){
						for(int i=0; i<Integer.valueOf(args[2]); i++)
							region.spawn();
						
						sender.sendMessage(ChatColor.GREEN + "Spawned " + ChatColor.DARK_GREEN + args[2] + ChatColor.GREEN + " meteorites");
					}else
						sender.sendMessage(ChatColor.DARK_RED + "Unkown region with the ID " + ChatColor.DARK_RED + args[1]);
				}else
					sender.sendMessage(ChatColor.DARK_RED + args[2] + ChatColor.RED + " isn't a number");
			}else
				sender.sendMessage(ChatColor.DARK_RED + args[1] + ChatColor.RED + " isn't a number");
		}else
			sendHelp(sender, label);
		
		return true;
	}
	
	private void sendHelp(CommandSender sender, String label){
		sender.sendMessage(ChatColor.GOLD + "======= " + ChatColor.YELLOW + "/" + label + ChatColor.GOLD + " =======");
		sender.sendMessage("");
		sender.sendMessage(ChatColor.DARK_AQUA + "/" + label + " listregions");
		sender.sendMessage(ChatColor.DARK_AQUA + "/" + label + " addregion");
		sender.sendMessage(ChatColor.DARK_AQUA + "/" + label + " removeregion " + ChatColor.AQUA + "<ID>");
		sender.sendMessage(ChatColor.DARK_AQUA + "/" + label + " spawn " + ChatColor.AQUA + "<ID> <amount>");
	}
}
