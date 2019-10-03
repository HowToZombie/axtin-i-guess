
package org.axtin.modules.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.axtin.modules.quests.conversation.Conversation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 *
 * @author Alan Tavakoli
 */
public class Quest {
    
    /*
    * Properties
    */
    
    private String questName;
    private String permission;
    private List<QuestTask> tasks;
    private List<QuestTaskProgress> progresses;
    private Conversation conversation;
    
    public Quest(String questName, String permission) {
        this.questName = questName;
        this.permission = permission;
        this.tasks = new ArrayList<>();
        this.progresses = new ArrayList<>();
        this.conversation = new Conversation();
    }
    public Quest(String questName, String permission, List<QuestTask> tasks) {
        this.questName = questName;
        this.permission = permission;
        this.tasks = tasks;
        this.progresses = new ArrayList<>();
    }
    
    public boolean isInConversation(Player player) {
        return this.conversation.isInConversation(player);
    }
    
    public Conversation getConversation() {
        return this.conversation;
    }
    
    public void saveProgresses(YamlConfiguration dataConfig) {
        String path;
        for(QuestTaskProgress qtp : progresses) {
            path = "Data." + this.questName + ".QTPs." + qtp.getUniqueId().toString();
            dataConfig.set(path, saveProgressToString(qtp));
        }
    }
    
    public void readQTPs(YamlConfiguration dataConfig) {
        if(dataConfig.getConfigurationSection("Data." + this.questName + ".QTPs") != null) {
            dataConfig.getConfigurationSection("Data." + this.questName + ".QTPs").getKeys(false).stream().map((path) -> "Data." + this.questName + ".QTPs." + path).map((fullPath) -> dataConfig.getString(fullPath)).forEachOrdered((data) -> {
                this.progresses.add(readProgressFromString(data));
            });
        }
    }  
    
    private String saveProgressToString(QuestTaskProgress qtp) {
        return String.format("%s:%d:%d", qtp.getUniqueId().toString(), this.tasks.indexOf(qtp.getTask()), qtp.getAmount());
    }
    
    private QuestTaskProgress readProgressFromString(String str) {
        return new QuestTaskProgress(this, str);
    }
    
    public void save(YamlConfiguration config) {
        Bukkit.broadcastMessage("Saving Quest...");
        ConfigurationSection section = config.createSection("Quests." + questName);
        section.set("Name", this.questName);
        section.set("Permission", this.permission);
        tasks.forEach((task) -> {
            task.setSaveSection("Quests." + questName + ".Tasks." + tasks.indexOf(task), config);
        });
    }
    
    public static Quest getQuest(YamlConfiguration config, String path) {
        ConfigurationSection section = config.getConfigurationSection(path);
        String questName = section.getString("Name");
        String permission = section.getString("Permission");
        List<QuestTask> list = new ArrayList<>();
        if(section.getConfigurationSection("Tasks") != null)
            section.getConfigurationSection("Tasks").getKeys(false).forEach((taskPath) -> {
                String finalPath = path + ".Tasks." + taskPath;
                Bukkit.broadcastMessage(finalPath);
                list.add(QuestTask.getTask(config, finalPath));
            }); 
        return new Quest(questName, permission, list);
    }
    
    public void addQuestTask(TaskType type, EntityType mobType, int amount, String message) {
        tasks.add(new QuestTask(type, message, amount, null, mobType, null));
    }
    
    public void addQuestTask(TaskType type, Location targetLocation, int distance, String message) {
        tasks.add(new QuestTask(type, message, distance, targetLocation, null, null));
    }
    
    public void addQuestTask(TaskType type, Material material, int amount, String message) {
        tasks.add(new QuestTask(type, message, amount, null, null, material));
    }
    
    public void addQuestTask(TaskType type, int amount, String message) {
        tasks.add(new QuestTask(type, message, amount, null, null, null));
    }
    
    public boolean hasProgress(UUID uuid) {
        return progresses.stream().anyMatch((qtp) -> (qtp.getUniqueId().equals(uuid)));
    }
    
    public QuestTaskProgress getProgress(UUID uuid) {
        if(hasProgress(uuid))
            for(QuestTaskProgress qtp : progresses)
                if(qtp.getUniqueId().equals(uuid))
                    return qtp;
        QuestTaskProgress qtp = new QuestTaskProgress(this, this.tasks.get(0), uuid);
        return qtp;
    }
    
    public List<QuestTask> getTasks() {
        return this.tasks;
    }

    public void complete(UUID uuid) {
        
    }
    
    public String getName() {
        return this.questName;
    }

}

