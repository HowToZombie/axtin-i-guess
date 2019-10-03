package org.axtin.deprecated.modules.companions;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.axtin.container.facade.Container;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.md_5.bungee.api.ChatColor;

public class Companion {

	public UUID owner;
	public Player ownerPlayer;
	public CmpnType type;
	public Location loc;
	public String name;
	public ArmorStand as;
	public boolean spawned;
	public boolean ownerOnline;
	public boolean moving;
	
	public Companion(Player owner) {
		this.owner = owner.getUniqueId();
		setSpawned(false);
		this.moving = false;
	}
	
	public Companion(UUID uuid, YamlConfiguration config) {
		Map<String, Object> settings = config.getConfigurationSection("Companions." + uuid.toString() + ".Settings").getValues(false);
		this.owner = uuid;
		this.type = CmpnType.valueOf((String) settings.get("type"));
		this.name = (String) settings.get("name");
		setSpawned(false);
		this.moving = false;
	}
	
	public boolean isSpawned() {
		return this.spawned;
	}
	
	public void setSpawned(boolean bool) {
		this.spawned = bool;
	}
	
	public void spawn(Player p, String name, CmpnType type) {
		ArmorStand as = (ArmorStand) EntityTypes.spawnEntity(new CustomArmorStand(p.getWorld(), this), p.getLocation());
		
		as.setGravity(true);
		as.setArms(false);
		as.setVisible(false);
		as.setSmall(true);
		as.setHelmet(getHead(type.getTexture()));
		
		as.setMetadata("companion_holder", new FixedMetadataValue(Container.get(Plugin.class), p.getUniqueId()));
		
		this.type = type;
		this.loc = as.getLocation();
		this.name = name;
		this.as = as;
		this.ownerOnline = true;
		this.ownerPlayer = p;
		setSpawned(true);
		
	}
	
	public static ItemStack getHead(String paramString)
	{
	    ItemStack localItemStack = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
	    SkullMeta localSkullMeta = (SkullMeta)localItemStack.getItemMeta();
	    
	    GameProfile localGameProfile = new GameProfile(UUID.randomUUID(), null);
	    byte[] arrayOfByte = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[] { paramString }).getBytes());
	    localGameProfile.getProperties().put("textures", new Property("textures", new String(arrayOfByte)));
	    Field localField = null;
	    try
	    {
	    	localField = localSkullMeta.getClass().getDeclaredField("profile");
	    	localField.setAccessible(true);
	    	localField.set(localSkullMeta, localGameProfile);
	    }
	    catch (NoSuchFieldException|IllegalArgumentException|IllegalAccessException localNoSuchFieldException)
	    {
	    	System.out.println("error: " + localNoSuchFieldException.getMessage());
	    }
	    localSkullMeta.setDisplayName("head");
	    localItemStack.setItemMeta(localSkullMeta);
	    
	    return localItemStack;
	}
	
	public void hide() {
		setSpawned(false);
		this.as.remove();
		this.as = null;
	}
	
	public void show(Player player) {
		setSpawned(true);
		spawn(player, name, type);
	}
	
	public void sendMessage(Player receiver, String msg) {
		receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', "[" + this.name + "&r] " + msg));
	}
	
	public void moveTowardPlayer(double speed){
		this.moving = true;
        Location loc = this.as.getLocation();
        Location to = ownerPlayer.getEyeLocation();
        double x = loc.getX() - to.getX();
        double y = loc.getY() - to.getY();
        double z = loc.getZ() - to.getZ();
        Vector velocity = new Vector(x, y, z).normalize().multiply(-speed);
        this.as.setVelocity(velocity);   
    }
	
	public void stop() {
		this.moving = false;
		as.setVelocity(as.getVelocity().multiply(0.1));
	}
	
	public void setVelocity(Vector vector) {
		this.as.setVelocity(vector);
	}
	
	public void teleport(Location loc) {
		this.as.teleport(loc);
	}
	
	public void teleport(double x, double y, double z, World world) {
		Location loc = new Location(world, x, y, z);
		this.as.teleport(loc);
	}
	
	public Map<String, Object> getSettings() {
		Map<String, Object> settings = new HashMap<>();
		settings.put("type", this.type.toString());
		settings.put("name", this.name);
		settings.put("uuid", this.owner);
		return settings;
	}
	
	public void save(String path, YamlConfiguration config) {
		config.createSection(path + this.owner.toString() +  ".Settings", getSettings());
	}
	
}
