package org.axtin.modules.economy;

import org.axtin.command.AxtinCommand;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.axtin.user.role.PrisonRole;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Joseph on 4/13/2017.
 */
public class RankUpCommand extends AxtinCommand {
    public RankUpCommand() {
        super("rankup");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Player p = (Player) commandSender;
        User user = Container.get(UserRepository.class).getUser(p.getUniqueId());
        double balance = user.getData().getBalance();
        if(balance >= user.getData().getPrisonRole().getRankup()){
            user.getData().setBalance(user.getData().getBalance()-user.getData().getPrisonRole().getRankup());
            user.getData().setPrisonRole(PrisonRole.getRole(user.getData().getPrisonRole().getIdentifier() +1));
            p.sendMessage("You have ranked up to rank " + user.getData().getPrisonRole().getName());
            if (user.getData().getPrisonRole().equals(PrisonRole.A)) {
                user.getData().setMining(PrisonRole.getRole("a"));
            }
        }else{
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            double more = user.getData().getPrisonRole().getRankup() - balance;
            p.sendMessage("You need $" + df.format(more) + " to rank up.");
        }
        return false;
    }
}
