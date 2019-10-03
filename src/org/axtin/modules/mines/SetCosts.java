package org.axtin.modules.mines;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.user.UserRepository;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Jo on 11/10/2015.
 */
public class SetCosts extends AxtinCommand {
    public SetCosts() {
        super("set");
    }

    public static HashMap<String,String> temp = new HashMap<String,String>();


    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        String name = strings[0];
        Player p = (Player) commandSender;

        if (Container.get(UserRepository.class).getUser(p.getUniqueId()).getData().getStaffRole().getIdentifier() < 21) {
            p.sendMessage(CommandErrors.NO_PERMISSION.toString());
            return true;
        }

        Location loc = p.getLocation();

        RankConfig config = new RankConfig(name);
        config.getConfig().set("Name", name);

        config.getConfig().set("Region.World", p.getWorld().getName());

        config.getConfig().set("Region.First.X", 0);
        config.getConfig().set("Region.First.Y", 0);
        config.getConfig().set("Region.First.Z", 0);

        config.getConfig().set("Region.Second.X", 0);
        config.getConfig().set("Region.Second.Y", 0);
        config.getConfig().set("Region.Second.Z", 0);


        p.sendMessage(ChatColor.GREEN + "Please click the first block");

        temp.put(p.getName(), name);

        config.getConfig().set("Region.BreakBlock", false);

        config.getConfig().set("Region.PvP" , false);


        RankConfig.saveConfig(name);

        /*
        /set A 10000
         */
        return false;
    }
}
