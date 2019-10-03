package org.axtin.modules.misccommands;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TopCommand extends AxtinCommand {
    public TopCommand() {
        super("top");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Player player = this.search(sender.getName());

        User user = Container.get(UserRepository.class).getUser(player.getUniqueId());

        if (user.getData().getStaffRole().getIdentifier() < 21) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        int highest = 0;
        int current = 0;

        while (current < player.getLocation().getWorld().getMaxHeight()) {
            int lastCurrent = current;
            current++;
            Location loc = new Location(player.getLocation().getWorld(), player.getLocation().getX(), lastCurrent, player.getLocation().getZ());

            if (loc.getBlock() != null && loc.getBlock().getType() != null && !loc.getBlock().getType().equals(Material.AIR)) {
                highest = lastCurrent;
            }
        }

        Location loc2 = new Location(player.getLocation().getWorld(), player.getLocation().getX(), highest, player.getLocation().getZ());
        loc2.setPitch(player.getLocation().getPitch());
        loc2.setYaw(player.getLocation().getYaw());

        if (loc2.getY() == 0) {
            player.sendMessage("No location found.");
            return true;
        }

        loc2.add(0, 1.01, 0);
        // TODO: Use LocationUtil once added back
        player.teleport(loc2, PlayerTeleportEvent.TeleportCause.COMMAND);
        player.sendMessage("Teleporting to the top...");

        return true;
    }
}
