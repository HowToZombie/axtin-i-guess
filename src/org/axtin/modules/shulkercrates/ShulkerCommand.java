

package org.axtin.modules.shulkercrates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class ShulkerCommand extends AxtinCommand {

	public static final BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };
    public static final BlockFace[] radial = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };

	public ShulkerCommand() {
		super("shulker");
	}

	@Override
	public boolean execute(CommandSender sender, String s, String[] args) {

		
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
				if(!Container.get(ShulkerHandler.class).shulkBoxes.containsValue(player)) {
					int allowedBoxes = 0;
					for(PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
						String perm = permission.getPermission();
						if(perm.contains("crates.")) {
							String[] parts = perm.split("\\.");
							int i = Integer.parseInt(parts[1]);
							if(i > allowedBoxes)
								allowedBoxes = i;
						}
					}
					
						if(allowedBoxes == 0)
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have access to any shulker box."));
						else {
							int i = 1;
							if(args.length > 0) {
								try {
									i = Integer.parseInt(args[0]);
								} catch (NumberFormatException e) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSecond argument must be a number."));
									return true;
								}
							}
							if(i > allowedBoxes) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou only have access to " + allowedBoxes + " Shulker Boxes."));
								return true;
							}
							if(i <= 0) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou only have access to " + allowedBoxes + " Shulker Boxes."));
								return true;
							}
								
							
							Location initLoc = player.getLocation().getBlock().getRelative(yawToFace(player.getLocation().getYaw(), false)).getLocation();
							if(initLoc.getBlock().getType() == Material.AIR) {
								initLoc.getBlock().setType(shulkerTypes().get(new Random().nextInt(shulkerTypes().size())));
								Bukkit.getPluginManager().callEvent(new ShulkerPlaceEvent(initLoc.getBlock(), player, i - 1));
							} else 
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, you can't use that here."));
							
							
						}
					 
					
				} else
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, Only one shulker box can be active at a time."));
//				if(!ShulkerHandler.plugin.sl.shulkBoxes.containsValue(player)) {__--
//				Location initLoc = player.getLocation().getBlock().getRelative(yawToFace(player.getLocation().getYaw(), false)).getLocation();
//				if(initLoc.getBlock().getType() == Material.AIR) {
//					initLoc.getBlock().setType(shulkerTypes().get(new Random().nextInt(shulkerTypes().size())));
//					Bukkit.getPluginManager().callEvent(new ShulkerPlaceEvent(initLoc.getBlock(), player));
//				} else 
//					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, you can't use that here."));
//				} else {
//					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSorry, Only one shulker box can be active at a time."));
//
//				}

				
				
		}
		
		return true;
	}
	
	private List<Material> shulkerTypes() {
		List<Material> list = new ArrayList<>();
		
		for(Material mat : Material.values()) {
			if(mat.toString().contains("SHULKER_BOX"))
				list.add(mat);
		}
		
		return list;
	}
        
	public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections) {
            return radial[Math.round(yaw / 45f) & 0x7];
        } else {
            return axis[Math.round(yaw / 90f) & 0x3];
        }
    }

}
