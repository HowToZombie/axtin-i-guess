package org.axtin.deprecated.modules.customenchants.enchantments;

import org.axtin.deprecated.modules.customenchants.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Zombie on 5/22/2017.
 */
public class ExampleEnchantment implements Listener {
    //Create new enchantment
    Enchantment Example = new Enchantment("Example", 2, "example");

    //Customize enchantment here
    @EventHandler
    public void runEnchantment(BlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack handItem = p.getItemInHand();
        if (Example.detectEnchantment(handItem)) {
            //Example of different effects for different levels
            if (Example.getLevel(handItem) == 1) {
                p.sendMessage("test 1");
            } else if (Example.getLevel(handItem) == 2) {
                p.sendMessage("test 2");
            }
        }
    }
}
