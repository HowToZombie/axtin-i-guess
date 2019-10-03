package org.axtin.util;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.GenericAttributes;

public class EntityUtil {
	public static void setSpeed(Entity entity, double speed){
        final AttributeInstance attributes = ((EntityInsentient)((CraftEntity)entity).getHandle()).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        attributes.setValue(speed);
	}
}
