package org.axtin.modules.ships;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Destinations {

    SPAWN(new Location(Bukkit.getWorld("world"),0.5,102,56.5,-180,0),"Axtin Station", -1, DestinationType.LOCATION, -1),
    MARS(new Location(Bukkit.getWorld("SolarSystem"),57.5,76,133.5, 180, 0),"Mars", 30, DestinationType.PLANET, 180),
    //PVP(new Location(Bukkit.getWorld("PvP"),111, 146, -16,0,0), "Asteroid Belt", 45, DestinationType.LOCATION, 180),
    NULL(new Location(Bukkit.getWorld("world"),0.5,95,56.5,-180,0),"null", 1, DestinationType.NONE, -1),
    PLUTO(new Location(Bukkit.getWorld("ShipWorld"), 0.5, 114, 1000.5, -180, 0), "Pluto", 180, DestinationType.PLANET, 180),
    TUTORIAL_SPAWN(new Location(Bukkit.getWorld("world"),0.5,102,56.5,-180,0),"Axtin Station", 15, DestinationType.LOCATION, -1);

    private Location teleport;
    private Location respawn;
    private Location pad;
    private ItemStack i;
    private String name;
    private DestinationType t;
    private int delay;
    private int degrees;
    Destinations(Location l, String name, int delay, DestinationType t, int degrees){
        this.respawn = l;
        this.name = name;
        this.teleport = l;
        this.delay = delay;
        this.pad = new Location(l.getWorld(),l.getX(),l.getY() - 2,l.getZ());
        this.t = t;
        this.degrees = degrees;
    }

    Destinations(Location l, Material m, String name, int delay, DestinationType t, int degrees, Location respawn){
        this.respawn = respawn;
        this.teleport = l;
        this.name = name;
        this.delay = delay;
        this.pad = new Location(l.getWorld(),l.getX(),l.getY() - 2,l.getZ());
        this.t = t;
        this.degrees = degrees;
    }

    private enum DestinationType {
        PLANET, LOCATION, NONE
    }

    public Location getPad(){
        return pad;
    }

    public ItemStack getItem() {
        return i;
    }

    public Location getTP() {
        return teleport;
    }

    public static Destinations getDestination(String name) {
        for (Destinations d : Destinations.values()) {
            if (d.toString().equalsIgnoreCase(name)) {
                return d;
            }
        }
        return Destinations.SPAWN;
    }

    public int getDelay() {
        return this.delay;
    }

    public DestinationType getType() {
        return t;
    }

    public int getDegrees() {
        return degrees;
    }

    public Location getRespawn() {
        return respawn;
    }

    public String getName() {
        return name;
    }
}
