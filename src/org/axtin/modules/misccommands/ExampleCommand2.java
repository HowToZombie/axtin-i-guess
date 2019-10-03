package org.axtin.modules.misccommands;

import org.axtin.command.AxtinCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExampleCommand2 extends AxtinCommand {
    public ExampleCommand2() {
        super("example2");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player p = (Player) sender;
        p.sendMessage("Hello!");

        return false;

    }
}
