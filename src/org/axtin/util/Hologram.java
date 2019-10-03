package org.axtin.util;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Hologram implements Listener {
    public static HashMap<String, Hologram> holograms = new HashMap<>();

    private List<String> lines = null;
    private Location loc = null;
    private ArmorStand stand = null;
    private ArrayList<ArmorStand> stands = new ArrayList<>();
    private double distanceBetweenStands = 0.24;
    private Boolean active = false;
    private Entity entityToFollow = null;

    public Hologram(Location location, List<String> text, String s) {
        lines = text;
        loc = location;
        active = true;
        startHologram();
        holograms.put(s, this);

        Container.get(PluginManager.class).registerEvents(this, Container.get(Plugin.class));
    }

    public List<ArmorStand> getEntities() {
        return this.stands;
    }

    public void deleteHologram() {
        if (active) {
            lines = null;
            for (ArmorStand stand : stands) {
                stand.remove();
            }

            active = false;
            stands.clear();
        }
    }

    public static void ShutDown() {
        holograms.values().forEach(org.axtin.util.Hologram::deleteHologram);
        holograms.clear();
    }

    private void startHologram() {
        if (active) {
            Integer lineID = -1;
            Location spawnLoc = loc;
            spawnLoc.setY(spawnLoc.getY() + distanceBetweenStands);
            ArmorStand stand = null;
            // ArmorStand prevStand = null;
            for (String line : lines) {
                spawnLoc.setY(spawnLoc.getY() - distanceBetweenStands);
                stand = loc.getWorld().spawn(spawnLoc, ArmorStand.class);
                stand.setRemoveWhenFarAway(false);
                stand.setVisible(false);
                stand.setSmall(true);
                stand.setBasePlate(false);
                stand.setGravity(false);
                stand.setArms(false);
                stand.setCustomNameVisible(true);
                stand.setCustomName(colorString(line));
                stands.add(stand);
                LivingEntity ent = stand;
                ent.setRemoveWhenFarAway(false);
                lineID++;
            }
        }
    }

    public void updateHologram(Hologram holo) {
        if (active) {
            for (ArmorStand stand : stands) {
                stand.remove();
            }
            stands.clear();
            Location spawnLoc = loc;
            spawnLoc.setY(spawnLoc.getY() + distanceBetweenStands);
            ArmorStand stand = null;
            ArmorStand prevStand = null;
            for (String line : lines) {
                spawnLoc.setY(spawnLoc.getY() - distanceBetweenStands);
                stand = loc.getWorld().spawn(spawnLoc, ArmorStand.class);
                stand.setRemoveWhenFarAway(false);
                stand.setVisible(false);
                stand.setSmall(true);
                stand.setBasePlate(false);
                stand.setGravity(false);
                stand.setArms(false);
                stand.setCustomNameVisible(true);
                stand.setCustomName(colorString(line));
                LivingEntity ent = stand;
                if (prevStand != null) {
                    if (entityToFollow != null) {
                        stand.setPassenger(prevStand);
                    }
                }
                ent.setRemoveWhenFarAway(false);
                stands.add(stand);
                prevStand = stand;
            }
            if (entityToFollow != null) {
                entityToFollow.setPassenger(prevStand);
            }

        }
    }

    private String colorString(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
