package org.axtin.modules.messaging;

import org.axtin.command.AxtinCommand;
import org.axtin.util.ArrayToMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AxtinReply extends AxtinCommand {

    private final AxtinSendMessage sendMessage;
    private final ReplyProvider replyProvider;

    public AxtinReply() {
        super("r", "Reply to a player", "/reply", "reply");
        this.sendMessage = AxtinSendMessage.getInstance();
        this.replyProvider = ReplyProvider.getInstance();
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        String message = ArrayToMessage.formatStringsStatic(strings);
        try{
            Player targetPlayer = replyProvider.getPlayer(commandSender);
            this.sendMessage.sendMessage(commandSender, targetPlayer, message);
        }
        catch(NoReplyException e){
            commandSender.sendMessage(e.getError());
        }
        return true;
    }
}
