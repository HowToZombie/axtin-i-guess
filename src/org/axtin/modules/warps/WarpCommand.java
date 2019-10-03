package org.axtin.modules.warps;

import org.apache.commons.lang.StringUtils;
import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Joseph on 3/20/2017.
 */
public class WarpCommand extends AxtinCommand {

    public static ArrayList<String> mining = new ArrayList<>();
    public WarpCommand() {
        super("warp");
    }
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player) sender;
		if (Container.get(UserRepository.class).getUser(player.getUniqueId()).getData().getStaffRole().getIdentifier() < 21) {
			player.sendMessage(CommandErrors.NO_PERMISSION.toString());
			return true;
		}
		User user = Container.get(UserRepository.class).getUser(player.getUniqueId());
		if(args.length < 1) {
			this.sendMessage(player, "&cIncorrect usage. /warp <warp>");
		} else {
			WarpManager manager =  Container.get(WarpManager.class);
			if(args.length == 4) {
				
				if(user.getData().getStaffRole().getIdentifier() > 20 || player.isOp()) {
					
					if(args[0].equalsIgnoreCase("set")) {
						//Syntax = /warp set <name> <rank>
						String name = args[1];

						if(manager.warpExists(name)) {
							this.sendMessage(player, "&cWarp already exists. If you want to edit it, type /warp edit <name> <rank> <type>");
							return true;
						}
						int rank;
						
						try {
							rank = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							this.sendMessage(player, "&cIncorrect usage. /warp set <name> <rank> <type>");
							return true;
						}
						
						if(!WarpType.typeExists(args[3])) {
							this.sendMessage(player, "&cAccepted types: MINE, OTHER");
							return true;
						}
						
						WarpType type = WarpType.getType(args[3]);
						
						manager.addWarp(name, rank, player.getLocation(), type);
						this.sendMessage(player, "&7Created new warp &6" + name);
					} else if(args[0].equalsIgnoreCase("edit")) {
						String name = args[1];
						if(!manager.warpExists(name)) {
							this.sendMessage(player, "&cWarp does not exist. If you want to create one, type /warp set <name> <rank> <type>");
							return true;
						}
						
						int rank;
						try {
							rank = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							this.sendMessage(player, "&cIncorrect usage. /warp set <name> <rank> <type>");
							return true;
						}
						
						if(!WarpType.typeExists(args[3])) {
							this.sendMessage(player, "&cAccepted types: MINE, OTHER");
							return true;
						}
						
						WarpType type = WarpType.getType(args[3]);
						
						manager.editWarp(name, rank, player.getLocation(), type);
						this.sendMessage(player, String.format("&7Warp &6%s &7edited with Rank: &6%d &7Type: &6%s &7. Also set a new location.", name, rank, type.toString()));
						
					} 
				} else {
					this.sendMessage(player, "&cInsufficent permissions.");
				}
			} else if(args.length == 2) {
				if(user.getData().getStaffRole().getIdentifier() > 20 || player.isOp()) {
					
					if(args[0].equalsIgnoreCase("del")) {
						String name = args[1];
						if(manager.warpExists(name)) {
							manager.removeWarp(name);
							this.sendMessage(player, "&7Warp &6" + name + " &7 deleted.");
						} else {
							this.sendMessage(player, "&cThat warp does not exist.");
						}
					}
					
				} else {
					this.sendMessage(player, "&cInsufficent permissions.");
				}

			} else if(args.length == 1) {
				String name = args[0];
				if(manager.warpExists(name)) {
					Warp warp = manager.getWarp(name);
					if(manager.canWarp(player, warp)) {
						this.sendMessage(player, "&7Teleporting...");
						player.teleport(warp.getTarget());
					} else {
						this.sendMessage(player, "&cInsufficent permissions.");
					}
				} else {
					if(name.equalsIgnoreCase("list")) {
						List<Warp> availableWarps = manager.getWarpsFor8(player);
						List<String> warpNames = availableWarps.stream().map(Warp::getName).collect(Collectors.toList());
						String join = StringUtils.join(warpNames, "&7, &6");
						join = "&7Warps: &6" + join;
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', join));
					} else if(name.equalsIgnoreCase("gui")) {
						player.openInventory(manager.getGUIFor(player));
					} else {
						this.sendMessage(player, "&cWarp does not exist.");
					}
				}
			}
		}
		
		
		// TODO Auto-generated method stub
		return true;
	}
	

//    @Override
//    public boolean execute(CommandSender sender, String s, String[] args) {
//        Player player = this.search(sender.getName());
//        User user = Container.get(UserRepository.class).getUser(player.getUniqueId());
//
//
//        if(args.length == 1){
//            String des = args[0].toLowerCase();
//            switch(des){
//                case("list"):
//                    player.sendMessage("Current warps are " + WarpManager.warps.keySet());
//                    //TODO send player a list of warps they can use
//                    break;
//                case("help"):
//                    player.sendMessage("Command usage is /warp <place> <set/del/delete> <perm>");
//                    //TODO send player a help screen for /warp and its usage
//                    break;
//                default:
//                    if(WarpManager.warps.containsKey(des)){
//                        FileStorage fs = new FileStorage(des,"warps");
//                        YamlConfiguration config = fs.getConfig();
//                        if(config.getInt("perm") <= user.getData().getPrisonRole().getIdentifier()){
//
//                            Location to = new Location(Bukkit.getWorld(config.getString("Location.world")),config.getInt("Location.x"),config.getInt("Location.y"),config.getInt("Location.z"),config.getInt("Location.yaw"),config.getInt("Location.pitch"));
//                            player.teleport(to);
//                            mining.add(player.getName());
//
//                        }else{
//                            if (user.getData().getStaffRole().getIdentifier() >20) {
//                                Location to = new Location(Bukkit.getWorld(config.getString("Location.world")),config.getInt("Location.x"),config.getInt("Location.y"),config.getInt("Location.z"),config.getInt("Location.yaw"),config.getInt("Location.pitch"));
//                                player.teleport(to);
//                                mining.add(player.getName());
//                            }else{
//                                player.sendMessage(message("&cYou don't have permission to use this command."));
//                            }
//                        }
//                    }else {
//                        player.sendMessage("That warp does not exist.");
//                    }
//                    break;
//            }
//        }else if(args.length == 3){
//            if(user.getData().getStaffRole().getIdentifier() > 20){
//                switch (args[0].toLowerCase()){
//                    case("set"):
//                        player.sendMessage("The warp " + args[1] + " has been set at your current location.");
//                        manageWarp(args[1], player.getLocation(), true, args[2]);
//                        break;
//                    default:
//                        player.sendMessage("Command usage is /warp <place> <set/del/delete> <perm>");
//                        break;
//
//
//                }
//            }
//        }else if(args.length == 2){
//            switch (args[0].toLowerCase()){
//                case("del"):
//                    player.sendMessage("The warp " + args[1] + " has been deleted");
//                    manageWarp(args[1],null, false, "");
//                    break;
//                case("delete"):
//                    player.sendMessage("The warp " + args[1] + " has been deleted");
//                    manageWarp(args[1],null, false, "");
//                    break;
//                default:
//                    player.sendMessage("Command usage is /warp <place> <set/del/delete> <perm>");
//                    break;
//
//
//            }
//        }else{
//            player.sendMessage("Command usage is /warp <place>");
//
//        }
//
//        return false;
//    }
//    private void manageWarp(String name,Location l, boolean b, String perm){
//        FileStorage fs = new FileStorage(name.toLowerCase(),"warps");
//        YamlConfiguration config = fs.getConfig();
//        if(b){
//            config.set("perm", Integer.valueOf(perm));
//            config.set("Location.world", l.getWorld().getName());
//            config.set("Location.x", l.getX());
//            config.set("Location.y", l.getY());
//            config.set("Location.z", l.getZ());
//            config.set("Location.yaw", l.getYaw());
//            config.set("Location.pitch", l.getPitch());
//            WarpManager.warps.put(name.toLowerCase(),l);
//            fs.saveConfig();
//
//
//
//        }else{
//            WarpManager.warps.remove(name.toLowerCase());
//            FileStorage.deleteConfig(name.toLowerCase(),"warps");
//        }
//
//
//    }
//    private String message(String message) {
//        return ChatColor.translateAlternateColorCodes('&', message);
//    }

}
