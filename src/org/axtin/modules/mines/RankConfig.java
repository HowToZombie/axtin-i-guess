package org.axtin.modules.mines;

import org.axtin.container.facade.Container;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jo on 11/10/2015.
 */
public class RankConfig {
    private static YamlConfiguration config;

    public RankConfig(String id) {
        if(configExists(id)) {
            config = getConfig(id);
        } else {
            config = createConfig(id);
        }
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public boolean configExists(String id) {
        return new File(Container.get(Plugin.class).getDataFolder() + "/ranks/" + id + ".yml").exists();
    }

    public static YamlConfiguration createConfig(String id) {
        File file = new File(Container.get(Plugin.class).getDataFolder() + "/ranks/" + id + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static YamlConfiguration getConfig(String id) {
        File file = new File(Container.get(Plugin.class).getDataFolder() + "/ranks/" + id + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static boolean saveConfig(String id) {
        File file = new File(Container.get(Plugin.class).getDataFolder() + "/ranks/" + id + ".yml");
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            System.out.println("CANNOT SAVE FILE: " + file);
            e.printStackTrace();
            return false;
        }
    }
}
