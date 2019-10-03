package org.axtin.modules.ships;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.modules.ships.Storage;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by zombi on 7/4/2017.
 */
public class StorageCommand extends AxtinCommand {

    public StorageCommand() {
        super("storage");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {

            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou must be a player to use this command!"));
            return true;

        }

        Player p = (Player) commandSender;
        User u = Container.get(UserRepository.class).getUser(p.getUniqueId());

        if (u.getData().getStaffRole().getIdentifier() < 21) {

            super.sendMessage(p, "&cYou do not have permission!");
            return true;

        }

        if (strings.length > 0) {

            for (Player p2 : Bukkit.getOnlinePlayers()) {

                if (p2.getName().toLowerCase().equalsIgnoreCase(strings[0].toLowerCase())) {

                    Storage.openStorage(Container.get(UserRepository.class).getUser(p2.getUniqueId()), p, 1);
                    return true;

                }

            }

            super.sendMessage(p, "&cThat player does not exist!");
            return true;

        }

        Storage.openStorage(u, p, 1);

        return true;

    }
}
