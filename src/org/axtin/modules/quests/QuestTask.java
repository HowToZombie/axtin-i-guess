
package org.axtin.modules.quests;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

/**
 *
 * @author Alan Tavakoli
 */
public class QuestTask {

    private TaskType type;
    private String completionMessage;
    private Integer amount;
    private Location target;
    private EntityType mobType;
    private Material material;
    
    public QuestTask(TaskType type, String completionMessage, Integer amount, Location target, EntityType mobType, Material material) {
        this.type = type;
        this.completionMessage = completionMessage;
        this.amount = amount;
        this.target = target;
        this.mobType = mobType;
        this.material = material;
    }
    
    public void setSaveSection(String path, YamlConfiguration config) {
        ConfigurationSection section = config.createSection(path);
        section.set("Type", type.toString());
        section.set("CompletionMessage", completionMessage);;
        if(amount != 0)
            section.set("Amount", amount);
        if(target != null)
            section.set("TargetLocation", LocationToString(target));
        if(mobType != null) 
            section.set("MobType", mobType.toString());
        if(material != null) 
            section.set("Material", material.toString());
    }
    
    public static QuestTask getTask(YamlConfiguration config, String path) {
         ConfigurationSection section = config.getConfigurationSection(path);
         Bukkit.broadcastMessage(section.getCurrentPath());
         TaskType type = TaskType.valueOf(section.getString("Type"));
         String msg = section.getString("CompletionMessage");
         Integer amount = section.getInt("Amount");
         Location loc = null;
         EntityType mobType = null;
         Material mat = null;
         if(section.contains("TargetLocation"))
             loc = StringToLocation(section.getString("TargetLocation"));
         if(section.contains("MobType"))
             mobType = EntityType.valueOf(section.getString("MobType"));
         if(section.contains("Material"))
             mat = Material.valueOf(section.getString("Mateerial"));
         return new QuestTask(type, msg, amount, loc, mobType, mat);
    }
    
    private String LocationToString(Location location) {
        return String.format("%s:%d:%d:%d", location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    public static Location StringToLocation(String string) {
        String[] parts = string.split(":");
        World world = Bukkit.getWorld(parts[0]);
        int x = Integer.valueOf(parts[1]);
        int y = Integer.valueOf(parts[2]);
        int z = Integer.valueOf(parts[3]);
        return new Location(world, x, y , z);
    }
    
    
    public TaskType getType() {
        return this.type;
    }
    
    public Material getMaterial() {
        return this.material;
    }
    
    public int getGoal() {
        return this.amount;
    }

    public Location getLocation() {
        return this.target;
    }
    
}
