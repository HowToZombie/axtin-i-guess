package org.axtin.modules.kitsystem;

import java.util.ArrayList;
import java.util.List;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Alan Tavakoli
 */
public class KitCommand extends AxtinCommand {

    public static final BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };
    public static final BlockFace[] radial = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };
    
    public KitCommand() {
        super("kit");
    }
    
    @Override
    public boolean execute(CommandSender sender, String Label, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length != 1) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/kit (name)"));
                return true;
            }
            
            String kitName = args[0];
            if(Container.get(KitHandler.class).kitExists(kitName)) {
                Kit kit = Container.get(KitHandler.class).getKit(kitName);

                if (!kit.canUse(player)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Kit available in &6" + kit.getTimeLeft(player)));
                    return true;
                }

                User user = Container.get(UserRepository.class).getUser(player.getUniqueId());
                if (user.getData().getPrisonRole().getIdentifier() < kit.getRequiredRank()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInsufficent Permissions."));
                    return true;
                }

                if (!kit.hasEnoughSpace(player.getInventory())) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You need more room in your inventory, &6" + kit.getSpaceRequired() + " &7slot(s) required."));
                    return true;
                }

            /*Block block = player.getLocation().getBlock().getRelative(yawToFace(player.getLocation().getYaw(), false)).getLocation().getBlock();
            if(block.getType() != Material.AIR) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't use that here."));
                return true;

            }*/
                kit.give(player);
                //Container.get(KitHandler.class).addShulker(block, player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Kit &6" + kit.getName() + " &7received."));
                kit.setCooldown(player);
                for (String cmd : kit.commands)
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("@p", player.getName()));

                
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7No such kit, do &6/kits"));
                return true;
            }
        }
        
        return true;
    }
    
    public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections) {
            return radial[Math.round(yaw / 45f) & 0x7];
        } else {
            return axis[Math.round(yaw / 90f) & 0x3];
        }
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
    	List<String> list = new ArrayList<>();

		
			
			if(sender instanceof Player) {
				
				Player player = (Player) sender;
                User user = Container.get(UserRepository.class).getUser(player.getUniqueId());
				for(Kit kit : Container.get(KitHandler.class).kits) {
					if(user.getData().getPrisonRole().getIdentifier() >= kit.getRequiredRank())
						list.add(kit.getName());
				}
				
				
			}
		
		
		return list;
	}
    
}
