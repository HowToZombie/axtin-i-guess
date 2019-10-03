package org.axtin.modules.oxygen;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.modules.oxygen.Helmet;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by Joseph on 4/15/2017.
 */
public class HelmetCommand extends AxtinCommand {
    public HelmetCommand() {
        super("helmet");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof Player) {
            User u = Container.get(UserRepository.class).getUser(((Player) commandSender).getUniqueId());
            if (u.getData().getStaffRole().getIdentifier() < 21) {
                commandSender.sendMessage(CommandErrors.NO_PERMISSION.toString());
                return true;
            }
        }
        if(strings.length >0){
            if(strings.length <2){
                Player p = Bukkit.getPlayer(strings[0]);
                Inventory pinv = p.getInventory();
                if(!pinv.contains(Helmet.BASIC.getItem())){
                    pinv.addItem(Helmet.BASIC.getItem());
                }
            }else if(strings.length ==3){
                Player p = Bukkit.getPlayer(strings[0]);
                Inventory pinv = p.getInventory();
                String level = strings[1];
                String oxygen = strings[2];
                if(!pinv.contains(Helmet.getHelmet(Integer.valueOf(level)).getItem(Integer.parseInt(oxygen)))){
                    pinv.addItem(Helmet.getHelmet(Integer.valueOf(level)).getItem(Integer.parseInt(oxygen)));
                }
            }else{
                Player p = Bukkit.getPlayer(strings[0]);
                Inventory pinv = p.getInventory();
                String level = strings[1];
                if(!pinv.contains(Helmet.getHelmet(Integer.valueOf(level)).getItem())){
                    pinv.addItem(Helmet.getHelmet(Integer.valueOf(level)).getItem());
                }
            }

        }
        return false;
    }
}
