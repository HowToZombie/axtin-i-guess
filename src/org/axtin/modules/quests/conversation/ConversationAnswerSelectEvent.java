package org.axtin.modules.quests.conversation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


/**
 *
 * @author admin
 */
public class ConversationAnswerSelectEvent extends Event {

    public HandlerList handerList = new HandlerList();
    private final Player player;
    private final int index;
	private Conversation conv;
            
    
    public ConversationAnswerSelectEvent(Player player, int index, Conversation conv) {
        this.player = player;
        this.index = index;
        this.conv = conv;
        conv.sendAnswer(player, index);
    }
    
    public Conversation getConversation() {
    	return this.conv;
    }
    
    public int getAnswerIndex() {
        return this.index;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    @Override
    public HandlerList getHandlers() {
        return this.handerList;
    }
    
    public HandlerList getHandlerList() {
        return this.handerList;
    }
    
}
