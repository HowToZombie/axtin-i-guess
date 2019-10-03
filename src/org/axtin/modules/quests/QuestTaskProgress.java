
package org.axtin.modules.quests;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 *
 * @author Alan Tavakoli
 */
public class QuestTaskProgress {

    private Quest quest;
    private QuestTask task;
    private int trackedAmount;
    private int goal;
    private UUID uuid;
    
    public QuestTaskProgress(Quest quest, QuestTask task, UUID uuid) {
        this.quest = quest;
        this.task = task;
        this.uuid = uuid;
        if(this.task.getType().requiresInteger()) {
            this.goal = this.task.getGoal();
        }
        this.trackedAmount = 0;

    }
    
    public QuestTaskProgress(Quest quest, String str) {
        this.quest = quest;
        String[] parts = str.split(":");
        uuid = UUID.fromString(parts[0]);
        int taskIndex = Integer.valueOf(parts[1]);
        trackedAmount = Integer.valueOf(parts[2]);
        this.task = quest.getTasks().get(taskIndex);
        if(this.task.getType().requiresInteger())
            this.goal = this.task.getGoal();
    }
    
    public int getAmount() {
        return this.trackedAmount;
    }
    
    public UUID getUniqueId() {
        return this.uuid;
    }
    
    public QuestTask getTask() {
        return this.task;
    }
    
    public void addKill() {
        trackedAmount++;
        if(trackedAmount >= goal) 
            complete();
        
    }
    
    public void addMobKill() {
        trackedAmount++;
        if(trackedAmount >= goal) 
            complete();
    }
    
    public void addMinedBlock(Block block) {
        if(block.getType().equals(this.task.getMaterial()))
            trackedAmount++;
        if(trackedAmount >= goal) 
            complete();
    }
    
    public void complete() {
        if((quest.getTasks().size() - 1) < (quest.getTasks().indexOf(this.task) + 1)) {
            //Quest completed overall
            quest.complete(this.uuid);
        } else {
            QuestTask newTask = quest.getTasks().get(quest.getTasks().indexOf(this.task) + 1);
            this.task = newTask;
            this.trackedAmount = 0;
            if(newTask.getType().requiresInteger())
                this.goal = newTask.getGoal();
        }
    }

    public void checkDistance(Location location) {
        if(location.distance(this.task.getLocation()) < this.goal) 
            complete();
    }
    
    
    
}
