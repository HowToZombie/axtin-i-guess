package org.axtin.deprecated.modules.customenchants;

import org.axtin.Axtin;
import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.deprecated.modules.customenchants.Enchantment;
import org.axtin.user.UserRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Zombie on 5/21/2017.
 */
public class EnchantmentCommand extends AxtinCommand {

    public EnchantmentCommand() {
        super("enchant");
    }

    public boolean execute(CommandSender p, String name, String[] args) {
        if (p instanceof Player) {

            if (Container.get(UserRepository.class).getUser(((Player) p).getUniqueId()).getData().getStaffRole().getIdentifier() < 21) {
                p.sendMessage(CommandErrors.NO_PERMISSION.toString());
                return true;
            }
            if (args.length == 0) {
                return true;
            }
            ItemStack handItem = ((Player) p).getItemInHand();
            for (Enchantment enchant : Enchantment.enchantments) {
                if (args[0].equalsIgnoreCase(enchant.getIdentification())) {
                    if (args.length == 1) {
                        handItem = enchant.addEnchantment(handItem, 1);
                    } else if (Integer.parseInt(args[1]) == 0) {
                        handItem = enchant.removeEnchantment(handItem);
                    } else {
                        handItem = enchant.addEnchantment(handItem, Integer.parseInt(args[1]));
                    }
                }
            }
        }
        return true;
    }
}
