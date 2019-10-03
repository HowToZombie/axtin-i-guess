package org.axtin.modules.maps;

import org.axtin.util.FileStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;

public class ImageMap {

    private String direction,name;
    private AxtinMap.MapType mapType;
    private Location startinPos;
    private BlockFace face;
    public ArrayList<SoftReference<BufferedImage>> imgs;
    public SplitImage splitImage;

    public ImageMap(String name, String s, AxtinMap.MapType type){
        this.name = name.toLowerCase();
        this.mapType = type;
        this.direction = s;
        this.splitImage = new SplitImage();
        try {
            this.imgs = this.splitImage.getImages(direction, mapType);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void generate(Location startingLoc, BlockFace face, Block startingBlock, Player p){
        this.startinPos = startingLoc;
        this.face = face;
        ArrayList<ItemStack> maps = new ArrayList<>();
        for(SoftReference<BufferedImage> b: this.imgs){
            short id=  Bukkit.createMap(startingLoc.getWorld()).getId();

            ItemStack i = new ItemStack(Material.MAP);
            i.setDurability(id);

            MapView view = Bukkit.getMap(i.getDurability());
            Iterator<MapRenderer> iter = view.getRenderers().iterator();
            while(iter.hasNext()){
                view.removeRenderer(iter.next());
            }

            try{
                AxtinMap renderer = new AxtinMap(b);
                view.addRenderer(renderer);
                //Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                 //       "&6Rendering " + name + "!"));
            }catch (IOException e){
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cFailed to load Image."));
                e.printStackTrace();
            }
            maps.add(i);
        }
        int count = 0;
        boolean canBePlaced = true;

        ArrayList<Location> frames = new ArrayList<>();

        if (face.equals(BlockFace.EAST)) {
            for (int i = 0; i < splitImage.mapsHigh; i++) {
                Location yTemp = startingLoc.clone();
                yTemp.add(0, i, 0);
                for (int a = 0; a < splitImage.mapsWide; a++) {
                    Location temp = yTemp.clone();
                    frames.add(temp.add(0, 0, -a));
                    if (startingBlock.getLocation().add(0, i, -a).getBlock().getType().equals(Material.AIR) ||
                            !temp.getBlock().getType().equals(Material.AIR)) {
                        canBePlaced = false;
                    }
                }
            }
        } else if (face.equals(BlockFace.NORTH)) {
            for (int i = 0; i < splitImage.mapsHigh; i++) {
                Location yTemp = startingLoc.clone();
                yTemp.add(0, i, 0);
                for (int a = 0; a < splitImage.mapsWide; a++) {
                    Location temp = yTemp.clone();
                    frames.add(temp.add(-a, 0, 0));
                    if (startingBlock.getLocation().add(-a, i, 0).getBlock().getType().equals(Material.AIR) ||
                            !temp.getBlock().getType().equals(Material.AIR)) {
                        canBePlaced = false;
                    }
                }
            }
        } else if (face.equals(BlockFace.WEST)) {
            for (int i = 0; i < splitImage.mapsHigh; i++) {
                Location yTemp = startingLoc.clone();
                yTemp.add(0, i, 0);
                for (int a = 0; a < splitImage.mapsWide; a++) {
                    Location temp = yTemp.clone();
                    frames.add(temp.add(0, 0, a));
                    if (startingBlock.getLocation().add(0, i, a).getBlock().getType().equals(Material.AIR) ||
                            !temp.getBlock().getType().equals(Material.AIR)) {
                        canBePlaced = false;
                    }
                }
            }
        } else if (face.equals(BlockFace.SOUTH)) {
            for (int i = 0; i < splitImage.mapsHigh; i++) {
                Location yTemp = startingLoc.clone();
                yTemp.add(0, i, 0);
                for (int a = 0; a < splitImage.mapsWide; a++) {
                    Location temp = yTemp.clone();
                    frames.add(temp.add(a, 0, 0));
                    if (startingBlock.getLocation().add(a, i, 0).getBlock().getType().equals(Material.AIR) ||
                            !temp.getBlock().getType().equals(Material.AIR)) {
                        canBePlaced = false;
                    }
                }
            }
        }

        if (canBePlaced) {
            for (Entity e : startingLoc.getWorld().getEntities()) {
                if (e instanceof ItemFrame && e.getName().equalsIgnoreCase(this.name)) {
                    e.remove();
                }
            }
            for (Location l : frames) {
                ItemFrame frame = (ItemFrame) startingLoc.getWorld().spawnEntity(l, EntityType.ITEM_FRAME);
                frame.setItem(maps.get(count));
                frame.setCustomName(this.name);
                count++;
            }
            p.getInventory().removeItem(p.getItemInHand());
            save();
        } else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cNot enough space! You need a " + splitImage.mapsHigh + "x" + splitImage.mapsWide + " block area!"));
        }

    }


    public void save(){
        FileStorage map = new FileStorage(name,"maps");
        map.getConfig().set("Type",mapType.toString());
        map.getConfig().set("Name",name);
        map.getConfig().set("World",startinPos.getWorld().getName());
        map.getConfig().set("StartingX",startinPos.getX());
        map.getConfig().set("StartingY",startinPos.getY());
        map.getConfig().set("StartingZ",startinPos.getZ());
        map.getConfig().set("BlockFace",face.toString());
        map.getConfig().set("Direction",direction);
        map.getConfig().set("Height",splitImage.mapsHigh);
        map.getConfig().set("Width",splitImage.mapsWide);
        map.saveConfig();
    }


}
