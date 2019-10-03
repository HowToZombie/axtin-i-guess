package org.axtin.modules.password;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.axtin.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PasswordManager implements Listener {
	
	private List<String> passwords = new ArrayList<String>();
	private HashMap<Player, PlayerData> playersTyping = new HashMap<Player, PlayerData>();
	
	public PasswordManager(){
		PasswordConfig.load(this);
	}
	
	public List<String> getPasswords(){
		return this.passwords;
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event){
		/*final Player player = event.getPlayer();
		final PlayerData data = new PlayerData(player);
		
		data.read();
		PlayerData.resetPlayer(player);
		playersTyping.put(player, data);
		
		player.sendMessage(ChatColor.RED + "Please write your password to authenticate yourself");
		player.sendMessage("");*/
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event){
		/*final Player player = event.getPlayer();
		
		if(playersTyping.containsKey(player)){
			final PlayerData data = playersTyping.get(player);
			
			data.write();
			playersTyping.remove(player);
		}*/
	}
	
	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event){
		
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		
	}
}
