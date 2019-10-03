package org.axtin.user;

import org.bukkit.entity.Player;

public class User {
    private Player player;
    private UserData data;

    public User(Player player, UserData data) {
        this.player = player;
        this.data = data;
    }

    public Player getPlayer() {
        return this.player;
    }

    public UserData getData() {
        return this.data;
    }
}
