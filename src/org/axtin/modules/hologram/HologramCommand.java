package org.axtin.modules.hologram;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.user.User;
import org.axtin.user.UserRepository;
import org.axtin.util.FileStorage;
import org.axtin.util.Hologram;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 4/8/2017.
 */
public class HologramCommand extends AxtinCommand{
    public HologramCommand() {
        super("hologram");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        Player p = (Player) commandSender;
        User user = Container.get(UserRepository.class).getUser(p.getUniqueId());

        if(user.getData().getStaffRole().getIdentifier() > 20){
            if(strings.length > 1){
                String name = strings[0];
                if(name.equalsIgnoreCase("delete")){
                    if(Hologram.holograms.containsKey(strings[1])){
                        Hologram.holograms.get(strings[1]).deleteHologram();
                        Hologram.holograms.remove(strings[1]);
                    }
                }else{
                    String[] split = strings[1].split(":");
                    List<String> contents = new ArrayList<>();
                    for(String s2: split){
                        contents.add(s2);
                    }
                    if(createHolo(name,contents,p.getLocation())){
                        p.sendMessage("Hologram created");
                    }else{
                        p.sendMessage("Hologram already exists");
                    }

                }

            }
        }else{
            p.sendMessage(CommandErrors.NO_PERMISSION.toString());
        }

        return false;
    }

    private boolean createHolo(String name, List<String> text, Location l){
        if(!Hologram.holograms.containsKey(name)){
            FileStorage fs = new FileStorage(name.toLowerCase(),"holos");
            YamlConfiguration config = fs.getConfig();
            config.set("Location.world", l.getWorld().getName());
            config.set("Location.x", l.getX());
            config.set("Location.y", l.getY());
            config.set("Location.z", l.getZ());
            config.set("content", text);
            new Hologram(l, text,name);
            fs.saveConfig();

            return true;
        }else{
            return false;
        }
    }
}
