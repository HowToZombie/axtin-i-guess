package org.axtin.modules.customenchants;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.modules.companion.Companion;
import org.axtin.modules.companion.CompanionManager;
import org.axtin.modules.companion.CompanionStyleBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/11/2017
 */
public class CustomEnchantmentCommand extends AxtinCommand {

	public CustomEnchantmentCommand(){
		super("cenchant");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command only works for players!");
			return true;
		}
		final Player player = (Player) sender;
		
		// TEST
		final Companion c = Container.get(CompanionManager.class).createCompanion(
				new CompanionStyleBuilder().setHelmet_SkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJkZjU2NDhmNGU2ZTNiNTJjOWRhZGQ3YThiYmY1NWY0YWJhN2MxM2MyNWUzMWY0YzI3NTFkMjEzZGJkOWE2In19fQ==")
				.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE)).setLeggings(new ItemStack(Material.IRON_LEGGINGS))
				.setBoots(new ItemStack(Material.IRON_BOOTS)).build(),
				player.getLocation());
		c.spawn();
		
		//
		
		if(args.length >= 1){
			final String arg = args[0].toLowerCase();
			final ItemStack is = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
			final CustomEnchantmentManager manager = Container.get(CustomEnchantmentManager.class);
			
			switch(arg){
			case "available":
				
				sender.sendMessage(ChatColor.DARK_AQUA + "Available custom enchantments:");
				for(CustomEnchantmentType type:CustomEnchantmentType.values())
					sender.sendMessage(ChatColor.AQUA + " " + type.name());
				
				break;
			
			
			case "getenchants":
				
				if(is != null){
					final CustomEnchantmentType[] enchants = manager.getEnchantments(is);
					
					sender.sendMessage(ChatColor.DARK_AQUA + "Enchantments on the item:");
					
					if(enchants.length >= 1){
						for(CustomEnchantmentType type:enchants)
							sender.sendMessage(ChatColor.AQUA + " " + type.name());
					}else
						sender.sendMessage(ChatColor.RED + " none");
				}else
					player.sendMessage(ChatColor.RED + "You aren't holding an item in your hand");
				
				break;
				
			case "addenchant":
				
				if(args.length >= 2){
					if(is != null){
						final CustomEnchantmentType type = CustomEnchantmentType.getByName(args[1]);
						
						if(type != null){
							if(manager.addEnchantment(is, type))
								player.sendMessage(ChatColor.GREEN + "Successfully added the enchantment to the item");
							else
								player.sendMessage(ChatColor.RED + "Failed to add the enchantment to the item");
						}else
							player.sendMessage(ChatColor.RED + "Unkown enchantment " + ChatColor.DARK_RED + args[1]);
					}else
						player.sendMessage(ChatColor.RED + "You aren't holding an item in your hand");
				}else
					player.sendMessage(ChatColor.RED + "Need more arguments");
				
				break;
				
			case "removeenchant":
				
				if(args.length >= 2){
					if(is != null){
						final CustomEnchantmentType type = CustomEnchantmentType.getByName(args[1]);
						
						if(type != null){
							player.sendMessage(ChatColor.GREEN + "Removed " + ChatColor.DARK_GREEN + manager.removeEnchantment(is, type) + ChatColor.GREEN + " enchantments");
						}else
							player.sendMessage(ChatColor.RED + "Unkown enchantment " + ChatColor.DARK_RED + args[1]);
					}else
						player.sendMessage(ChatColor.RED + "You aren't holding an item in your hand");
				}else
					player.sendMessage(ChatColor.RED + "Need more arguments");
				
				break;
				
			case "removeenchants":
				
				if(is != null)
					player.sendMessage(ChatColor.GREEN + "Removed " + ChatColor.DARK_GREEN + manager.removeEnchantments(is) + ChatColor.GREEN + " enchantments");
				else
					player.sendMessage(ChatColor.RED + "You aren't holding an item in your hand");
				
				break;
				
			default:
			
				player.sendMessage(ChatColor.RED + "Unkown argument " + ChatColor.DARK_RED + arg);
				
				break;
			}
			
			if(is != null)
				player.getInventory().setItem(player.getInventory().getHeldItemSlot(), is);
		}else{
			sender.sendMessage(ChatColor.GOLD + "/" + label + " available");
			sender.sendMessage(ChatColor.GOLD + "/" + label + " getenchants");
			sender.sendMessage(ChatColor.GOLD + "/" + label + " addenchant " + ChatColor.YELLOW + "<enchantment>");
			sender.sendMessage(ChatColor.GOLD + "/" + label + " removeenchant " + ChatColor.YELLOW + "<enchantment>");
			sender.sendMessage(ChatColor.GOLD + "/" + label + " removeenchants");
		}
		
		return true;
	}
}
