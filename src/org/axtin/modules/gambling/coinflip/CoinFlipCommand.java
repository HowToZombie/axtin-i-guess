package org.axtin.modules.gambling.coinflip;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.modules.gambling.roulette.RouletteManager;
import org.axtin.user.User;
import org.axtin.user.UserData;
import org.axtin.util.gui.paging.FastPage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinFlipCommand extends AxtinCommand {

	//private PageUtil util = null;
	
	public CoinFlipCommand() {
		super("coinflip");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player player = (Player) sender;
		if(args.length < 1) {
			player.sendMessage("/coinflip help");
		} else {
			CoinFlipManager manager = Container.get(CoinFlipManager.class);

			if(args[0].equalsIgnoreCase("create")) {
//				if(util == null)
//					util = new PageUtil();
//				
//				if(args.length >= 2) {
//					FastPage fastPage = util.getFastPage(Integer.valueOf(args[1]));
//					player.openInventory(fastPage.getInventory());
//				} else {
//					util.open(player);
//				}
				
				if(args.length >= 2) {
					
					int amount;
					
					try {
						amount = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						
						//argument 2 must be a number
						
						return true;
					}
					
					User user = Container.get(RouletteManager.class).getUser(player);
					UserData data = user.getData();
					
					if(data.getBalance() < amount) {
						sendMessage(player, manager, "player.balance.insufficent");
						return true;
					}
					
					manager.createGlobalRequest(player, amount);
					
					
				}
				
			} else if(args[0].equalsIgnoreCase("global")) {
				if(args.length >= 2) {
					int page = Integer.parseInt(args[1]);
					FastPage fast = new FastPage(manager.getGlobalRequestPages().getPage(page));
					player.openInventory(fast.getInventory());
				}
				//manager.getGlobalRequestPages().open(player);
			}
			
			else if(args[0].equalsIgnoreCase("challenge")) {
				if(args.length >= 3) {
					String target = args[1];
					double amount;
					if(!playerExists(target)) {
						sendMessage(player, manager, "player.notexists", target);
						return true;
					} else {
						if(!isOnline(target)) {
							sendMessage(player, manager, "player.notonline", target);
							return true;
						}
					}
					
					Player tPlayer = Bukkit.getPlayer(target);
					
					try {
						amount = Double.parseDouble(args[2]);
					} catch (NumberFormatException e) {
						sendMessage(player, manager, "coinflip.challenge.usage");
						return true;
					}
					
					
					User user = Container.get(RouletteManager.class).getUser(player);
					UserData data = user.getData();
					
					User tUser = Container.get(RouletteManager.class).getUser(tPlayer);
					UserData tData = tUser.getData();
					
					if(data.getBalance() < amount) {
						sendMessage(player, manager, "player.balance.insufficent");
						return true;
					}
					
					if(tData.getBalance() < amount) {
						sendMessage(player, manager, "opponent.balance.insufficent", target);
						return true;
					}
					
					CoinFlipRequest request = manager.getNewRequest(player, tPlayer, amount);
					request.sendRequest();
					
					
					
					
					
				} else {
					sendMessage(player, manager, "coinflip.challenge.usage");
					return true;
				}
			}
			else if(args[0].equalsIgnoreCase("accept")) {
				if(args.length >= 2) {
					
					String target = args[1];
					
					if(!playerExists(target)) {
						sendMessage(player, manager, "player.notexists", target);
						return true;
					} else {
						if(!isOnline(target)) {
							sendMessage(player, manager, "player.notonline", target);
							return true;
						}
					}
					
					Player tPlayer = Bukkit.getPlayer(target);
					
					if(!manager.requestExists(tPlayer, player)) {
						sendMessage(player, manager, "target.not.requested", target);
						return true;
					}
					
					if(!manager.hasValidRequest(tPlayer, player)) {
						sendMessage(player, manager, "target.request.expired", target);
						return true;
					}
					
					CoinFlipRequest request = manager.getRequest(tPlayer, player);
					
					
					if(!request.isValid()) {
						sendMessage(player, manager, "request.expired");
						return true;
					}
					
					request.accept();
					sendMessage(player, manager, "player.accept.request", target);
					sendMessage(tPlayer, manager, "target.accept.request", player.getName());

					
					
				} else {
					sendMessage(player, manager, "coinflip.accept.usage");
					return true;
				}
			}
			
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean isOnline(String name) {
		return Bukkit.getOfflinePlayer(name).isOnline();
	}
	
	public boolean playerExists(String name) {
		for(OfflinePlayer oPlayer : Bukkit.getOfflinePlayers()) {
			if(oPlayer.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
	private void sendMessage(Player player, CoinFlipManager manager, String identifier, Object... args) {
		String str = manager.getMessageManager().getString(identifier, args);
		player.sendMessage(str);
	}

	
	
	
}
