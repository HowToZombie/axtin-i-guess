package org.axtin.modules.oxygen;

import org.axtin.command.AxtinCommand;
import org.axtin.command.CommandErrors;
import org.axtin.container.facade.Container;
import org.axtin.modules.oxygen.Airlock;
import org.axtin.modules.oxygen.LoadAirlocks;
import org.axtin.user.UserRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by zombi on 6/2/2017.
 */
public class AirLockCommand extends AxtinCommand {
    public AirLockCommand() {
        super("airlock");
    }

    public static HashMap<String,String> temp = new HashMap<String,String>();

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;

            if (Container.get(UserRepository.class).getUser(p.getUniqueId()).getData().getStaffRole().getIdentifier() < 21) {
                p.sendMessage(CommandErrors.NO_PERMISSION.toString());
                return true;
            }

            if (strings.length == 8) {
                if (strings[0].equalsIgnoreCase("create")) {
                    File f = new File(Container.get(Plugin.class).getDataFolder() + "/airlocks/" + strings[1] + ".yml");
                    YamlConfiguration config;
                    config = YamlConfiguration.loadConfiguration(f);
                    config.set("Name", strings[1]);

                    config.set("Region.World", p.getWorld().getName());

                    config.set("Region.Maximum.X", Double.parseDouble(strings[2]));
                    config.set("Region.Maximum.Y", Double.parseDouble(strings[3]));
                    config.set("Region.Maximum.Z", Double.parseDouble(strings[4]));
                    config.set("Region.Minimum.X", Double.parseDouble(strings[5]));
                    config.set("Region.Minimum.Y", Double.parseDouble(strings[6]));
                    config.set("Region.Minimum.Z", Double.parseDouble(strings[7]));

                    super.sendMessage(p, "&2Created airlock " + strings[1] + "!");

                    try {
                        config.save(f);
                    } catch(IOException e) {
                        System.out.println("CANNOT SAVE FILE: " + f);
                        e.printStackTrace();
                        return false;
                    }
                    new LoadAirlocks().loadAreas();
                }

                if (strings[0].equalsIgnoreCase("edit")) {
                    File f = new File(Container.get(Plugin.class).getDataFolder() + "/airlocks/" + strings[1] + ".yml");
                    if (f.exists()) {
                        YamlConfiguration config;
                        config = YamlConfiguration.loadConfiguration(f);
                        config.set("Name", strings[1]);

                        config.set("Region.World", p.getWorld().getName());

                        config.set("Region.Maximum.X", Double.parseDouble(strings[2]));
                        config.set("Region.Maximum.Y", Double.parseDouble(strings[3]));
                        config.set("Region.Maximum.Z", Double.parseDouble(strings[4]));
                        config.set("Region.Minimum.X", Double.parseDouble(strings[5]));
                        config.set("Region.Minimum.Y", Double.parseDouble(strings[6]));
                        config.set("Region.Minimum.Z", Double.parseDouble(strings[7]));

                        super.sendMessage(p, "&2Created airlock " + strings[1] + "!");

                        try {
                            config.save(f);
                        } catch(IOException e) {
                            System.out.println("CANNOT SAVE FILE: " + f);
                            e.printStackTrace();
                            return false;
                        }
                        new LoadAirlocks().loadAreas();
                    } else {
                        super.sendMessage(p, "&cAirlock does not exist!");
                    }
                }
            }

            if (strings.length == 2) {
                if (strings[0].equalsIgnoreCase("delete")) {
                    File f = new File(Container.get(Plugin.class).getDataFolder() + "/airlocks/" + strings[1] + ".yml");
                    if (f.exists()) {
                        f.delete();
                        Airlock.areas.remove(strings[2]);
                    } else {
                        super.sendMessage(p, "&cInvalid airlock!");
                    }
                }

                if (strings[0].equalsIgnoreCase("create")) {
                    File f = new File(Container.get(Plugin.class).getDataFolder() + "/airlocks/" + strings[1] + ".yml");
                    YamlConfiguration config;
                    config = YamlConfiguration.loadConfiguration(f);
                    config.set("Name", strings[1]);

                    config.set("Region.World", p.getWorld().getName());

                    config.set("Region.Maximum.X", 0);
                    config.set("Region.Maximum.Y", 0);
                    config.set("Region.Maximum.Z", 0);
                    config.set("Region.Minimum.X", 0);
                    config.set("Region.Minimum.Y", 0);
                    config.set("Region.Minimum.Z", 0);

                    try {
                        config.save(f);
                    } catch (IOException e) {
                        System.out.println("CANNOT SAVE FILE: " + f);
                        e.printStackTrace();
                        return false;
                    }
                    p.sendMessage("Please click the first block");

                    temp.put(p.getName(), strings[1]);
                }

                if (strings[0].equalsIgnoreCase("edit")) {
                    File f = new File(Container.get(Plugin.class).getDataFolder() + "/airlocks/" + strings[1] + ".yml");
                    if (f.exists()) {
                        YamlConfiguration config;
                        config = YamlConfiguration.loadConfiguration(f);
                        config.set("Name", strings[1]);

                        config.set("Region.World", p.getWorld().getName());

                        config.set("Region.Maximum.X", 0);
                        config.set("Region.Maximum.Y", 0);
                        config.set("Region.Maximum.Z", 0);
                        config.set("Region.Minimum.X", 0);
                        config.set("Region.Minimum.Y", 0);
                        config.set("Region.Minimum.Z", 0);

                        try {
                            config.save(f);
                        } catch (IOException e) {
                            System.out.println("CANNOT SAVE FILE: " + f);
                            e.printStackTrace();
                            return false;
                        }
                        p.sendMessage("Please click the first block");

                        temp.put(p.getName(), strings[1]);
                    } else {
                        super.sendMessage(p, "&cAirlock does not exist!");
                    }
                }
            }
        }
        return true;
    }
}
