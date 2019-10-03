
package org.axtin.modules.quests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.axtin.container.facade.Container;
import org.axtin.modules.quests.conversation.Conversation;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Alan Tavakoli
 */
public class QuestHandler {
    
    public HashMap<Player, Quest> editing;
    public List<Quest> quests;
    public String absolutePath;

    public QuestHandler() {
        editing = new HashMap<>();
        quests = new ArrayList<>();
        absolutePath = Container.get(Plugin.class).getDataFolder().getAbsolutePath();
        File dir = new File(absolutePath + "/Quests/");
        File configFile = new File(dir, "Config.yml");
        File dataFile = new File(dir, "Data.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if(config.getConfigurationSection("Quests") != null)
            config.getConfigurationSection("Quests").getKeys(false).forEach((path) -> {
                Quest quest = Quest.getQuest(config, "Quests." + path);
                quest.readQTPs(YamlConfiguration.loadConfiguration(dataFile));
                quests.add(quest);
            });
    }
    
    public boolean isInConversation(Player player) {
        for(Quest quest : quests) {
        	if(quest.isInConversation(player))
        		return true;
        }
        return Conversation.globalConversations.containsKey(player);
    }
    
    public Conversation getConversation(Player player) {
        for(Quest quest : quests) {
            if(quest.isInConversation(player))
                return quest.getConversation();
        }
        if(Conversation.globalConversations.containsKey(player))
        	return Conversation.globalConversations.get(player);
        return null;
    }
    
    public boolean questExists(String name) {
        return quests.stream().anyMatch((quest) -> (quest.getName().equalsIgnoreCase(name.trim())));
    }
    
    public void putEditing(Player editor, Quest target) {
        editing.put(editor, target);
    }

    public Quest getQuest(String name) {
        if(questExists(name))
            for(Quest quest : this.quests) {
                if(quest.getName().equalsIgnoreCase(name.trim()))
                    return quest;
            }
    return null;
    }
    
    public List<Quest> readQuestsFromFile() {
    File dir = new File(absolutePath + "/Quests/");
    File configFile = new File(dir, "Config.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
    List<Quest> list = new ArrayList<>();
    if(configFile.exists()) {
        if(config.getConfigurationSection("Quests") != null) {
            config.getConfigurationSection("Quests").getKeys(false).forEach((path) -> {
                list.add(Quest.getQuest(config, "Quest." + path));
            });
        }
    }
                
    return list;
    }
    
    public QuestTaskProgress getQTP(UUID uuid) {
        for(Quest quest : quests) {
            if(quest.hasProgress(uuid))
                return quest.getProgress(uuid);
        }
        return null;
    }
    
    public void addQuest(Quest quest) {
        this.quests.add(quest);
        Bukkit.broadcastMessage("Executed addQuest(Quest quest)");
    }

    public void shutDown() {
        File dir = new File(absolutePath + "/Quests/");
        File configFile = new File(dir, "Config.yml");
        File dataFile = new File(dir, "Data.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        YamlConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        Bukkit.broadcastMessage(this.quests.size() + " size of quests array");
        this.quests.forEach((quest) -> {
            quest.save(config);
            quest.saveProgresses(dataConfig);
        });
        try {
            config.save(configFile);
            dataConfig.save(dataFile);
        } catch (IOException ex) {
            Logger.getLogger(QuestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
