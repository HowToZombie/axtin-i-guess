package org.axtin.util;

import org.axtin.container.facade.Container;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class FileStorage {
    private static YamlConfiguration config;
    private static String path;
    private String name;
    private static File f = Container.get(Plugin.class).getDataFolder();

    public FileStorage(String id, String path) {
        this.name = id;
        this.path = path;
        if (configExists()) {
            config = getConfig(id, path);
        } else {
            config = createConfig(id, path);
        }
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public boolean configExists() {
        return new File(f + "/" + path + "/" + name + ".yml").exists();
    }

    public static YamlConfiguration createConfig(String id, String path) {
        File file = new File(f + "/" + path + "/" + id + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static YamlConfiguration getConfig(String id, String path) {
        File file = new File(f + "/" + path + "/" + id + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public boolean saveConfig() {
        File file = new File(f + "/" + path + "/" + name + ".yml");
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            System.out.println("CANNOT SAVE FILE: " + file);
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveConfig(String id, String path) {
        File file = new File(f + "/" + path + "/" + id + ".yml");

        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            System.out.println("CANNOT SAVE FILE: " + file);
            e.printStackTrace();
            return false;
        }
    }

    public void deleteConfig() {
        File file = new File(f + "/" + path + "/" + name + ".yml");
        file.delete();
    }

    public static void deleteConfig(String id, String path) {
        File file = new File(f + "/" + path + "/" + id + ".yml");
        file.delete();
    }
}
