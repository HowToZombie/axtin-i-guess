package org.axtin.modules.pickaxe;

import org.axtin.modules.mines.RankHolder;
import org.axtin.util.ActionBarUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 4/26/2017.
 */
public class XPGainListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void BreakBlock(BlockBreakEvent e){
        ItemStack hand = e.getPlayer().getItemInHand();
        if(hand.getType() !=null){
            if(hand.hasItemMeta()){
                if(hand.getItemMeta().hasLore()) {
                    boolean inMine = false;
                    for(RankHolder mine : RankHolder.data.values()){
                        if(mine.getCube().containsLocation(e.getBlock().getLocation())) {
                            inMine= true;
                        }
                    }
                    if(inMine) {
                        PickaxeManager pm = PickaxeManager.getPickaxe(hand.getType());
                        if (!(pm.getIdentifier() < 0)) {
                            List<String> lore = hand.getItemMeta().getLore();

                            String level = lore.get(0).split(" ")[1];
                            String xp = lore.get(2).split(" ")[1];

                            int levelInt = Integer.valueOf(level);
                            int xpEarned = Integer.valueOf(xp.split("/")[0]) + 1;
                            int points = Integer.valueOf(lore.get(3).split(" ")[2]);

                            if (pm.getIdentifier() == 4 && levelInt == 4) {
                                return;
                            }

                            if (xpEarned >= pm.getBasexp() * levelInt) {
                                levelInt++;
                                points++;
                                if (levelInt > 4) {
                                    e.getPlayer().setItemInHand(PickaxeManager.getPickaxe(pm.getIdentifier() + 1).getPickaxeWithLevel(1, points));
                                }/*else{

                            if(hand.containsEnchantment(Enchantment.DIG_SPEED)){
                                int enchantLevel = hand.getEnchantmentLevel(Enchantment.DIG_SPEED);
                                hand.removeEnchantment(Enchantment.DIG_SPEED);
                                hand.addUnsafeEnchantment(Enchantment.DIG_SPEED, enchantLevel+1);
                            }else{
                                hand.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                            }
                        }*/
                                xpEarned = 0;
                            }

                            String newVisual = PickaxeManager.getXpBar(xpEarned, pm.getBasexp() * levelInt);

                            ActionBarUtil.sendActionBar(e.getPlayer(), ChatColor.LIGHT_PURPLE + "XP: " + xpEarned + "/" + pm.getBasexp() * levelInt);

                            ItemMeta meta = hand.getItemMeta();
                            lore.set(0, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Level " + levelInt);
                            lore.set(1, newVisual);
                            lore.set(2, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "XP: " + xpEarned + "/" + pm.getBasexp() * levelInt);
                            lore.set(3, ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Upgrade Points: " + points);
                            meta.setLore(lore);
                            hand.setItemMeta(meta);
                        }
                    }
                }
                //TODO finish checks and give xp per block? or different xp per different block?
            }
        }
    }
}
