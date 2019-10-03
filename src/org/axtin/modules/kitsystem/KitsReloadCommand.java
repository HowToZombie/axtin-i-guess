package org.axtin.modules.kitsystem;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class KitsReloadCommand extends AxtinCommand {

	public KitsReloadCommand() {
		super("kitsreload");
	}
	
	@Override
	public boolean execute(CommandSender cs, String Label, String[] args) {
		
		if(cs instanceof Player) {
			if(cs.hasPermission("kits.reload")) {
				Container.get(KitHandler.class).reload();
				sendMessage(cs, "&aReloaded Kits configuration.");
			} else {
				sendMessage(cs, "&cInsufficent permissions.");
			}
				
		} else {
			Container.get(KitHandler.class).reload();
			sendMessage(cs, "&aReloaded Kits configuration.");
		}
		
		
		return true;
	}
	
	private void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

}
