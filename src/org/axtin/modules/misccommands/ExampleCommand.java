package org.axtin.modules.misccommands;

import org.axtin.command.AxtinCommand;
import org.axtin.util.Hologram;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ExampleCommand extends AxtinCommand {
    public ExampleCommand() {
        super("test");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player p = (Player) sender;
        p.sendMessage("This is an example command.");
        return false;
    }
}
