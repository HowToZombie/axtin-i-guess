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

import java.util.Arrays;

public class BalanceCommand extends AxtinCommand {

	public BalanceCommand() {
		super("bal");
		this.setAliases(Arrays.asList("balance", "money"));
	}

    private UserRepository repository = Container.get(UserRepository.class);


    @Override
	public boolean execute(CommandSender sender, String Label, String[] args) {
        Player player = this.search(sender.getName());

        User from = repository.getUser(player.getUniqueId());

        if(args.length == 0) {
				UserData pUserdata = from.getData();
				player.sendMessage(message("&7Your balance: &6$" + pUserdata.getBalance()));
			} else if(args.length == 3){
            String arg1 = args[0];
            String target = args[1];
            String m = args[2];
            if(arg1.equalsIgnoreCase("set") && from.getData().getStaffRole().getIdentifier() >80){
                OfflinePlayer t = Bukkit.getOfflinePlayer(target);
                User to;
                if(t.isOnline()){
                     to = Container.get(UserRepository.class).getUser(t.getUniqueId());
                }else{
                     to = Container.get(UserRepository.class).offsetGet(t.getUniqueId());
                }
                if(repository.contains(t.getUniqueId())){

                    int amount;
                    try {
                        amount = Integer.parseInt(m);
                    } catch (NumberFormatException e) {
                        player.sendMessage(message("&cInvalid arguments"));

                        return true;
                    }
                    if(t.isOnline()){
                        to.getData().setBalance(amount);
                    }else{
                        Container.get(UserRepository.class).offsetUpdate(t.getUniqueId(),"balance", amount);
                    }
                    player.sendMessage(message("&7Set " + t.getName() + "'s balance to &6$" + amount));

                }else{
                    player.sendMessage(message("&cNo such player"));
                }
            }else{
                player.sendMessage(message("&cInsufficent permissions."));
                return true;
            }
        }else{


                if (from.getData().getStaffRole().getIdentifier() < 3) {
                    player.sendMessage(message("&cInsufficent permissions."));
                    return true;
                }

                @SuppressWarnings("deprecation")
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                if (target == null || !target.hasPlayedBefore()) {
                    player.sendMessage(message("&cNo such player"));
                    return true;
                }
            User to;
                if(target.isOnline()){
                    to = Container.get(UserRepository.class).getUser(target.getUniqueId());
                }else{
                    to = Container.get(UserRepository.class).offsetGet(target.getUniqueId());
                }
                UserData tUserData = to.getData();
            player.sendMessage(message("&7" + target.getName() + "'s balance: &6$" + tUserData.getBalance()));
            }

		return true;
	}
    private String message(String message) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

}
