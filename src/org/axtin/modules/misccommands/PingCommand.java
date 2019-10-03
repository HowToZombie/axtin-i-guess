package org.axtin.modules.misccommands;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.user.UserRepository;
import org.axtin.util.UserUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand extends AxtinCommand {
    public PingCommand() {
        super("ping");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Player player = this.search(sender.getName());
        if (Container.get(UserRepository.class).getUser(player.getUniqueId()).getData().getStaffRole().getIdentifier() < 21) {
            player.sendMessage(CommandErrors.NO_PERMISSION.toString());
            return true;
        }
        player.sendMessage(ChatColor.GRAY + "Ping: " + ChatColor.GREEN + UserUtil.getPing(player));

        return true;
    }
}
