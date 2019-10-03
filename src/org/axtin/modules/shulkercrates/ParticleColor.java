package org.axtin.modules.shulkercrates;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;

public enum ParticleColor {
	
	WHITE(1f, 1f, 1f),
	GREY(0.5f, 0.5f, 0.5f),
	BLACK(0.01f, 0f, 0f),
	RED(1f, 0f, 0f),
	ORANGE(1f, 0.5f, 0f),
	YELLOW(1f, 1f, 0f),
	GREEN(0.01f, 1f, 0f),
	CYAN(0.01f, 1f, 1f),
	BLUE(0.01f, 0f, 1f),
	PURPLE(0.5f, 0f, 1f),
	MAGENTA(1f, 0f, 1f),
	RAINBOW(0.5f, 0.5f, 0.5f);
	
	
	private float r;
	private float g;
	private float b;
	
	private ParticleColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public float getR() {
		return this.r;
	}
	
	public float getG() {
		return this.g;
	}
	
	public float getB() {
		return this.b;
	}
	
	public static ParticleColor getAppropiateColor(Block shulker) {
		ParticleColor color = ParticleColor.RED;
		switch(shulker.getType()) {
			case GREEN_SHULKER_BOX:
				color = ParticleColor.GREEN;
				break;
				
			case CYAN_SHULKER_BOX:
				color = ParticleColor.CYAN;
				break;
				
			case BLUE_SHULKER_BOX:
				color = ParticleColor.BLUE;
				break;
				
			case YELLOW_SHULKER_BOX:
				color = ParticleColor.YELLOW;
				break;
				
			case MAGENTA_SHULKER_BOX:
				color = ParticleColor.MAGENTA;
				break;
				
			case PINK_SHULKER_BOX:
				color = ParticleColor.RED;
				break;
				
			case LIGHT_BLUE_SHULKER_BOX:
				color = ParticleColor.BLUE;
				break;
				
			case BROWN_SHULKER_BOX:
				color = ParticleColor.BLACK;
				break;
				
			case WHITE_SHULKER_BOX:
				color = ParticleColor.WHITE;
				break;
				
			case ORANGE_SHULKER_BOX:
				color = ParticleColor.ORANGE;
				break;
				
			case GRAY_SHULKER_BOX:
				color = ParticleColor.GREY;
				break;
				
			case BLACK_SHULKER_BOX:
				color = ParticleColor.BLACK;
				break;
				
			case RED_SHULKER_BOX:
				color = ParticleColor.RED;
				break;
				
			case PURPLE_SHULKER_BOX:
				color = ParticleColor.PURPLE;
				break;
				
			case LIME_SHULKER_BOX:
				color = ParticleColor.GREEN;
				break;
				
			case SILVER_SHULKER_BOX:
				color = ParticleColor.GREY;
				break;
				
			default:
				color = ParticleColor.values()[new Random().nextInt(ParticleColor.values().length)];
				break;
				
		}
		return color;
	}
	
	@SuppressWarnings("deprecation")
	public void playEffect(Location loc) {
		loc.add(0.5, 0.0, 0.5);
		for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
		    double radius = Math.sin(i);
		    double y = Math.cos(i);
		    for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
		        double x = Math.cos(a) * radius;
		        double z = Math.sin(a) * radius;
		        loc.add(x, y, z);
		        //staticLoc.getWorld().spawnParticle(Particle.SPELL_MOB, staticLoc, 0, 23 / 255, 23 / 255, 23 / 255);
		        loc.getWorld().spigot().playEffect(loc, Effect.COLOURED_DUST, 0, 0, this.getR()/*R*/, this.getG()/*G*/, this.getB()/*B*/, 1, 0, 16); 
		        loc.subtract(x, y, z);
		    }
		}
	}
	
}
