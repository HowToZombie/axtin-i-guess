package org.axtin.util.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigSection {

	private final String fullPath;
	private Map<String, ConfigValue> values;
	
	
	public ConfigSection(ConfigurationSection section) {
		Map<String, Object> values = section.getValues(false);
		this.values = new HashMap<>();
		this.fullPath = section.getCurrentPath();
		for(Entry<String, Object> e : values.entrySet()) {
			if(section.isConfigurationSection(e.getKey())) {
				ConfigValue value = new ConfigValue(e.getKey(), new ConfigSection((ConfigurationSection) e.getValue()));
				this.values.put(e.getKey(), value);
			} else {
				this.values.put(e.getKey(), new ConfigValue(e.getKey(), e.getValue()));
			}
		}
		
	}
	
	public Map<String, ConfigValue> getValues() {
		return this.values;
	}
	
	public String getFullPath() {
		return this.fullPath;
	}
	
}
