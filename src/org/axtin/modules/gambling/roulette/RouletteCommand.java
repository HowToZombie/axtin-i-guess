package org.axtin.modules.gambling.roulette;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RouletteCommand extends AxtinCommand{

	public RouletteCommand() {
		super("roulette");
	}

	@Override
	public boolean execute(CommandSender sender, String cmd, String[] args) {
		Player player = (Player) sender;
		if(args[0].equalsIgnoreCase("bid")) {
			if(args.length >= 3) {
				
				double amount;
				String color = args[1];
				BidColor enumColor = BidColor.getByString(color);
				
				try {
					amount = Double.parseDouble(args[2]);
				} catch(NumberFormatException e) {
					player.sendMessage("/roulette bid {red|green|black} {amount}");
					return true;
				}
				
				Bid bid = new Bid(player, amount);
				RouletteManager rManager = Container.get(RouletteManager.class);
				boolean canBid = rManager.canBid(player, amount);
				if(canBid)
					rManager.addBid(bid, enumColor); 
				
				if(canBid)
					player.sendMessage("You've put a " + amount + "$ bid on " + enumColor.getName(false) + ".");
				else 
					player.sendMessage("Sorry, you can't bid.");
				rManager.open(player);
			
			
			} else {
				player.sendMessage("/roulette bid {red|green|black} {amount}");
				return true;
			}
	} else if(args[0].equalsIgnoreCase("money")) {
		User user = Container.get(RouletteManager.class).getUser(player);
		user.getData().setBalance(user.getData().getBalance() + 5000.0);
		player.sendMessage("+5000");
	}
		return true;
	}

}
