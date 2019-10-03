package org.axtin.modules.ships;

import org.axtin.container.facade.Container;
import org.axtin.user.UserRepository;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EnterShipEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    private Player exiter;

    public EnterShipEvent(Player p) {
        this.exiter = p;
    }

    public Player getPlayer() {
        return exiter;
    }

    public Ship getShip() {
        return Ship.ships.get(Container.get(UserRepository.class).getUser(exiter.getUniqueId()));
    }

    public Location getDestination() {
        return Ship.ships.get(Container.get(UserRepository.class).getUser(exiter.getUniqueId())).getPad();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
