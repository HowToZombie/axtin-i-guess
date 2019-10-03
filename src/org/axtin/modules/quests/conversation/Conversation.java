/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.axtin.modules.quests.conversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.axtin.container.facade.Container;
import org.axtin.modules.quests.Quest;
import org.axtin.modules.quests.QuestHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author admin
 */
public class Conversation {
    
    private ArrayList<ConversationLine> lines;
    private Map<Player, Integer> activeConversations;
    public static HashMap<Player, Conversation> globalConversations = new HashMap<>();
    
    public Conversation() {
        lines = new ArrayList<>();
        activeConversations = new HashMap<>();
    }
    
    public boolean isInConversation(Player player) {
        return activeConversations.containsKey(player);
    }
    
    public void addLine(String message) {
        lines.add(new ConversationLine(message));
    }
    
    public void save(YamlConfiguration config, Quest quest) {
    	String path = "Quests." + quest.getName() + ".Conversation";
    	for(int i = 0; i < lines.size(); i++) {
    		ConversationLine line = lines.get(i);
    		line.save(config, path + ".Line_" + i);
    	}
    }
    
    public Conversation(YamlConfiguration config, String quest) {
    	lines = new ArrayList<>();
        activeConversations = new HashMap<>();
    	String path = "Quests." + quest + ".Conversation";
    	for(String key : config.getConfigurationSection(path).getKeys(false)) 
    		lines.add(new ConversationLine(config, path + "." + key));
    	
    }
    
    public int startConversation(Player player) {
        QuestHandler qh = Container.get(QuestHandler.class);
        if(qh.isInConversation(player))
            return 1; //Player already is in another conversation
        if(lines.isEmpty())
            return 2; //No lines have been added
        activeConversations.put(player, 0);
        globalConversations.put(player, this);
        lines.get(0).send(player);
        return 0;
    }
    
    public void sendAnswer(Player player, int answer) {
        if(activeConversations.containsKey(player)) {
        	int current = activeConversations.get(player);
        	if((current + 1) > (lines.size() - 1)) {
        		//TODO Conversation end
        		Bukkit.broadcastMessage("End of conversation");
        		return;
        	}
        	if(lines.get(current).shouldProgress(answer)) {
        		current++;
        		activeConversations.put(player, current);
        		lines.get(current).send(player);
        	} else {
        		Bukkit.broadcastMessage("End of conversation");
        		//TODO end of conversation
        	}
        }
    }
    
    public void addEvent(int index, String answer, String action, boolean progress) {
        lines.get(index).addAnswer(answer, action, progress);
    }
    
    
}
