package org.axtin.modules.managing.moderation;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Joseph on 4/16/2017.
 */
public class InventorySeeCommand extends AxtinCommand {
    public InventorySeeCommand() {
        super("invsee");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        Player p = (Player) sender;
        User user = Container.get(UserRepository.class).getUser(p.getUniqueId());
        if(user.getData().getStaffRole().getIdentifier() > 80){
            if(strings.length !=1){
                return false;
            }else{
                String name = strings[0];
                Player target = Bukkit.getPlayer(name);
                p.openInventory(target.getInventory());
                return true;
            }
        }else{
            p.sendMessage(CommandErrors.NO_PERMISSION.toString());
            return false;
        }
    }
}
