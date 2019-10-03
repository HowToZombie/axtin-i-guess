package org.axtin.modules.misccommands;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand extends AxtinCommand {
    public SpeedCommand() {
        super("speed");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Player player = this.search(sender.getName());

        User user = Container.get(UserRepository.class).getUser(player.getUniqueId());

        if (user.getData().getStaffRole().getIdentifier() < 21) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        if (!this.getArguments(args, 0)) {
            player.sendMessage(ChatColor.RED + "/speed [player] [speed]");
            return true;
        }

        Float speed = Float.parseFloat(args[0]);

        if (speed > 10 || speed < 0) {
            player.sendMessage(ChatColor.RED + "Speed must be between 1 and 10.");
            return true;
        }

        if (!this.getArguments(args, 1)) {
            player.setFlySpeed(calculate(speed, true));
            player.setWalkSpeed(calculate(speed, false));
            player.sendMessage(ChatColor.GREEN + "You have changed your speed to " + speed);
        } else {
            Player target = this.search(args[1]);

            if (target == null) {
                player.sendMessage(ChatColor.RED + "User was not found.");
                return true;
            }

            target.setFlySpeed(calculate(speed, true));
            target.setWalkSpeed(calculate(speed, false));
            player.sendMessage("You have changed " + target.getName() + " speed to " + speed);
            target.sendMessage("Your speed has been changed to " + speed);
        }

        return true;
    }

    private float calculate(float speed, boolean fly) {
        if (speed > 10.0F) {
            speed = 10.0F;
        } else if (speed < 1.0E-004F) {
            speed = 1.0E-004F;
        }

        float def = fly ? 0.1F : 0.2F;
        float max = 1.0F;

        if (speed < 1.0F) {
            return def * speed;
        }

        float rat = (speed - 1.0F) / 9.0F * (max - def);

        return rat + def;
    }
}
