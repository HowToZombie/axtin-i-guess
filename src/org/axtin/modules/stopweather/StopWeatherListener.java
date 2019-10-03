package org.axtin.modules.stopweather;

import org.axtin.Axtin;
import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.Plugin;

public class StopWeatherListener implements Listener {

	@EventHandler
	public void detectWeather(WeatherChangeEvent e) {
		if(e.toWeatherState()) {
            Container.get(Plugin.class).getServer().getScheduler().scheduleSyncDelayedTask(Container.get(Plugin.class),new Runnable() {
                public void run() {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "weather clear");
                }
            }, 10L);

		}
		
	}
	
}