package org.axtin.modules.ships;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.modules.ships.Destinations;
import org.axtin.modules.ships.ShipListener;
import org.axtin.modules.warps.WarpCommand;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Joseph on 4/14/2017.
 */
public class SpawnCommand extends AxtinCommand {
    public SpawnCommand() {
        super("spawn");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Player player = (Player) commandSender;
        User u = Container.get(UserRepository.class).getUser(player.getUniqueId());

        if (u.getData().getStaffRole().getIdentifier() < 21) {
            player.sendMessage(CommandErrors.NO_PERMISSION.toString());
            return true;
        }

        if(WarpCommand.mining.contains(player.getName())){
            WarpCommand.mining.remove(player.getName());
        }

        if (player.getWorld().getName().equalsIgnoreCase("ShipWorld")) {
            for (Player p : Bukkit.getWorld("ShipWorld").getPlayers()) {
                p.showPlayer(player);
                player.showPlayer(p);
            }
        }

        Location spawn = Destinations.SPAWN.getTP();
        player.teleport(spawn);
        return false;
    }
}
