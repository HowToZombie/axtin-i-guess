package org.axtin.util;

import java.util.HashMap;
import java.util.Map;

import org.axtin.container.facade.Container;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_12_R1.PacketPlayOutBed;
import net.minecraft.server.v1_12_R1.PacketPlayOutGameStateChange;

/**
 * 
 * @author Marcel
 * Broken
 */
public class SkyColorHandler {
	
	private static final Map<World, SkyColorHandler> worlds = new HashMap<World, SkyColorHandler>();
	
	private final World world;
	
	@Deprecated
	/**
	 * 
	 * @deprecated Just do not!
	 */
	public SkyColorHandler(World world){
		this.world = world;
	}
	
	public void setRawColor(float id, float fade){
		
	}
	
	public void setColor(SkyColor color, float fade){
		setRawColor(color.getID(), fade);
	}
	
	public static SkyColorHandler SkyColorHandler(World world){
		if(!worlds.containsKey(world)){
			final SkyColorHandler handler = new SkyColorHandler(world);
			
			worlds.put(world, handler);
			return handler;
		}else
			return worlds.get(world);
	}
	
	public static enum SkyColor {
		DEFAULT_DAY(0);
		
		
		
		private final int id;
		
		private SkyColor(int id){
			this.id = id;
		}
		
		public int getID(){
			return this.id;
		}
	}
	
	public static class SkyColorWorldsHandler implements Listener {
		
		
		@EventHandler
		public void onPlayerTeleportEvent(PlayerTeleportEvent event){
			sendPacket(event.getPlayer(), 0, 20*2);
			
			final World world = event.getTo().getWorld();
			final World fromWorld = event.getFrom().getWorld();
			
			if(!world.equals(fromWorld)){
				if(worlds.containsKey(world)){
					
					
				}else if(worlds.containsKey(fromWorld)){
					
				}
			}
		}
		
		private void sendPacket(Player player, float id, float fade){
			final EntityPlayer ep = ((CraftPlayer) player).getHandle();
			/*final PacketPlayOutGameStateChange packet1 = new PacketPlayOutGameStateChange(7, id);
			final PacketPlayOutGameStateChange packet2 = new PacketPlayOutGameStateChange(8, fade);
			
			// ep.playerConnection.sendPacket(packet2);
			ep.playerConnection.sendPacket(packet1);
			
			new BukkitRunnable(){
				public void run(){
					// client automaticly starts rain?
					final PacketPlayOutGameStateChange packet3 = new PacketPlayOutGameStateChange(1, 0);
					
					ep.playerConnection.sendPacket(packet3);
				}
			}.runTaskLater(Container.get(Plugin.class), 20);*/
			final PacketPlayOutBed packet = new PacketPlayOutBed(ep, new BlockPosition(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getZ()));
		
			ep.playerConnection.sendPacket(packet);
		}
	}
}
