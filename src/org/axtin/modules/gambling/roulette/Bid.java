package org.axtin.modules.gambling.roulette;

import org.bukkit.entity.Player;

public class Bid implements Comparable<Bid> {

	private Player bidder;
	private double amount;
	
	public Bid(Player player, double amount) {
		this.bidder = player;
		this.amount = amount;
	}
	
	public Player getBidder() {
		return this.bidder;
	}
	
	public Double getAmount() {
		return this.amount;
	}
	
	public int compareTo(Bid o) {
		return Double.compare(amount, o.amount);
	}
	
}
