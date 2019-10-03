package org.axtin.modules.gambling.coinflip;

import org.axtin.container.facade.Container;
import org.bukkit.entity.Player;

public class CoinFlip {

	private Player challenger;
	private Player opponent;
	private double amount;
	private CoinFlipInventory inventory;
	private boolean done = false;
	
	public CoinFlip(Player challenger, Player opponent, double amount) {
		this.setChallenger(challenger);
		this.setOpponent(opponent);
		this.setAmount(amount);
		this.inventory = new CoinFlipInventory(this);
		this.inventory.open();
	}


	public Player getChallenger() {
		return challenger;
	}


	public void setChallenger(Player challenger) {
		this.challenger = challenger;
	}


	public Player getOpponent() {
		return opponent;
	}


	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public void finishGame() {
		this.done = true;
		Container.get(CoinFlipManager.class).remove(this);
	}
	
	public boolean isFinished() {
		return this.done;
	}
	
	
}
