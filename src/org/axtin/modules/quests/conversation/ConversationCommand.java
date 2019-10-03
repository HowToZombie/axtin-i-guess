/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.axtin.modules.quests.conversation;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.modules.quests.QuestHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author admin
 */
public class ConversationCommand extends AxtinCommand {

    public ConversationCommand() {
        super("conversation");
    }
    
    
    @Override
    public boolean execute(CommandSender cs, String string, String[] args) {
        if(cs instanceof Player) {
            Player player = (Player) cs;
            if(Container.get(QuestHandler.class).isInConversation(player)) {
                int index = Integer.valueOf(args[0]);
                String commandString = getAllStringsFromIndex(args, 1);
                String[] commands = commandString.split(";");
                for(String cmd : commands) {
                    String type = cmd.split(" ")[0];
                    cmd = cmd.replace(type, "").trim();
                    if(type.equalsIgnoreCase("console")) 
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    else if(type.equalsIgnoreCase("player"))
                        Bukkit.getServer().dispatchCommand(cs, cmd);
                }
                Bukkit.getPluginManager().callEvent(new ConversationAnswerSelectEvent(player, index, Container.get(QuestHandler.class).getConversation(player)));
            }
        }
        return true;
    }
    
    private String getAllStringsFromIndex(String[] array, int from) {
        if(from + 1 > array.length)
            return "";
        String str = "";
        for (int j = from; j < array.length; j++) 
            str += array[j] + " ";
        return str.trim();
            
    }
    
}
