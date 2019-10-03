package org.axtin.modules.hidetoggle;

import java.util.HashMap;
import java.util.Map.Entry;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class VisibilityToggleCommand extends AxtinCommand {

	public static HashMap<Player, Boolean> hide = new HashMap<>();
	
	public VisibilityToggleCommand() {
		super("toggle");
	}

	@Override
	public boolean execute(CommandSender sender, String Label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			doHide(player);
			if(hide.get(player))
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Other players except staff are now hidden."));
			else 
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Other players are now shown"));

			
			
		}
		
		return true;
	}
	
	public static void checkHides() {
		UserRepository repo = Container.get(UserRepository.class);
		for(Entry<Player, Boolean> e : hide.entrySet()) {
			if(e.getValue()) {
				for(User user : repo.getUsers().values()) {
					if(user.getData().getStaffRole().getIdentifier() <= 0)
						e.getKey().hidePlayer(user.getPlayer());
				}
			}
		}
	}
	
	public static void doHide(Player player) {
		UserRepository repo = Container.get(UserRepository.class);
		if(!hide.containsKey(player)) {
			hide.put(player, true);
			for(Player p : Bukkit.getOnlinePlayers()) {
				User user = repo.getUser(p.getUniqueId());
				if(user.getData().getStaffRole().getIdentifier() <= 0)
					player.hidePlayer(p);
			}
		} else if(hide.get(player)) {
			hide.put(player, false);
			for(Player p : Bukkit.getOnlinePlayers())
				player.showPlayer(p);
		} else if(!hide.get(player)) {
			hide.put(player, true);
			for(Player p : Bukkit.getOnlinePlayers()) {
				User user = repo.getUser(p.getUniqueId());
				if(user.getData().getStaffRole().getIdentifier() <= 0)
					player.hidePlayer(p);
			}
		}
	}

}
