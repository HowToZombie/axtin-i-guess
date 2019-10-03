package org.axtin.modules.shulkercrates;

import java.util.HashMap;
import java.util.UUID;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ShulkerHandler{
	
	public HashMap<UUID, ShulkerInventoryHolder> shulkers;

	public HashMap<FallingBlock, Player> fallingShulkers;

	public ShulkerDataHandler dataHandler = new ShulkerDataHandler();
	public HashMap<ShulkerBox, Player> shulkBoxes = new HashMap<ShulkerBox, Player>();
	public HashMap<ShulkerBox, Integer> whichBox = new HashMap<ShulkerBox, Integer>();
	
	private double[][] coordinates;
	
	
	public ShulkerHandler() {
		shulkers = new HashMap<>();
		fallingShulkers = new HashMap<>();
		coordinates = new double[11 + 10 * 2][];
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!shulkers.containsKey(p)) {
				ShulkerInventoryHolder inv = dataHandler.get(p);
				if(inv == null) 
					inv = new ShulkerInventoryHolder(p.getUniqueId());
				shulkers.put(inv.getUUID(), inv);

			}
		}
	}
	
	public double[][] getCoordinates() {
		return coordinates;
	}
	
	public void remove(ShulkerBox sb) {
		if(shulkBoxes.containsKey(sb))
			shulkBoxes.remove(sb);
	}
	
	public void shutDown() {
		if(!Container.get(Plugin.class).getDataFolder().exists())
			Container.get(Plugin.class).getDataFolder().mkdirs();
                shulkers.entrySet().forEach((set) -> {
                    dataHandler.save(set.getValue());
            });
	}
	

}
