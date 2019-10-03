package org.axtin.modules.oxygen;

import org.axtin.container.facade.Container;
import org.axtin.modules.mines.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * Created by zombi on 6/13/2017.
 */
public class LoadAirlocks {
    public void loadAreas() {
        File dir = new File(Container.get(Plugin.class).getDataFolder() + "/airlocks/");

        if(!dir.exists())
            dir.mkdir();

        for (File f : dir.listFiles()) {
            YamlConfiguration config;
            config = YamlConfiguration.loadConfiguration(f);

            String name = config.getString("Name");
            Location maximum = new Location(Bukkit.getWorld(config.getString("Region.World")), config.getDouble("Region.Maximum.X"), config.getDouble("Region.Maximum.Y"), config.getDouble("Region.Maximum.Z"));
            Location minimum = new Location(Bukkit.getWorld(config.getString("Region.World")), config.getDouble("Region.Minimum.X"), config.getDouble("Region.Minimum.Y"), config.getDouble("Region.Minimum.Z"));

            new Airlock(name, maximum, minimum);
        }
    }
}
