package org.axtin.modules.hologram;

import org.axtin.container.facade.Container;
import org.axtin.util.FileStorage;
import org.axtin.util.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

/**
 * Created by Joseph on 4/8/2017.
 */
public class HologramLoader {

    public void load() {
        File dir = new File(Container.get(Plugin.class).getDataFolder() + "/holos/");
        if(!dir.exists())
        	dir.mkdirs();
        for(File file : dir.listFiles()) {
            FileStorage fs = new FileStorage(file.getName().replace(".yml", ""),"holos");
            YamlConfiguration config = fs.getConfig();
            Location to = new Location(Bukkit.getWorld(config.getString("Location.world")),config.getDouble("Location.x"),config.getDouble("Location.y"),config.getDouble("Location.z"));
            List<String> content = config.getStringList("content");
            new Hologram(to, content, file.getName().replace(".yml", ""));
        }
    }
}
