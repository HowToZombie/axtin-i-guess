package org.axtin.deprecated.modules.companions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CompanionInteractEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private Companion companion;
	
	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public CompanionInteractEvent(Player player, Companion companion) {
		this.player = player;
		this.companion = companion;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Companion getCompanion() {
		return this.companion;
	}

}
