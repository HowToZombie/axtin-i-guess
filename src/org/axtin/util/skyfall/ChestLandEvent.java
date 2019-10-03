package org.axtin.util.skyfall;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChestLandEvent extends Event {
	
	private FallingChest fc;
	
	private static final HandlerList handlers = new HandlerList();

	public ChestLandEvent(FallingChest fc) {
		this.fc = fc;
	}
	
	public FallingChest getFallingChest() {
		return this.fc;
	}
	
	
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
