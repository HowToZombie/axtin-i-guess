package org.axtin.modules.economy;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserData;
import org.axtin.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class PayCommand extends AxtinCommand {

	public PayCommand() {
		super("pay");
	}

	@Override
	public boolean execute(CommandSender sender, String Label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;

			if(args.length < 2) {
				sendMessage(player, "&c/pay (player) (amount)");
				return true;
			}
			
			
			
			@SuppressWarnings("deprecation")
			OfflinePlayer recipent = Bukkit.getOfflinePlayer(args[0]);

			if(recipent == null || !recipent.hasPlayedBefore()) {
				sendMessage(player, "&cNo such player");
				return true;
			}
			
			int amount;

			try {
				amount = Integer.parseInt(args[1]);
				if (amount < 1) {
					sendMessage(player, "&cYou aren't allowed to steal money!");
					return false;
				}
			} catch (NumberFormatException e) {
				sendMessage(player, "&c/pay (player) (amount)");
				return true;
			}
			User rUser;
			if(recipent.isOnline()){
				 rUser = Container.get(UserRepository.class).getUser(recipent.getUniqueId());
			}else{
				 rUser = Container.get(UserRepository.class).offsetGet(recipent.getUniqueId());

			}
			UserData rUserData = rUser.getData();
			User pUser = Container.get(UserRepository.class).getUser(player.getUniqueId());
			UserData pUserData = pUser.getData();
			

			if(pUserData.getBalance() < amount) {
				sendMessage(player, "&cInsufficent funds");
				return true;
			}
			pUserData.setBalance(pUserData.getBalance() - amount);

			//Transaction trans = new Transaction(amount, player.getUniqueId(), recipent.getUniqueId());

			sendMessage(player, String.format("&7Sent &a$%d &7to %s", amount, recipent.getName()));
			if(recipent.isOnline()){
				rUserData.setBalance(rUserData.getBalance() + amount);
				sendMessage((Player) recipent, String.format("&7Recieved &a$%d &7from %s", amount, player.getName()));
			}else{
				Container.get(UserRepository.class).offsetUpdate(rUser.getPlayer().getUniqueId(),"balance", rUserData.getBalance() + amount);
			}
		}
		
		
		return true;
	}

	
	
	
	
}
