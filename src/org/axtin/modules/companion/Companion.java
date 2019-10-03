package org.axtin.modules.companion;

import javax.annotation.Nullable;

import org.axtin.container.facade.Container;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.WorldServer;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/12/2017
 */
public class Companion {
	
	private final CompanionManager manager;
	private final CompanionStyle style;
	private final Location startLoc;
	
	private ArmorStand as;
	private Zombie zombie;
	private BukkitTask scheduler;
	
	public Companion(CompanionManager manager, @Nullable CompanionStyle style, Location loc){
		this.manager = manager;
		this.style = style;
		this.startLoc = loc;
	}
	
	public boolean isSpawned(){
		return zombie != null;
	}
	
	public boolean spawn(){
		if(isSpawned()) return false;
		
		// spawn armorstand
		as = (ArmorStand) startLoc.getWorld().spawnEntity(startLoc, EntityType.ARMOR_STAND);
		as.setSmall(true);
		as.setGravity(false);
		as.setInvulnerable(true);
		as.setBasePlate(false);
		
		// add armor
		if(style != null){
			if(style.getHelmet() != null) as.setHelmet(style.getHelmet());
			if(style.getChestplate() != null) as.setChestplate(style.getChestplate());
			if(style.getLeggings() != null) as.setLeggings(style.getLeggings());
			if(style.getBoots() != null) as.setBoots(style.getBoots());
		}
		
		// spawn zombie
		final WorldServer world = ((CraftWorld) startLoc.getWorld()).getHandle();
		final EntityZombie nmsZombie = new EntityZombie(world);
		
		// make him silent
		final NBTTagCompound tag = new NBTTagCompound();
		nmsZombie.c(tag);
		tag.setInt("Silent", 1);
		nmsZombie.f(tag);
		
		// set other stuff & spawn
		nmsZombie.setBaby(true);
		nmsZombie.setLocation(startLoc.getX(), startLoc.getY(), startLoc.getZ(), startLoc.getYaw(), startLoc.getPitch());
		world.addEntity(nmsZombie);
		this.zombie = (Zombie) nmsZombie.getBukkitEntity();
		this.zombie.setInvulnerable(true);
		
		// make zombie invisible
		new BukkitRunnable(){
			public void run(){
				nmsZombie.setInvisible(true);
			}
		}.runTaskLater(Container.get(Plugin.class), 4);
		
		// create scheduler
		this.scheduler = new BukkitRunnable(){
			public void run(){
				as.teleport(zombie);
			}
		}.runTaskTimer(Container.get(Plugin.class), 2, 2);
		
		this.manager.getSpawnedCompanions().add(this);
		
		return true;
	}
	
	public boolean remove(){
		if(!isSpawned()) return false;
		
		this.as.remove();
		this.zombie.remove();
		this.scheduler.cancel();
		
		this.as = null;
		this.zombie = null;
		this.scheduler = null;
		
		this.manager.getSpawnedCompanions().remove(this);
		
		return true;
	}
	
	public CompanionStyle getStyle(){
		return this.style;
	}
	
	public Location getLocation(){
		return this.as.getLocation();
	}
	
	public void teleport(Location loc){
		zombie.teleport(loc);
		as.teleport(loc);
	}
	
	public void setVelocity(Vector vec){
		as.setVelocity(vec);
	}
	
	public Vector getVelocity(){
		return as.getVelocity();
	}
}