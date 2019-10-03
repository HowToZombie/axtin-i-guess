
package org.axtin.modules.quests;

import java.util.UUID;
import org.axtin.container.facade.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author Alan Tavakoli
 */
public class QuestListener implements Listener {

    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
    	QuestHandler qh = Container.get(QuestHandler.class);
    	if(qh.isInConversation(e.getPlayer())) 
    		e.getRecipients().remove(e.getPlayer());
    }
	
    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() != null) {
            UUID uuid = e.getEntity().getKiller().getUniqueId();
            if(Container.get(QuestHandler.class).getQTP(uuid) != null) {
                 QuestTaskProgress qtp = Container.get(QuestHandler.class).getQTP(uuid);
                 if(qtp.getTask().getType().equals(TaskType.KILL_PLAYERS))
                     qtp.addKill();
            }
        }
    }
    
    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        if(e.getEntity().getKiller() != null) {
            UUID uuid = e.getEntity().getKiller().getUniqueId();
            if(Container.get(QuestHandler.class).getQTP(uuid) != null) {
                 QuestTaskProgress qtp = Container.get(QuestHandler.class).getQTP(uuid);
                 if(qtp.getTask().getType().equals(TaskType.KILL_MOBS))
                     qtp.addMobKill();
            }
        }
    }
    
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
         UUID uuid = e.getPlayer().getUniqueId();
         if(Container.get(QuestHandler.class).getQTP(uuid) != null) {
            QuestTaskProgress qtp = Container.get(QuestHandler.class).getQTP(uuid);
            if(qtp.getTask().getType().equals(TaskType.REACH_LOCATION)) {
                qtp.checkDistance(e.getPlayer().getLocation());
            }
         }
    }
    
    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if(Container.get(QuestHandler.class).getQTP(uuid) != null) {
            QuestTaskProgress qtp = Container.get(QuestHandler.class).getQTP(uuid);
            if(qtp.getTask().getType().equals(TaskType.MINE_BLOCKS))
                qtp.addMinedBlock(e.getBlock());
        }
    }
    
    
    
}
