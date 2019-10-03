package org.axtin.modules.misccommands.afk;

import org.axtin.container.facade.Container;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Map;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AfkChecker {

    private static AfkChecker instance = null;

    public static AfkChecker getInstance(){
        if(instance==null){
            instance = new AfkChecker();
        }
        return instance;
    }

    private AfkChecker(){
        this.startChecker();
        this.stillAfkChecker();
    }

    private void startChecker(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Container.get(Plugin.class), new Runnable() {
            @Override
            public void run() {
                AfkHandler handler = AfkHandler.getInstance();
                Bukkit.getOnlinePlayers().stream()
                        .filter(p -> !handler.isAfk(p.getName()))
                        .forEach(p -> handler.checkPlayer(p.getName(), p.getLocation().toVector()));

            }
        }, 0L, 5*60*20);

    }

    private void stillAfkChecker(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Container.get(Plugin.class), new Runnable() {
            @Override
            public void run() {
                AfkHandler handler = AfkHandler.getInstance();
                for(Map.Entry<String, Vector> entry: handler.getAfkPlayers().entrySet()){
                    Player player = Bukkit.getPlayer(entry.getKey());
                    if(player==null){
                        handler.silentUnAfk(entry.getKey());
                        continue;
                    }
                    if(player.getLocation().toVector().equals(entry.getValue())) continue;
                    handler.unAfk(player.getName());
                }
            }
        },0L, 20L);
    }


}
