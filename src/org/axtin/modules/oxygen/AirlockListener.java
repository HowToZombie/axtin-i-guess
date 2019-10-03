package org.axtin.modules.oxygen;

import org.axtin.container.facade.Container;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by zombi on 6/14/2017.
 */
public class AirlockListener implements Listener {

    HashMap<String, String> second = new HashMap<String,String>();

    @EventHandler
    public void HitPlugin(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action a = e.getAction();
        if(a.equals(Action.LEFT_CLICK_BLOCK)) {
            Block b = e.getClickedBlock();
            Location loc = b.getLocation();

            if (AirLockCommand.temp.containsKey(p.getName()) && (!second.containsKey(p.getName()))) {
                File f = new File(Container.get(Plugin.class).getDataFolder() + "/airlocks/" + AirLockCommand.temp.get(p.getName()) + ".yml");
                YamlConfiguration config;
                config = YamlConfiguration.loadConfiguration(f);


                config.set("Region.Maximum.X", loc.getX());
                config.set("Region.Maximum.Y", loc.getY());
                config.set("Region.Maximum.Z", loc.getZ());

                try {
                    config.save(f);
                } catch (IOException exception) {
                    System.out.println("CANNOT SAVE FILE: " + f);
                    exception.printStackTrace();
                }

                second.put(p.getName(), AirLockCommand.temp.get(p.getName()));
                p.sendMessage(ChatColor.GREEN + "Please hit the second block");
                AirLockCommand.temp.remove(p.getName());
                e.setCancelled(true);
                return;
            }
            if (second.containsKey(p.getName())){
                e.setCancelled(true);

                File f = new File(Container.get(Plugin.class).getDataFolder() + "/airlocks/" + this.second.get(p.getName()) + ".yml");
                YamlConfiguration config;
                config = YamlConfiguration.loadConfiguration(f);

                config.set("Region.Minimum.X", loc.getX());
                config.set("Region.Minimum.Y", loc.getY());
                config.set("Region.Minimum.Z", loc.getZ());

                try {
                    config.save(f);
                } catch (IOException exception) {
                    System.out.println("CANNOT SAVE FILE: " + f);
                    exception.printStackTrace();
                }
                p.sendMessage(ChatColor.GREEN + "Region set");
                second.remove(p.getName());
                new LoadAirlocks().loadAreas();


                return;

            }
        }
    }

    /*public void airlockParticles() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Container.get(Plugin.class), new Runnable() {
            @Override
            public void run() {
                File dir = new File(Container.get(Plugin.class).getDataFolder() + "/airlocks/");
                if(!dir.exists())
                    dir.mkdir();

                for(File file : dir.listFiles()) {
                    YamlConfiguration config;
                    config = YamlConfiguration.loadConfiguration(file);

                    World world = Bukkit.getWorld(config.getString("Region.World"));

                    double minX = Math.min(config.getDouble("Region.Maximum.X"), config.getDouble("Region.Minimum.X"));
                    double minY = Math.min(config.getDouble("Region.Maximum.Y"), config.getDouble("Region.Minimum.Y"));
                    double minZ = Math.min(config.getDouble("Region.Maximum.Z"), config.getDouble("Region.Minimum.Z"));
                    double maxX = Math.max(config.getDouble("Region.Maximum.X"), config.getDouble("Region.Minimum.X"));
                    double maxY = Math.max(config.getDouble("Region.Maximum.Y"), config.getDouble("Region.Minimum.Y"));
                    double maxZ = Math.max(config.getDouble("Region.Maximum.Z"), config.getDouble("Region.Minimum.Z"));

                    Location min = new Location(world, minX, minY, minZ);
                    Location max = new Location(world, maxX, maxY, maxZ);

                    List<Location> particles;
                    particles = getHollowCube(min, max);

                    System.out.println("loaded " + config.get("Name"));

                    for (Location l : particles) {
                        world.spawnParticle(Particle.CRIT, l, 1);
                    }

                }


            }
        }, 0L, 100L);
    }

    public List<Location> getHollowCube(Location corner1, Location corner2) {
        List<Location> result = new ArrayList<Location>();
        World world = corner1.getWorld();
        double minX = Math.min(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        // 2 areas
        for (double x = minX; x <= maxX; x+=0.5D) {
            for (double z = minZ; z <= maxZ; z+=0.5D) {
                result.add(new Location(world, x, minY, z));
                result.add(new Location(world, x, maxY, z));
            }
        }

        // 2 sides (front & back)
        for (double x = minX; x <= maxX; x+=0.5D) {
            for (double y = minY; y <= maxY; y+=0.5D) {
                result.add(new Location(world, x, y, minZ));
                result.add(new Location(world, x, y, maxZ));
            }
        }

        // 2 sides (left & right)
        for (double z = minZ; z <= maxZ; z+=0.5D) {
            for (double y = minY; y <= maxY; y+=0.5D) {
                result.add(new Location(world, minX, y, z));
                result.add(new Location(world, maxX, y, z));
            }
        }

        return result;
    }*/
}