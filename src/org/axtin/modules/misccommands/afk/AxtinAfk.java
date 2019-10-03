package org.axtin.modules.misccommands.afk;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinAfk extends AxtinCommand {

    private final AfkHandler afkHandler;

    public AxtinAfk() {
        super("afk");
        this.afkHandler = AfkHandler.getInstance();
        AfkChecker.getInstance();
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        if(!(commandSender instanceof Player)){
            commandSender.sendMessage(CommandErrors.PLAYER_ONLY.toString());
            return true;
        }

        Player player = (Player) commandSender;


        afkHandler.updatePlayer(player.getName(), player.getLocation().toVector());
        return true;
    }
}
