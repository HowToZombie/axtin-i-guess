package org.axtin.modules.warps;

import java.io.File;
import java.io.IOException;

import org.axtin.container.facade.Container;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigHandler {

	private File file;
	private YamlConfiguration config;
	
	public ConfigHandler(String dirName, String fileName) {
		File dir = new File(Container.get(Plugin.class).getDataFolder().getAbsolutePath() + "/" + dirName + "/");
		if(!dir.exists())
			dir.mkdir();
		this.file = new File(dir, fileName + ".yml");
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void reload() {
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	public YamlConfiguration get() {
		return this.config;
	}
	
	public void save() throws IOException {
		this.config.save(this.file);
	}
	
	
}
