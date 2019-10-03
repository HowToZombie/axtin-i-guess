package org.axtin.modules.gambling.coinflip;

import org.axtin.container.facade.Container;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CoinFlipRequest {

	private Player requester;
	private Player receiver;
	private double amount;
	private boolean valid;
	
	public CoinFlipRequest(Player requester, Player receiver, double amount) {
		this.requester = requester;
		this.receiver = receiver;
		this.amount = amount;
		this.valid = true;
	}
	
	public void sendRequest() {
		String message = Container.get(CoinFlipManager.class).
				getMessageManager().
				getString("request.message", 
						requester.getName(),
						amount,
						180);
		this.receiver.sendMessage(message);
		startTimer();
	}
	
	public void startTimer() {
		BukkitRunnable timer = new BukkitRunnable() {
			
			@Override
			public void run() {
				valid = false;
			}
			
		};
		
		timer.runTaskLaterAsynchronously(Container.get(Plugin.class), 20 * 180);
	}
	
	public boolean isValid() {
		return this.valid;
	}
	
	public Player getRequester() {
		return this.requester;
	}
	
	public Player getReceiver() {
		return this.receiver;
	}
	
	public void accept() {
		Container.get(CoinFlipManager.class).getNewGame(requester, receiver, amount);
		this.valid = false;
	}
	
	
}
