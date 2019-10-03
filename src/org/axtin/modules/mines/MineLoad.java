package org.axtin.modules.mines;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;


import java.io.File;

public class MineLoad {

    public void loadRankUps(){
        File dir = new File(Container.get(Plugin.class).getDataFolder() + "/ranks/");
        if(!dir.exists())
            dir.mkdir();
        
        for(File file : dir.listFiles()){
            RankConfig config = new RankConfig(file.getName().replace(".yml", ""));

            String name = config.getConfig().getString("Name");
            Location region1 = new Location(Bukkit.getWorld(config.getConfig().getString("Region.World")), config.getConfig().getDouble("Region.First.X"), config.getConfig().getDouble("Region.First.Y"), config.getConfig().getDouble("Region.First.Z"));
            Location region2 = new Location(Bukkit.getWorld(config.getConfig().getString("Region.World")), config.getConfig().getDouble("Region.Second.X"), config.getConfig().getDouble("Region.Second.Y"), config.getConfig().getDouble("Region.Second.Z"));

            if(!RankHolder.data.containsKey(name)){
                RankHolder.data.put(name, new RankHolder(name,new Cuboid(region1, region2)));

            }
        }
    }

}
