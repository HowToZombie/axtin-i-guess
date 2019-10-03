package org.axtin.modules.maps;

import org.axtin.container.facade.Container;
import org.axtin.util.FileStorage;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Joseph on 4/8/2017.
 */
public class MapLoader {

    public void load() {
        File dir = new File(Container.get(Plugin.class).getDataFolder() + "/maps/");
        if(!dir.exists())
        	dir.mkdirs();
        for(File file : dir.listFiles()) {
            FileStorage fs = new FileStorage(file.getName().replace(".yml", ""),"maps");
            YamlConfiguration config = fs.getConfig();
            if(Bukkit.getWorld(config.getString("World")) == null){
            	System.out.println("[FATAL] There is a null world: " + file.getName());
            	continue;
            }
            Location start = new Location(Bukkit.getWorld(config.getString("World")), config.getDouble("StartingX"),
                    config.getDouble("StartingY"), config.getDouble("StartingZ"));
            String face = config.getString("BlockFace");
            String direction = config.getString("Direction");
            String name = config.getString("Name");
            int height = config.getInt("Height");
            int width = config.getInt("Width");

            if (config.getString("Type").equalsIgnoreCase("url")) {

                SplitImage splitImage = new SplitImage();
                ArrayList<SoftReference<BufferedImage>> imgs = new ArrayList<>();
                try {
                    imgs = splitImage.getImages(direction, AxtinMap.MapType.URL);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ArrayList<ItemStack> maps = new ArrayList<>();
                for (SoftReference<BufferedImage> b : imgs) {
                    short id = Bukkit.createMap(start.getWorld()).getId();

                    ItemStack i = new ItemStack(Material.MAP);
                    i.setDurability(id);

                    MapView view = Bukkit.getMap(i.getDurability());
                    Iterator<MapRenderer> iter = view.getRenderers().iterator();
                    while (iter.hasNext()) {
                        view.removeRenderer(iter.next());
                    }

                    try {
                        AxtinMap renderer = new AxtinMap(b);
                        view.addRenderer(renderer);
                        //Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        //       "&6Rendering " + name + "!"));
                    } catch (IOException e) {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cFailed to load Image."));
                        e.printStackTrace();
                    }
                    maps.add(i);
                }
                int count = 0;

                ArrayList<Location> frames = new ArrayList<>();

                if (face.equalsIgnoreCase("east")) {
                    for (int i = 0; i < height; i++) {
                        Location yTemp = start.clone();
                        yTemp.add(0, i, 0);
                        for (int a = 0; a < width; a++) {
                            Location temp = yTemp.clone();
                            frames.add(temp.add(0, 0, -a));
                        }
                    }
                } else if (face.equalsIgnoreCase("north")) {
                    for (int i = 0; i < height; i++) {
                        Location yTemp = start.clone();
                        yTemp.add(0, i, 0);
                        for (int a = 0; a < width; a++) {
                            Location temp = yTemp.clone();
                            frames.add(temp.add(-a, 0, 0));
                        }
                    }
                } else if (face.equalsIgnoreCase("west")) {
                    for (int i = 0; i < height; i++) {
                        Location yTemp = start.clone();
                        yTemp.add(0, i, 0);
                        for (int a = 0; a < width; a++) {
                            Location temp = yTemp.clone();
                            frames.add(temp.add(0, 0, a));
                        }
                    }
                } else if (face.equalsIgnoreCase("south")) {
                    for (int i = 0; i < height; i++) {
                        Location yTemp = start.clone();
                        yTemp.add(0, i, 0);
                        for (int a = 0; a < width; a++) {
                            Location temp = yTemp.clone();
                            frames.add(temp.add(a, 0, 0));
                        }
                    }
                }

                for (Location l : frames) {
                    try {
                        ItemFrame frame = (ItemFrame) start.getWorld().spawnEntity(l, EntityType.ITEM_FRAME);
                        frame.setItem(maps.get(count));
                        frame.setCustomName(name);
                        count++;
                    } catch (IllegalArgumentException e) {
                        for (Entity en : start.getWorld().getNearbyEntities(l, 0.5, 0.5, 0.5)) {
                            if (en instanceof ItemFrame && en.getCustomName().equalsIgnoreCase(name)) {
                                ItemFrame frame = (ItemFrame) en;
                                frame.setItem(maps.get(count));
                                count++;
                            }
                        }
                    }
                }


            } else {

                SplitImage splitImage = new SplitImage();
                ArrayList<SoftReference<BufferedImage>> imgs = new ArrayList<>();
                try {
                    imgs = splitImage.getImages(direction, AxtinMap.MapType.IMAGE);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ArrayList<ItemStack> maps = new ArrayList<>();
                for (SoftReference<BufferedImage> b : imgs) {
                    short id = Bukkit.createMap(start.getWorld()).getId();

                    ItemStack i = new ItemStack(Material.MAP);
                    i.setDurability(id);

                    MapView view = Bukkit.getMap(i.getDurability());
                    Iterator<MapRenderer> iter = view.getRenderers().iterator();
                    while (iter.hasNext()) {
                        view.removeRenderer(iter.next());
                    }

                    try {
                        AxtinMap renderer = new AxtinMap(b);
                        view.addRenderer(renderer);
                        //Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        //       "&6Rendering " + name + "!"));
                    } catch (IOException e) {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cFailed to load Image."));
                        e.printStackTrace();
                    }
                    maps.add(i);
                }
                int count = 0;

                ArrayList<Location> frames = new ArrayList<>();

                if (face.equalsIgnoreCase("east")) {
                    for (int i = 0; i < height; i++) {
                        Location yTemp = start.clone();
                        yTemp.add(0, i, 0);
                        for (int a = 0; a < width; a++) {
                            Location temp = yTemp.clone();
                            frames.add(temp.add(0, 0, -a));
                        }
                    }
                } else if (face.equalsIgnoreCase("north")) {
                    for (int i = 0; i < height; i++) {
                        Location yTemp = start.clone();
                        yTemp.add(0, i, 0);
                        for (int a = 0; a < width; a++) {
                            Location temp = yTemp.clone();
                            frames.add(temp.add(-a, 0, 0));
                        }
                    }
                } else if (face.equalsIgnoreCase("west")) {
                    for (int i = 0; i < height; i++) {
                        Location yTemp = start.clone();
                        yTemp.add(0, i, 0);
                        for (int a = 0; a < width; a++) {
                            Location temp = yTemp.clone();
                            frames.add(temp.add(0, 0, a));
                        }
                    }
                } else if (face.equalsIgnoreCase("south")) {
                    for (int i = 0; i < height; i++) {
                        Location yTemp = start.clone();
                        yTemp.add(0, i, 0);
                        for (int a = 0; a < width; a++) {
                            Location temp = yTemp.clone();
                            frames.add(temp.add(a, 0, 0));
                        }
                    }
                }

                for (Location l : frames) {
                    try {
                        ItemFrame frame = (ItemFrame) start.getWorld().spawnEntity(l, EntityType.ITEM_FRAME);
                        frame.setItem(maps.get(count));
                        frame.setCustomName(name);
                        count++;
                    } catch (IllegalArgumentException e) {
                        for (Entity en : start.getWorld().getNearbyEntities(l, 0.5, 0.5, 0.5)) {
                            if (en instanceof ItemFrame && en.getCustomName().equalsIgnoreCase(name)) {
                                ItemFrame frame = (ItemFrame) en;
                                frame.setItem(maps.get(count));
                                count++;
                            }
                        }
                    }
                }

            }

        }
    }
}
