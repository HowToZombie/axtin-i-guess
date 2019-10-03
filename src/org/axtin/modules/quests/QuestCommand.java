
package org.axtin.modules.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

/**
 *
 * @author Alan Tavakoli
 */
public class QuestCommand extends AxtinCommand {

    public QuestCommand() {
        super("quest");
    }
    
    @Override
    public boolean execute(CommandSender cs, String label, String[] args) {
        if(cs instanceof Player) {
            Player player = (Player) cs;
            QuestHandler qh = Container.get(QuestHandler.class);
            if(args.length < 1) {
                //show help
            } else {
                if(args[0].equalsIgnoreCase("edit")) {
                    if(args.length < 2) {
                        player.sendMessage("/quest edit (name) (possible arguments)");
                    } else if(args.length == 2) {
                        if(!qh.questExists(args[1])) {
                            player.sendMessage("Quest does not exist.");
                            return true;
                        }
                        Quest quest = qh.getQuest(args[1]);
                        qh.putEditing(player, quest);
                        player.sendMessage("Editing quest " + quest.getName());
                    } else if(args.length > 2) {
                        Quest quest = qh.getQuest(args[1]);

                        String cmd = args[2];
                        if(cmd.equalsIgnoreCase("add")) {
                            if(args.length < 4) {
                                player.sendMessage("/quest edit (name) add (KILL_MOBS, KILL_PLAYERS, MINE_BLOCKS, REACH_LOCATION)");
                            } else 
                                player.sendMessage(processString(cmd, args, quest, player));
                            
                        } else if(cmd.equalsIgnoreCase("remove")) {
                            
                        }
                    }
                }
                else if(args[0].equalsIgnoreCase("create")) {
                    if(args.length < 3) {
                        player.sendMessage("/quest create (name) (permission)");
                    } else {
                        Quest quest = new Quest(args[1], args[2]);
                        Container.get(QuestHandler.class).addQuest(quest);
                        player.sendMessage("Created quest " + quest.getName() + ", type /quest edit " + quest.getName() + ", to edit the quest.");
                    }
                }
            }
            
        }
        return true;
    }
    
    @SuppressWarnings("deprecation")
	private String processString(String cmd, String[] args, Quest quest, Player player) {
        if(args[3].equalsIgnoreCase("KILL_MOBS")) {
            if(args.length < 7) 
                return "/quest edit (name) add KILL_MOBS (mobtype) (amount) (completionMessage)";
            else {
                if(!isEnumElement(EntityType.class, args[4].toUpperCase()))
                    return "No such mob.";
                try {
                    Integer.valueOf(args[5]);
                } catch(NumberFormatException e) {
                    return "Amount must be a number.";
                }
                EntityType type = EntityType.valueOf(args[4]);
                int amount = Integer.parseInt(args[5]);
                quest.addQuestTask(TaskType.KILL_MOBS, type, amount, getAllStringsFromIndex(args, 6));
                return "Successfully added a quest task: kill " + amount + " " + type.getName();
            }
            
        } else if(args[3].equalsIgnoreCase("KILL_PLAYERS")) {
            if(args.length < 6)
                return "/quest edit (name) add KILL_PLAYERS (amount) (completionMessage)";
            else {
                try {
                    Integer.valueOf(args[4]);
                } catch(NumberFormatException e) {
                    return "Amount must be a number.";
                }
                int amount = Integer.valueOf(args[4]);
                quest.addQuestTask(TaskType.KILL_PLAYERS, amount, getAllStringsFromIndex(args, 5));
                String part = amount > 1 ? "players" : "player";
                return "Successfully added a quest task: kill " + amount + " " + part;
            }
        } else if(args[3].equalsIgnoreCase("REACH_LOCATION")) {
            if(args.length < 6) 
                return "/quest edit (name) add REACH_LOCATION (distance) (completionMessage)";
            else {
                try {
                    Integer.valueOf(args[4]);
                } catch(NumberFormatException e) {
                    return "Distance must be a number.";
                }
                int amount = Integer.valueOf(args[4]);
                quest.addQuestTask(TaskType.REACH_LOCATION, player.getLocation(), amount, getAllStringsFromIndex(args, 5));
                return "Successfully added a quest task: reach a distance closer than " + amount + " from selected locatiomn";
            }
        } else if(args[3].equalsIgnoreCase("MINE_BLOCKS")) {
            if(args.length < 7)
                return "/quest edit (name) add MINE_BLOCKS (material) (amount) (completionMessage)";
            else {
                if(!isEnumElement(Material.class, args[4].toUpperCase()))
                    return "No such material.";
                
                try {
                    Integer.valueOf(args[5]);
                } catch(NumberFormatException e) {
                    return "Amount must be a number.";
                }
                int amount = Integer.valueOf(args[5]);
                Material mat = Material.valueOf(args[4].toUpperCase());
                quest.addQuestTask(TaskType.MINE_BLOCKS, mat, amount, getAllStringsFromIndex(args, 6));
                return "Successfully added a quest task: mine " + amount + " of " + mat.name();
            }
        }
        return "No such task type.";
    }
    
    private String getAllStringsFromIndex(String[] array, int i) {
        if(i + 1 > array.length)
            return "";
        String str = "";
        for (int j = i; j < array.length; j++) 
            str += array[j] + " ";
        return str.trim();
            
    }
    
    public LivingEntity getTarget(Player player) {
    int range = 60;
    List < Entity > nearbyE = player.getNearbyEntities(range, range, range);
    ArrayList < LivingEntity > livingE = new ArrayList <> ();

    nearbyE.stream().filter((e) -> (e instanceof LivingEntity)).forEachOrdered((e) -> {
        livingE.add((LivingEntity) e);
        });

    LivingEntity target = null;
    BlockIterator bItr = new BlockIterator(player, range);
    Block block;
    Location loc;
    int bx, by, bz;
    double ex, ey, ez;
    // loop through player's line of sight
    while (bItr.hasNext()) {
        block = bItr.next();
        bx = block.getX();
        by = block.getY();
        bz = block.getZ();
        // check for entities near this block in the line of sight
        for (LivingEntity e: livingE) {
            loc = e.getLocation();
            ex = loc.getX();
            ey = loc.getY();
            ez = loc.getZ();
            if ((bx - .75 <= ex && ex <= bx + 1.75) &&
                (bz - .75 <= ez && ez <= bz + 1.75) &&
                (by - 1 <= ey && ey <= by + 2.5)) {
                // entity is close enough, set target and stop
                target = e;
                break;
            }
        }
    }
    return target;

    }
    
    public <E extends Enum<E>> boolean isEnumElement(Class<E> clazz, String value) {
    	return Arrays.stream(EnumSet.allOf(clazz).toArray()).anyMatch((en) -> (en.toString().equalsIgnoreCase(value)));
    }

    
    
}
