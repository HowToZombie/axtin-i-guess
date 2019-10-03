package org.axtin.modules.messaging;

import net.md_5.bungee.api.ChatColor;
import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.util.ArrayToMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinMessage extends AxtinCommand {

    private final AxtinSendMessage sendMessage = AxtinSendMessage.getInstance();

    private final String usage = "&c/msg <player> <message>";

    public AxtinMessage() {
        super("msg", "Message a player", "/msg <target> <message>", "tell", "whisper", "m");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        if(strings.length<2){
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',usage));
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(strings[0]);
        if(targetPlayer==null){
            commandSender.sendMessage(CommandErrors.INVALID_TARGET.toString());
            return true;
        }

        String message = ArrayToMessage.formatStringsStatic(Arrays.copyOfRange(strings, 1, strings.length));
        sendMessage.sendMessage(commandSender, targetPlayer, message);

        return true;
    }
}
