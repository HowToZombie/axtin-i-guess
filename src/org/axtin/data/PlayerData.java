package org.axtin.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerData {
	
	private final Player player;
	
	private ItemStack[] d_inv, d_armor;
	private boolean d_flying, d_flyingAllowed;
	private int d_level, d_food;
	private float d_exp;
	private double d_health, d_maxHealth;
	private List<PotionEffect> d_effects;
	
	public PlayerData(Player player){
		this.player = player;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	@SuppressWarnings("deprecation")
	public void read(){
		this.d_inv = player.getInventory().getContents();
		this.d_armor = player.getInventory().getArmorContents();
		this.d_flying = player.isFlying();
		this.d_flyingAllowed = player.getAllowFlight();
		this.d_level = player.getLevel();
		this.d_food = player.getFoodLevel();
		this.d_exp = player.getExp();
		this.d_health = player.getHealth();
		this.d_maxHealth = player.getMaxHealth();
		this.d_effects = new ArrayList<PotionEffect>(player.getActivePotionEffects());
	}
	
	@SuppressWarnings("deprecation")
	public void write(){
		player.getInventory().setContents(d_inv);
		player.getInventory().setArmorContents(d_armor);
		player.setAllowFlight(d_flyingAllowed);
		player.setFlying(d_flying);
		player.setLevel(d_level);
		player.setFoodLevel(d_food);
		player.setExp(d_exp);
		player.setHealth(d_health);
		player.setMaxHealth(d_maxHealth);
		
		for(PotionEffect effect:player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		for(PotionEffect effect:d_effects)
			player.addPotionEffect(effect, true);
	}
	
	@SuppressWarnings("deprecation")
	public static void resetPlayer(Player player){
		player.getInventory().clear();
		player.setAllowFlight(false);
		player.setFlying(false);
		player.setLevel(0);
		player.setFoodLevel(20);
		player.setExp(0);
		player.setMaxHealth(20);
		player.setHealth(20);
		for(PotionEffect effect:player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
	}
}
