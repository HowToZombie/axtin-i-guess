package org.axtin.modules.shulkercrates;

import java.util.ArrayList;
import java.util.List;
import org.axtin.container.facade.Container;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


public class ShulkerPlaceEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private Block block;
	private Player player;
	private BukkitRunnable remove;
	private ShulkerInventoryHolder sih;
	private int which;
	

	public ShulkerPlaceEvent(Block b, Player p, int box) {
		player = p;
		block = b;
		which = box;
		sih = Container.get(ShulkerHandler.class).shulkers.get(player.getUniqueId());
		remove = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(sih.hasOpened) {
					this.cancel();
					return;
				}
				
				if(shulkerTypes().contains(b.getType())) {
					Container.get(ShulkerHandler.class).remove(getShulkerBox());
					ParticleColor color = ParticleColor.getAppropiateColor(b);
					b.setType(Material.AIR);
					color.playEffect(b.getLocation());
				}
				
			}
		
		};
		remove.runTaskLater(Container.get(Plugin.class), 400);
		
	}
	public Player getWhoItBelongsTo() {
		return player;
	}
	
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public ShulkerBox getShulkerBox() {
		return (ShulkerBox) block.getState();
	}
	
	private List<Material> shulkerTypes() {
		List<Material> list = new ArrayList<>();
		
		for(Material mat : Material.values()) {
			if(mat.toString().contains("SHULKER_BOX"))
				list.add(mat);
		}
		
		return list;


	}
	public int getWhich() {
		// TODO Auto-generated method stub
		return which;
	}


}
