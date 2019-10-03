package org.axtin.deprecated.modules.companions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.EnumMoveType;

public class CustomArmorStand extends EntityArmorStand {

	private Companion companion;
	private double speed;
	
	public CustomArmorStand(org.bukkit.World world, Companion companion) {
		super(((CraftWorld)world).getHandle());
		this.setNoGravity(true);
		this.noclip = false;
		this.companion = companion;
		this.speed = 0.0d;
	}
	
	@Override
	public float g(float f, float f1) {
		
		if(!isNoGravity()) {
			return super.g(f, f1);
		} else {
			move(EnumMoveType.SELF, motX, motY, motZ);
			return 0.0f;
		}
		
	}
	
	@Override
	public void n() { // This method is called each tick. This also slowly multiplies each velocity by 0.98, so I just reset those values.
		
		if(companion.ownerOnline) {
			Location location = new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
			double dist = location.distance(companion.ownerPlayer.getEyeLocation());
			if(dist > 3 /*&& dist < 100*/) {
				this.speed = dist * 0.03;
				if(speed > 0.3)
					companion.teleport(companion.ownerPlayer.getEyeLocation());
				else
					companion.moveTowardPlayer(this.speed);
				Bukkit.broadcastMessage(String.valueOf(this.speed));
			} else {
				if(companion.moving)
					companion.stop();
				else {
					
					if(this.ticksLived % 20 == 0) {
						Vector vec = new Vector(0.0 , 0.0115 , 0.0);
						companion.setVelocity(vec);
					}
					
					if(this.ticksLived % 40 == 0) {
						Vector vec = new Vector(0.0 , -0.0115 , 0.0);
						companion.setVelocity(vec);
					}
					
					
				}
			}
			this.setYawPitch(yaw + 1.5f, 0.0f);

		}
		
	    if (!isNoGravity()) {
	        double motX = this.motX, motY = this.motY, motZ = this.motZ;
	        super.n();
	        this.motX = motX;
	        this.motY = motY;
	        this.motZ = motZ;
	    } else super.n();
	    
	}
	
	public void setHeadPos(ArmorStand as, double yaw, double pitch){
		double xint = Math.cos(yaw/Math.PI);
		double zint = Math.sin(yaw/Math.PI);
		//This will convert the yaw to a xint and zint between -1 and 1. Here are some examples of how the yaw changes:
		/*
		yaw = 0 : xint = 1. zint = 0;  East
		yaw = 90 : xint = 0. zint = 1; South
		yaw = 180: xint = -1. zint = 0; North
		yaw = 270 : xint = 0. zint = -1; West
		*/
		double yint = Math.sin(pitch/Math.PI);
		//This converts the pitch to a yint
		EulerAngle ea = as.getHeadPose();
		ea.setX(xint);
		ea.setY(yint);
		ea.setZ(zint);
		as.setHeadPose(ea);
		//This gets the EulerAngle of the armorStand, sets the values, and then updates the armorstand.
		}

	
	
	
}
