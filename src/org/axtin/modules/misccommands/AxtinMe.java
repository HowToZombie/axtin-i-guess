package org.axtin.modules.misccommands;

import org.axtin.command.AxtinCommand;
import org.axtin.util.ArrayToMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinMe extends AxtinCommand {

    public AxtinMe(){
        super("me");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        String message = (new ArrayToMessage(strings)).getString();
        Bukkit.broadcastMessage(ChatColor.GRAY + "*" + commandSender.getName() + " " + message);
        return true;
    }
}
