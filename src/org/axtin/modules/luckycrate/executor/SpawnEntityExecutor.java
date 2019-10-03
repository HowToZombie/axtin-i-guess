package org.axtin.modules.luckycrate.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.axtin.modules.luckycrate.Prize;
import org.axtin.util.EntityUtil;
import org.axtin.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * 
 * @author Marcel (MrEAlderson)
 * @date 11/9/2017
 */
public class SpawnEntityExecutor extends PrizeExecutor {
	
	private List<Entity> spawnedEntities = new ArrayList<>();
	
	private EntityType input_type;
	private double input_x = 0, input_y = 0, input_z = 0;
	private float input_yaw = 0, input_pitch = 0, input_velo_x = 0, input_velo_y = 0, input_velo_z = 0;
	private String input_name = "";
	private boolean input_velo_ranomMultiplier = false, input_glowing = false, input_gravity = true;
	private Double input_moveSpeed = null;
	private ItemStack input_item_material = new ItemStack(Material.STONE);
	
	public SpawnEntityExecutor(Prize prize, Map<String, Object> input){
		super(prize, input);
		
		for(Entry<String, Object> e:input.entrySet()){
			switch(e.getKey()){
			case "type":
				this.input_type = (EntityType) e.getValue();
				break;
				
			case "x":
				this.input_x = (double) e.getValue();
				break;
			
			case "y":
				this.input_y = (double) e.getValue();
				break;
			
			case "z":
				this.input_z = (double) e.getValue();
				break;
			
			case "yaw":
				this.input_yaw = (float) e.getValue();
				break;
			
			case "pitch":
				this.input_pitch = (float) e.getValue();
				break;
			
			case "item-material":
				this.input_item_material = (ItemStack) e.getValue();
				break;
				
			case "velo-x":
				this.input_velo_x = (float) e.getValue();
				break;
				
			case "velo-y":
				this.input_velo_y = (float) e.getValue();
				break;
				
			case "velo-z":
				this.input_velo_z = (float) e.getValue();
				break;
				
			case "velo-randommultiplier":
				this.input_velo_ranomMultiplier = (boolean) e.getValue();
				break;
				
			case "name":
				this.input_name = (String) e.getValue();
				break;
				
			case "glowing":
				this.input_glowing = (boolean) e.getValue();
				break;
				
			case "movespeed":
				this.input_moveSpeed = (double) e.getValue();
				break;
				
			case "gravity":
				this.input_gravity = (boolean) e.getValue();
				break;
			}
		}
	}

	@Override
	public void execute(Player player, Location loc){
		loc = loc.clone();
		
		// set yaw/pitch
		loc.setYaw(input_yaw);
		loc.setPitch(input_pitch);
		
		// spawn entity/item
		Entity entity = null;
		
		if(input_type == EntityType.DROPPED_ITEM)
			entity = loc.getWorld().dropItem(loc, input_item_material);
		else
			entity = loc.getWorld().spawnEntity(loc.add(input_x, input_y, input_z), input_type);
		
		// set velocity
		final float veloX = input_velo_x * (input_velo_ranomMultiplier ? (Util.RAND.nextFloat()*2)-1 : 1);
		final float veloY = input_velo_y * (input_velo_ranomMultiplier ? (Util.RAND.nextFloat()*2)-1 : 1);
		final float veloZ = input_velo_z * (input_velo_ranomMultiplier ? (Util.RAND.nextFloat()*2)-1 : 1);
		
		entity.setVelocity(new Vector(veloX, veloY, veloZ));
		
		// set glowing
		entity.setGlowing(input_glowing);
		
		// set gravity
		entity.setGravity(input_gravity);
		
		// set name
		if(!input_name.equals("")){
			entity.setCustomName(input_name);
			entity.setCustomNameVisible(true);
		}
		
		// set move speed
		if(input_moveSpeed != null)
			EntityUtil.setSpeed(entity, input_moveSpeed);
		
		spawnedEntities.add(entity);
	}
	
	@Override
	public void reset(){
		for(Entity e:spawnedEntities)
			e.remove();
		
		spawnedEntities.clear();
	}
}