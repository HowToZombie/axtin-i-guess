package org.axtin.modules.messaging;

import org.axtin.command.CommandErrors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew on 26/03/2017.
 */
public class ReplyProvider {

    private final Map<String, String> repliedTo;

    private static ReplyProvider instance = null;

    public static ReplyProvider getInstance(){
        if(instance==null){
           instance =  new ReplyProvider();
        }
        return instance;
    }

    private ReplyProvider(){
        this.repliedTo = new HashMap<>();
    }

    public Player getPlayer(CommandSender sender) throws NoReplyException{

        String name = sender.getName();
        if(!this.repliedTo.containsKey(name)) throw new NoReplyException(CommandErrors.NO_TARGET);

        String targetPlayerName = this.repliedTo.get(name);
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if(targetPlayer==null) throw new NoReplyException(CommandErrors.INVALID_TARGET);

        return targetPlayer;
    }

    public void addPlayers(CommandSender playerOne, Player playerTwo){
        String playerOneName = playerOne.getName();
        String playerTwoName = playerTwo.getName();

        this.repliedTo.put(playerOneName, playerTwoName);
        this.repliedTo.put(playerTwoName, playerOneName);
    }




}
