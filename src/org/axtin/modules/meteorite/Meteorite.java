package org.axtin.modules.meteorite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.axtin.container.facade.Container;
import org.axtin.data.BlockData;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 10/6/2017
 */
public class Meteorite {
	
	private final MeteoriteRegion region;
	private final Location dest;
	
	private boolean exists = true;
	private final Giant entity;
	private BukkitTask task;
	private int size = 0;
	private final List<BlockData> blocks = new ArrayList<>();
	private final List<org.bukkit.entity.Item> items = new ArrayList<>();
	
	public Meteorite(MeteoriteRegion region, Location dest){
		this.region = region;
		this.dest = dest;
		
		final Random rand = new Random();
		final Location startLocation = dest.clone().add(rand.nextInt(200)-100, rand.nextInt(200)+20, rand.nextInt(200)-100);
		
		entity = (Giant) dest.getWorld().spawnEntity(startLocation.clone().add(0, -15, 0), EntityType.GIANT);
		entity.setInvulnerable(true);
		entity.setAI(false);
		entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
		entity.getEquipment().setItemInMainHand(new ItemStack(Material.OBSIDIAN));
		
		final double speed = 60;
		final double xAdd = (dest.getX()-startLocation.getX())/speed;
		final double yAdd = (dest.getY()-startLocation.getY())/speed;
		final double zAdd = (dest.getZ()-startLocation.getZ())/speed;
		
		task = new BukkitRunnable(){
			int c = 0;
			
			@SuppressWarnings("deprecation")
			public void run(){
				final Location blockLoc = entity.getLocation().clone().add(0, 11.7, 3.7);
				
				dest.getWorld().playSound(blockLoc, Sound.ITEM_ARMOR_EQUIP_ELYTRA, 5, 1);
				
				entity.teleport(entity.getLocation().add(xAdd, yAdd, zAdd));
				dest.getWorld().playEffect(blockLoc, Effect.FLAME, 500);
				
				if(c == speed){
					dest.getWorld().playSound(blockLoc, Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 10, 10);
					
					for(int i=0; i<5; i++)
						dest.getWorld().playEffect(blockLoc.add(rand.nextInt(6)-3, rand.nextInt(2), rand.nextInt(6)-3), Effect.EXPLOSION_HUGE, 500);
					
					entity.remove();
					cancel();
					
					// change blocks
					size = new Random().nextInt(6) + 2;
					
					for(int iy=-(size*2); iy<size; iy++){
						for(int ix=-size; ix<size; ix++){
							for(int iz=-size; iz<size; iz++){
								final Location loc = dest.clone().add(ix, iy, iz);
								final Block block = loc.getBlock();
								
								if(loc.distance(dest) <= size && block.getType() != Material.AIR){
									final BlockData data = new BlockData(block);
									data.read();
									
									blocks.add(data);
								
									final int rand = new Random().nextInt(100);
									Material mat = Material.AIR;
									
									if(iy <= -(size/1.5)){
										if(rand <= 30)
											mat = Material.OBSIDIAN;
										else if(rand <= 60)
											mat = Material.BEDROCK;
										else if(rand <= 80)
											mat = Material.NETHERRACK;
									}
									
									block.setType(mat);
								}
							}
						}
					}
					
					// set block under items
					final Block block = dest.clone().add(0, -1, 0).getBlock();
					final BlockData data = new BlockData(block);
					
					data.read();
					blocks.add(data);
					
					block.setType(Material.WOOD_STEP);
					
					// drop items
					for(Item i:Container.get(MeteoriteManager.class).getItems()){
						if(i.gamble())
							items.add(i.drop(dest));
					}
					
					// change back to normal after 5 minutes (When the item despawns)
					task = new BukkitRunnable(){
						public void run(){
							remove();
						}
					}.runTaskLater(Container.get(Plugin.class), 20*60*5);
				}
				
				c++;
			}
		}.runTaskTimer(Container.get(Plugin.class), 3, 3);
	}
	
	public void remove(){
		if(exists){
			exists = false;
			
			entity.remove();
			task.cancel();
			
			for(BlockData block:blocks)
				block.write();
			blocks.clear();
			
			for(org.bukkit.entity.Item item:items)
				item.remove();
			items.clear();
			
			this.region.getMeteorites().remove(this);
		}
	}
	
	public void removeSlowly(){
		if(exists){
			task.cancel();
			
			task = new BukkitRunnable(){
				int cY = 0;
				
				public void run(){
					if(blocks.size() >= 1){
						final BlockData data = blocks.get(0);
						blocks.remove(data);
						data.write();
						
						if(data.getBlock().getLocation().getBlockY() != cY){
							final Location loc = dest.getBlock().getLocation().clone();
							loc.setY(data.getBlock().getLocation().getBlockY());
							
							for(Entity e:dest.getWorld().getNearbyEntities(loc, size, 1, size))
								e.teleport(e.getLocation().clone().add(0, 1, 0));
							
							cY = loc.getBlockY();
						}
						
						data.getBlock().getLocation().getWorld().playSound(data.getBlock().getLocation(), Sound.BLOCK_STONE_PLACE, 2, 1);
					}else
						remove();
				}
			}.runTaskTimer(Container.get(Plugin.class), 1, 1);
		}
	}
	
	public MeteoriteRegion getRegion(){
		return this.region;
	}
	
	public Location getDestination(){
		return this.dest;
	}
	
	public List<org.bukkit.entity.Item> getDroppedItems(){
		return this.items;
	}
	
	public static @Nullable Meteorite spawn(Location dest){
		return new Meteorite(null, dest);
	}
}
