package org.axtin.modules.messaging;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SocialSpyCommand extends AxtinCommand {

    public SocialSpyCommand() {
        super("socialspy", "let you be a perv and see other messages", "/socialspy", "/ss");
    }

    public static HashMap<Player, Boolean> socialspy = new HashMap<>();

    @Override
    public boolean execute(CommandSender sender, String string, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = Container.get(UserRepository.class).getUser(p.getUniqueId());
            if (u.getData().getStaffRole().getIdentifier() < 3) {
                sendMessage(p, CommandErrors.NO_PERMISSION.toString());
                return true;
            }

            if (!socialspy.containsKey(p)) {
                socialspy.put(p, true);
                sendMessage(p, "&6Enabled SocialSpy!");
                return true;
            }

            if (socialspy.get(p)) {
                socialspy.put(p, false);
                sendMessage(p, "&6Disabled SocialSpy!");
                return true;
            } else {
                socialspy.put(p, true);
                sendMessage(p, "&6Enabled SocialSpy!");
                return true;
            }
        }
        return true;
    }
}
