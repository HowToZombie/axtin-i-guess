package org.axtin.command;

import net.md_5.bungee.api.ChatColor;

public enum CommandErrors {
    NO_PERMISSION(ChatColor.RED + "You don't have permission to do this command!"),
    INVALID_TARGET(ChatColor.RED + "Target player is not online!"),
    NO_TARGET(ChatColor.RED + "There is no target player!"),
    PLAYER_ONLY(ChatColor.RED + "Only players can use this command!"),
    NON_EXISTANT_TARGET(ChatColor.RED + "Target player does not exist!");

    private final String errorMessage;

    private CommandErrors(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String toString(){
        return this.errorMessage;
    }
}
