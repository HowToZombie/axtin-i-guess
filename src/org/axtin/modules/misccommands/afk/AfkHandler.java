package org.axtin.modules.misccommands.afk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew on 26/03/2017.
 */
public class AfkHandler {

    private static AfkHandler instance = null;

    public static AfkHandler getInstance(){
        if(instance==null){
            instance = new AfkHandler();
        }
        return instance;
    }

    private AfkHandler(){
        this.afkPlayers = new HashMap<>();
        this.notAfkPlayers = new HashMap<>();
    }

    private final Map<String, Vector> afkPlayers;
    private final Map<String, Vector> notAfkPlayers;

    public void checkPlayer(String playerName, Vector vector){
        if(this.notAfkPlayers.containsKey(playerName)){
            Vector playerVector = this.notAfkPlayers.get(playerName);
            if(playerVector.equals(vector)){
                this.announceAfk(playerName,vector);
                return;
            }
        }
        this.updateNonAfkPlayer(playerName,vector);
    }

    private void updateNonAfkPlayer(String playerName, Vector vector){
        this.notAfkPlayers.put(playerName,vector);
    }

    public void removeNonAfkPlayer(String playerName){
        if(this.notAfkPlayers.containsKey(playerName)) this.notAfkPlayers.remove(playerName);
    }

    public void updatePlayer(String playerName, Vector vector){
        if(afkPlayers.keySet().contains(playerName)){
            this.announceNotAfk(playerName);
            return;
        }
        this.announceAfk(playerName, vector);
    }

    public void unAfk(String playerName){
        this.announceNotAfk(playerName);
    }

    public void silentUnAfk(String playerName){
        this.removePlayer(playerName);
    }

    public boolean isAfk(String playerName){
        return this.afkPlayers.containsKey(playerName);
    }

    private void announceNotAfk(String playerName){
        Bukkit.broadcastMessage(message("&6" + playerName + "&7 is no longer afk"));
        this.removePlayer(playerName);
    }

    private void announceAfk(String playerName, Vector vector){
        Bukkit.broadcastMessage(message("&6" + playerName + "&7 is now afk"));
        this.addPlayer(playerName, vector);
    }
    private String message(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private void removePlayer(String playerName){
        this.afkPlayers.remove(playerName);
    }

    private void addPlayer(String playerName, Vector vector){
        this.afkPlayers.put(playerName, vector);
    }

    public Map<String, Vector> getAfkPlayers(){
        return this.afkPlayers;
    }


}
