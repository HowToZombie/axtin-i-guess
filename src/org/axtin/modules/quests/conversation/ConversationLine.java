/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.axtin.modules.quests.conversation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Alan Tavakoli
 */
public class ConversationLine {
    
    /*
    * A line with possible answers.
    */
    private String line;
    private LinkedHashMap<String, ClickAction> clickableAnswers;
    
    public ConversationLine(String line) {
        this.line = line;
        clickableAnswers = new LinkedHashMap<>();
    }
    
    public boolean shouldProgress(int answer) {
    	int i = 0;
    	for(Entry<String, ClickAction> entry : this.clickableAnswers.entrySet()) {
    		if(i == answer)
    			return entry.getValue().shouldProgress();
    		i++;
    	}
    	return false;
    	
    }
    
    public void addAnswer(String answer, String action, boolean progress) {
        this.clickableAnswers.put(answer, new ClickAction(action, progress));
    }
    
    public ConversationLine(YamlConfiguration config, String path) {
    	line = config.getString(path + ".Line");
        clickableAnswers = new LinkedHashMap<>();
    	for(String key : config.getConfigurationSection(path + ".Answers").getKeys(false)) 
    		clickableAnswers.put(key, new ClickAction(config.getString(path + ".Answer." + key)));
    	
    }
    
    public void save(YamlConfiguration config, String path) {
    	config.set(path + ".Line", line);
    	for(Entry<String, ClickAction> e : clickableAnswers.entrySet()) 
    		config.set(path + ".Answers." + e.getKey(), e.getValue().toString());
    }
    
    public void send(Player player) {
        String tempLine = line;
        tempLine = tempLine.replaceAll("@CustomName", player.getCustomName())
                .replaceAll("@Name", player.getName());
        
        player.sendMessage(tempLine);
        List<Entry<String, ClickAction>> keys = new ArrayList<Entry<String, ClickAction>>(clickableAnswers.entrySet());
        for (int i = 0; i < keys.size(); i++) {
            Entry<String, ClickAction> entry = keys.get(i);
            TextComponent message = new TextComponent(entry.getKey());
            message.setColor(ChatColor.GOLD);
            message.setClickEvent(entry.getValue().getEvent(i));
            player.spigot().sendMessage(message);
            
        }
        
        
    }
    
}
